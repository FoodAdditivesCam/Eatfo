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
import android.content.ContentValues;
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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.myj.foodadditivescam.result.ShowInfo;
import com.myj.foodadditivescam.result.ShowResult;
import com.myj.foodadditivescam.search.SearchAPI;
import com.myj.foodadditivescam.search.Symspell;
import com.myj.foodadditivescam.search.GetResult;
import com.myj.foodadditivescam.search_m.SearchAdapter;
import com.myj.foodadditivescam.userData.EditUserData;

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
    public static String Tag = OCRMainActivity.class.getSimpleName();
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCtg9P-C34UriZhK6LqTg_tbo5a-zI17Ys";
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
    private ImageButton editUserDataBtn;

    private ProgressDialog customProgressDialog;

    private Bitmap bitmap;
    private boolean isOpenCvLoaded = false;
    private Context mContext;
    Intent intent;

    private Button searchBtn;
    private List<String> list;          // ???????????? ?????? ???????????????
    private ListView listView;          // ????????? ????????? ???????????????
    private EditText searchTxt;        // ???????????? ????????? Input ???
    private SearchAdapter adapter;      // ??????????????? ????????? ?????????
    private ArrayList<String> arraylist;
    private ArrayList<String> searchInfoList;

    static {
        System.loadLibrary("opencv_java4");
    }

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
        editUserDataBtn = findViewById(R.id.editUserDataBtn);

        pickPicBtn.setVisibility(View.VISIBLE);
        pickAgainBtn.setVisibility(View.INVISIBLE);
        uploadBtn.setVisibility(View.INVISIBLE);
        backBtn.setVisibility(View.INVISIBLE);

        Uri imageUri = getIntent().getParcelableExtra("imageUri");

        if(imageUri != null) {
            mImageDetails = findViewById(R.id.image_details);
            mMainImage = findViewById(R.id.main_image);

            pickPicBtn.setVisibility(View.GONE);
            mImageDetails.setVisibility(View.GONE);
            pickAgainBtn.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);

            Log.d(TAG, "uri:  " + imageUri);
            intent = new Intent(this, ShowResult.class);
            Bitmap grayBitmap = uploadImage(imageUri);

            //??????????????? ?????? ?????????
            uploadBtn.setOnClickListener(view->{
                // ?????????????????? api ??????
                callCloudVision(grayBitmap);
                //????????? ?????? ??????
                customProgressDialog = new ProgressDialog(this);
                customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // ?????? ?????? ??????
                customProgressDialog.setCancelable(false); //????????? ????????? ???????????? ??????
                customProgressDialog.show(); //????????? ????????????
            });
            //???????????? ????????? ??????????????? ?????????
            backBtn.setOnClickListener(view->{
//                //??????????????? ???????????? ???????????? OCR???????????? ????????????
//                Intent btnIntent = new Intent(this, ImageLoadActivity.class);
//                startActivity(btnIntent);
                //????????? ?????? ?????? ?????? ?????? OCR???????????? ????????????
                finish();
            });
        }
        //?????? ???????????? ????????? ?????????
        pickAgainBtn.setOnClickListener(view->{
            //((ImageLoadActivity)ImageLoadActivity.mContext).createAlterDialog();
            Intent AgainIntent = new Intent(this, ImageLoadActivity.class);
            AgainIntent.putExtra("value", "re");
            startActivity(AgainIntent);
            finish();
        });

        //?????? ??????????????? ?????????
        editUserDataBtn.setOnClickListener(view->{
            //EditUserData ???????????? ?????? ??? ??????
            Intent EditIntent = new Intent(this, EditUserData.class);
            startActivity(EditIntent);
        });

        //????????? ?????? ?????? ????????? ?????? ??????
        searchBtn = findViewById(R.id.searchBtn);
        searchTxt = findViewById(R.id.searchTxt);
        searchBtn.setOnClickListener(view->{
            //?????? ?????? ??????????????? ??????????????? ??????
            String inputMName = searchTxt.getText().toString();
            if(inputMName.equals("")||inputMName.equals(null)){   //????????? ?????? ?????? ??????
                //???????????? ???????????? ?????? ????????? ??????
                searchTxt.requestFocus();
                //????????? ?????????
                Toast.makeText(this, "??????????????? ?????? ??????????????? ???????????????.", Toast.LENGTH_LONG).show();
            }else{  //????????? ??????
                SharedPreferences prefs = getSharedPreferences("isSearch", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isSearch", true);
                editor.commit();

                Intent intent2 = new Intent(this, ShowInfo.class);
                intent2.putExtra("word", inputMName);
                startActivity(intent2);

            }
        });

        searchTxt = (EditText) findViewById(R.id.searchTxt);
        listView = (ListView) findViewById(R.id.listView);
        // ???????????? ????????????.
        list = new ArrayList<String>();
        // ????????? ????????? ???????????? ?????? ????????????.
        settingList();
        // ???????????? ?????? ???????????? arraylist??? ????????????.// list ???????????? ?????????.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);
        list.clear();
        // ???????????? ????????? ???????????? ????????????.
        adapter = new SearchAdapter(list, this);
        // ??????????????? ???????????? ????????????.
        listView.setAdapter(adapter);
        // input?????? ???????????? ????????? "addTextChangedListener" ????????? ???????????? ????????????.
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input?????? ????????? ?????????????????? ????????????.
                // search ???????????? ????????????.
                String text = searchTxt.getText().toString();
                search(text);
            }
        });

    }

    // ????????? ????????? ???????????? ???????????? ????????????.
    private void settingList(){
        SharedPreferences prefs = getSharedPreferences("searchArray", Activity.MODE_PRIVATE);
        // sharedPreferences?????? arrayList ?????????
        String json = prefs.getString("searchArray", null);
        if (json != null) {
            Log.d(Tag, "???????????? ???????????? ?????????");

            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String word = a.optString(i);
                    list.add(word);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("?");
            System.out.println(list);
        }


    }
    // ????????? ???????????? ?????????
    public void search(String charText) {
        // ?????? ??????????????? ???????????? ????????? ?????? ????????????.
        list.clear();
        // ?????? ????????? ???????????? ?????? ???????????? ????????????.
        if (charText.length() == 0) {
            //list.addAll(arraylist);
        }
        // ?????? ????????? ??????..
        else
        {
            // ???????????? ?????? ???????????? ????????????.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist??? ?????? ???????????? ???????????? ??????(charText)??? ???????????? ????????? true??? ????????????.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // ????????? ???????????? ???????????? ????????????.
                    list.add(arraylist.get(i));
                }
            }
        }
        // ????????? ???????????? ????????????????????? ???????????? ???????????? ????????? ???????????? ????????? ????????????.
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
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
                bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);
                // ????????? ?????????
                Log.d(TAG, "isOpenCvLoaded: "+ isOpenCvLoaded);
                Bitmap grayBitmap = GrayScaling(bitmap);
                // ????????? ??????
                mMainImage.setImageBitmap(grayBitmap);
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
                textDetection.setType("TEXT_DETECTION"); //???????????? ????????? ?????? ????????? ????????????
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
                String res = "OCR ?????? ??????\n\n";

                //text split
                String[] resArr = splitString(convertResponseToString(response));
                Log.d(TAG, "<<?????? ????????? ?????? ??? ??????>>\n"+convertResponseToString(response));
                for(int i=0; i<resArr.length;i++){
                    res+=resArr[i];
                    res+="\n"; // "\n"?????? ??????
                }
                Log.d(TAG, "<<?????? ????????? ????????? ??????>>\n"+res);

                // server db data
                String rs[] = getResult(resArr);
                String url = rs[1];
                JSONArray fin_list = new JSONArray(rs[0]);

                //fin_list??? ???????????? ???????????? ????????? ?????? ?????????????????? ?????? ????????? ??????
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
                    Log.d(TAG, i+": "+rm.getName());
                }

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

        // doInBackGround()??? ??????????????? ????????? ?????? ???????????? ??????
        protected void onPostExecute(String result) {
            Log.d(TAG, "?????? ?????? ???????????? ???????????????");
            //????????? ??????
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

    // ????????? ????????? ?????? ?????? ??????
    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "";
        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();

        if (labels != null) {
            message += labels.get(0).getDescription();
        } else {
            message += "nothing";
        }
        return message; //xml??? ????????? ?????? data
    }

    private static String[] splitString(String txt){
        List<String> resList= SplitTest.splitText(txt);
        String[] res = new String[resList.size()];
        for(int i = 0; i<resList.size(); i++){
            res[i] = resList.get(i);
        }
        return res;
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

        try {
            obj.accumulate("input", list);
            obj.accumulate("weight", weight);
        } catch(Exception e) {
            System.out.print("obj.accumulate err"+e.toString());
        }
        System.out.println(obj);

        JSONObject json = GetResult.POST(obj, "result");
        try {
            Log.d("ocr-result","result ????????????!!"+json.toString());
            score =  json.getString("scores");
            url = json.getString("url");
            Log.d("ocr-result", score);
        }catch(Exception e) {
            System.out.print("ocr-result"+e.toString());
        }
        String result[] = {score, url};
        return result;
    }

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
}
