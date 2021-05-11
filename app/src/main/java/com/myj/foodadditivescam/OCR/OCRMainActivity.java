/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myj.foodadditivescam.OCR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.myj.foodadditivescam.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.myj.foodadditivescam.search.SearchAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OCRMainActivity extends AppCompatActivity{
    private static final String CLOUD_VISION_API_KEY = "AIzaSyAZhmCpNXx_rXSAhnGLN_MR2U7EH3X5n88";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = OCRMainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;
    private ImageView mMainImage;
    private Bitmap bitmap;
    private List boundary=new ArrayList<List>();
    private boolean isOpenCvLoaded = false;
    private Context mContext;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext =  this.getApplicationContext();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent2 = new Intent(this, ImageLoadActivity.class);
            startActivity(intent2);
            finish();
        });

        if(getIntent()!=null) {
            mImageDetails = findViewById(R.id.image_details);
            mMainImage = findViewById(R.id.main_image);

            Uri imageUri = (Uri) getIntent().getParcelableExtra("imageUri");

            Log.d("minjeong", "uri:  " + imageUri);

            intent = new Intent(this, ShowResult.class);
            uploadImage(imageUri);
        }

        //intent.putExtra("itemName", );
        //intent.putExtra("tag", );
        //intent.putExtra("info", );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ImageLoadActivity.class);
        startActivity(intent);
        finish();
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                // api 시작
                callCloudVision(bitmap);

                // 이미지 로드
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature textDetection = new Feature();
                textDetection.setType("TEXT_DETECTION"); //클라우드 비전의 어떤 기능을 사용할지
                textDetection.setMaxResults(10);
                add(textDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private class LableDetectionTask extends AsyncTask<Object, Void, String> { //static
        private final WeakReference<OCRMainActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(com.myj.foodadditivescam.OCR.OCRMainActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                String res = "OCR 출력 결과\n\n";

                // 이미지 box boundary
                BoxBoundary box=new BoxBoundary();
                boundary = box.ParagraphBoundary(response);

                //text split
                String[] resArr = splitString(convertResponseToString(response));
                List<String> wordsArr = responseToList(response);

                String word = "";
                for(int i=0;i<wordsArr.size();i++){
                    word+=(wordsArr.get(i)+"\n");
                }
                Log.d("wordsArr", "<<ocr에서 직접 split된 결과>>\n"+word);

                for(int i=0; i<resArr.length;i++){
                    res+=resArr[i];
                    res+="\n"; // "\n"으로 수정
                }
                Log.d("wordsArr", "<<직접 텍스트 처리한 결과>>\n"+res);

                // 성분 개수 만큼 백과사전에 검색
                String result = null;
                for(int i=0; i<resArr.length;i++){
                    result += resultAPI(resArr[i]) + "\n";
                }

                intent.putExtra("itemName", resArr);
                return res; //result

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        // doInBackGround()가 정상적으로 완료된 경우 호출되는 함수
        protected void onPostExecute(String result) {
            com.myj.foodadditivescam.OCR.OCRMainActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                Log.d("minjeong","이미지로드 boundary: "+boundary);
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(7f);
                Canvas canvas = new Canvas(bitmap);
                for(int box=0;box<boundary.size();box+=8){ // -7?
                    canvas.drawLine((float)boundary.get(box), (float)boundary.get(box+1),(float)boundary.get(box+2),(float)boundary.get(box+3),paint);
                    canvas.drawLine((float)boundary.get(box+4), (float)boundary.get(box+5),(float)boundary.get(box+2),(float)boundary.get(box+3),paint);
                    canvas.drawLine((float)boundary.get(box+6), (float)boundary.get(box+7),(float)boundary.get(box+4),(float)boundary.get(box+5),paint);
                    canvas.drawLine((float)boundary.get(box), (float)boundary.get(box+1),(float)boundary.get(box+6), (float)boundary.get(box+7),paint);
                }
            }

            startActivity(intent);
            finish();
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    // 텍스트 결과값 리턴 받아 출력
    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "";
        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();

        if (labels != null) {
            message+=labels.get(0).getDescription();
        } else {
            message += "nothing";
        }
        return message; //xml에 메세지 띄울 data
    }
    // OCR에서 직접 split된 것 가져오기
    private static List responseToList(BatchAnnotateImagesResponse response){
        List<String> words = new ArrayList<>();
        String cur = "", temp="";
        try {
            List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
            for (int i = 1; i < labels.size(); i++) {
                cur=labels.get(i).getDescription();
                if (cur.equals(",")) {
                    words.add(temp);
                    temp="";
                }else{
                    temp+=cur;
                }
            }
            words.add("<<OCR이 split>>");
            for (int i = 1; i < labels.size(); i++) {
                words.add(labels.get(i).getDescription());
            }
        }catch (Exception e){
            Log.d(TAG, "responseToList error: "+e);
        }
        return words;
    }


    private static String[] splitString(String txt){
        List<String> resList= SplitTest.splitText(txt);
        String[] res = new String[resList.size()];
        for(int i = 0; i<resList.size(); i++){
            res[i] = resList.get(i);
        }
        return res;
    }

    // 네이버 백과사전 API 검색 결과를 JSON으로 받아와 파싱해주는 함수
    private static String resultAPI(String word) {
        String jsonResult = SearchAPI.search(word);
        String result = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            String items = jsonObject.getString("items");
            JSONArray jsonArray = new JSONArray(items);

            result += word + "검색 결과\n";

            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                String title = subJsonObject.getString("title");
                String link  = subJsonObject.getString("link");
                String description = subJsonObject.getString("description");
                if ("".equals(title)) {
                    continue;
                }

                result += ("title : " + title + "\n" + "link : " + link + "\n"
                        + "description : " + description + "\n\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
