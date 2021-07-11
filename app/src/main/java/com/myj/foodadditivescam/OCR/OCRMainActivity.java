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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.myj.foodadditivescam.ProgressDialog;
import com.myj.foodadditivescam.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.myj.foodadditivescam.RawMaterials;
import com.myj.foodadditivescam.result.ShowResult;
import com.myj.foodadditivescam.search.SearchAPI;
import com.myj.foodadditivescam.search.Symspell;
import com.myj.foodadditivescam.search.GetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


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
    private Button pickPicBtn;
    private Button pickAgainBtn;
    private Button uploadBtn;
    private ImageButton backBtn;

    private ProgressDialog customProgressDialog;

    private Bitmap bitmap;
    private List boundary=new ArrayList<List>();
    private boolean isOpenCvLoaded = false;
    private Context mContext;
    Intent intent;

    static {
        System.loadLibrary("opencv_java4");
    }

//    if (!OpenCVLoader.initDebug()){
//        Log.e("OpenCv", "Unable to load OpenCV");
//    }
//    else{
//        Log.d("OpenCv", "OpenCV loaded");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_activity_main);
        mContext =  this.getApplicationContext();

        // OpenCV load
        OpenCVLoad();

        pickPicBtn = findViewById(R.id.pickPicBtn);
        pickAgainBtn = findViewById(R.id.pickAgainBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        backBtn = findViewById(R.id.backBtn);

        pickPicBtn.setVisibility(View.VISIBLE);
        pickAgainBtn.setVisibility(View.INVISIBLE);
        uploadBtn.setVisibility(View.INVISIBLE);
        backBtn.setVisibility(View.INVISIBLE);

        Uri imageUri = getIntent().getParcelableExtra("imageUri");

        if(imageUri != null) {
            mImageDetails = findViewById(R.id.image_details);
            mMainImage = findViewById(R.id.main_image);

            pickPicBtn.setVisibility(View.INVISIBLE);
            mImageDetails.setVisibility(View.INVISIBLE);
            pickAgainBtn.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);

            Log.d("minjeong", "uri:  " + imageUri);

            intent = new Intent(this, ShowResult.class);
            Bitmap grayBitmap = uploadImage(imageUri);

            //업로드하기 버튼 누르면
            uploadBtn.setOnClickListener(view->{
                // 클라우드비전 api 시작
                callCloudVision(grayBitmap);

//                LayoutInflater inflater = getLayoutInflater();
//                View loading_view = inflater.inflate(R.layout.dialog_progress, null);

                //로딩창 객체 생성
                customProgressDialog = new ProgressDialog(this);
                //로딩창을 투명하게
                customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //로딩창 터치로 종료하지 않기
                customProgressDialog.setCancelable(false);
                //로딩창 보여주기
                customProgressDialog.show();
//                ImageView gif_view = loading_view.findViewById(R.id.gif_view);
//                Glide.with(OCRMainActivity.this).load(R.raw.loading).into(gif_view);
            });

            //뒤로가기 화살표 이미지버튼 누르면
            backBtn.setOnClickListener(view->{
                //이미지로드 액티비티 호출하여 OCR메인으로 돌아가기
                Intent intent = new Intent(this, ImageLoadActivity.class);
                startActivity(intent);
                finish();
            });

        }
        //다시 선택하기 버튼을 누르면
        pickAgainBtn.setOnClickListener(view->{
            //((ImageLoadActivity)ImageLoadActivity.mContext).createAlterDialog();
            Intent intent = new Intent(this, ImageLoadActivity.class);
            intent.putExtra("value", "re");
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ImageLoadActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // opencv load
        OpenCVLoad();
    }
    // LoaderCallback
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(mContext) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public Bitmap uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                // api 시작
//                callCloudVision(bitmap);

                // 이미지 전처리
                Bitmap grayBitmap = bitmap;
                Log.d(TAG, "isOpenCvLoaded: "+ isOpenCvLoaded);
                grayBitmap = GrayScaling(bitmap);
//                grayBitmap = detectEdge(bitmap);

                // 이미지 로드
                mMainImage.setImageBitmap(grayBitmap); //bitmap
                return grayBitmap;

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
        return null;
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
        private final Vision.Images.Annotate mRequest;

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

                //text split
                String[] resArr = splitString(convertResponseToString(response));
                List<String> wordsArr = responseToList(response);

//                String word = "";
//                for(int i=0;i<wordsArr.size();i++){
//                    word+=(wordsArr.get(i)+"\n");
//                }
//                Log.d("wordsArr", "<<ocr에서 직접 split된 결과>>\n"+word);

                for(int i=0; i<resArr.length;i++){
                    res+=resArr[i];
                    res+="\n"; // "\n"으로 수정
                }
                Log.d("wordsArr", "<<직접 텍스트 처리한 결과>>\n"+res);

                // 성분 개수 오타 교정 후 백과사전에 검색
//                String result = null;
//                for(int i=0; i<resArr.length;i++){
//                    // String sym = symspell(resArr[i]);
////                    getResult(resArr);
//                    // result += sym + "\n";
//
//                    // 네이버 백과사전 API 검색
//                    // result += resultAPI(sym);
//                }
                // server db data
                String rs[] = getResult(resArr);
                String url = rs[1];
                JSONArray fin_list = new JSONArray(rs[0]);

                //fin_list에 들어있는 데이터를 꺼내서 객체 만들어가지고 객체 배열에 저장
                RawMaterials[] rms = new RawMaterials[fin_list.length()];

                for(int i=0; i<fin_list.length(); i++){
                    JSONObject jsonObject = fin_list.getJSONObject(i);
                    RawMaterials rm = new RawMaterials(jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("tag1"),
                            jsonObject.getString("tag2"),
                            jsonObject.getString("tag3"),
                            jsonObject.getString("tag4"),
                            jsonObject.getString("tag5"),
                            jsonObject.getString("reference"),
                            jsonObject.getString("link"));
                    rms[i] = rm;
                }

                //intent.putExtra("data", res);
                intent.putExtra("rms", rms);
                intent.putExtra("url", url);
                return res; //result

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            } catch (JSONException e) {
                Log.d(TAG, "failed to change JsonObject " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        // doInBackGround()가 정상적으로 완료된 경우 호출되는 함수
        protected void onPostExecute(String result) {
            Log.d(TAG, "이제 결과 화면으로 넘어갈거임");

            //로딩창 종료
            customProgressDialog.dismiss();
            startActivity(intent);
            finish();
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        mImageDetails.setVisibility(View.INVISIBLE);

        Button pickPicBtn = findViewById(R.id.pickPicBtn);
        pickPicBtn.setVisibility(View.GONE);
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

    private String[] getResult(String[] resArr) {
        JSONObject obj = new JSONObject();
        List<String> list = new ArrayList<String>();
        String score = "";
        String url = "";
        for(int i = 0; i < resArr.length; i++) {
            list.add(resArr[i]);
        }

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        String weight = pref.getString("index", "");
        System.out.println(weight);
//        String[] array_word = weight.split(""); // 한 글자씩 잘라 저장
//        System.out.println(array_word);
//        for(int i = 0; i < array_word.length; i++) {
//            str += array_word[i];
//            if(i != weight.length()-1) {
//                str += ",";
//            }
//        }

        try {
            obj.accumulate("input", list);
            obj.accumulate("weight", weight);
        } catch(Exception e) {
            System.out.print("obj.accumulate err"+e.toString());
        }
        System.out.println(obj);

        JSONObject json = GetResult.POST(obj);
        try {
            Log.d("ocr-result","result 들어왔으!!"+json.toString());
            score =  json.getString("scores");
            url = json.getString("url");
            Log.d("ocr-result", score);
        }catch(Exception e) {
            System.out.print("ocr-result"+e.toString());
        }
        String result[] = {score, url};
        return result;
    }

    private static String symspell(String input) {
        String result = "";
        try{
            // json에서 값이 들어있는 scores 객체를 가져옴
            JSONObject jObject= Symspell.symspell(input);
            JSONObject scores = (JSONObject) jObject.get("scores");
            //System.out.println(scores.toString());

            // 검색 결과 개수 구하기
            ArrayList<String> jArray = new ArrayList<String> ();
            Iterator i = scores.keys();
            while(i.hasNext()) {
                jArray.add(i.next().toString());
            }

            //Log.d("scores: ", String.valueOf(jArray.size()));

            // 이름만 추출하기
            ArrayList<String> jName = new ArrayList<String>();
            for(int j = 0; j < jArray.size(); j++) {
                String element = scores.getString(jArray.get(j));

                // 첫 번째 것만 선택
                if(j == 0) {
                    jName.add(element.split(",")[0]);
                    //Log.d("value", jName.get(j));
                }

            }
            result = jName.get(0);
            // Log.d("scores: ", String.valueOf(jName));

        } catch(Exception e) {
            Log.w("symspell err", e.toString());
        }

        return result;
    }

    //opencv
    private void OpenCVLoad(){
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, mContext, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            isOpenCvLoaded = true;
        }
    }
    private Bitmap GrayScaling(Bitmap bitmap){
        Bitmap grayBitmap=bitmap;
        if( isOpenCvLoaded ) {
            Log.d(TAG, "into GrayScaling");
            // gray scaling
            try {
                Bitmap tempbitmap=bitmap;
                Mat gray = new Mat();
                Utils.bitmapToMat(tempbitmap, gray);

                Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGBA2GRAY);

                grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(gray, grayBitmap);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return grayBitmap;
    }
    private Bitmap Threshold(Bitmap bitmap){
        Bitmap thresBitmap=bitmap;
        if( isOpenCvLoaded ) {
            Log.d(TAG, "into Threshold");
            // gray scaling
            try {
                Bitmap tempbitmap=bitmap;
                Mat thres = new Mat();
                Utils.bitmapToMat(tempbitmap, thres);

//                Imgproc.threshold(thres, thres, 150, 255, Imgproc.THRESH_BINARY);

//                Imgproc.GaussianBlur(thres,thres, new Size(5,5), 0);
//                Imgproc.threshold(thres, thres, 0, 255, Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);

//                Imgproc.adaptiveThreshold(thres, thres, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,21,5);

//                Imgproc.adaptiveThreshold(thres, thres, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,5,10); //21,5

                thresBitmap = Bitmap.createBitmap(thres.cols(), thres.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(thres, thresBitmap);
            }catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Threshold error: "+e);
            }
        }
        return thresBitmap;
    }
    private Bitmap detectEdge(Bitmap bitmap){
        Bitmap edgeBitmap=bitmap;
        if( isOpenCvLoaded ) {
            Log.d(TAG, "into detectEdge");
            // gray scaling
            try {
                Bitmap tempbitmap=bitmap;
                Mat edge = new Mat();
                Utils.bitmapToMat(tempbitmap, edge);
                Mat canny = new Mat();

                Imgproc.Canny(edge, canny, 100, 150);

                edgeBitmap = Bitmap.createBitmap(canny.cols(), canny.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(canny, edgeBitmap);
                Log.d(TAG, "into detectEdge1");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return edgeBitmap;
    }

}
