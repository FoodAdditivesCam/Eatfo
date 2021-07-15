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

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.myj.foodadditivescam.userData.EditUserData;
import com.myj.foodadditivescam.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;


public class ImageLoadActivity extends AppCompatActivity {
    public static final String FILE_NAME = "temp.jpg";
    public static String Tag = ImageLoadActivity.class.getSimpleName();

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final int REQUEST_IMAGE_CROP = 4;
    public static Context mContext;

    private Button searchBtn;
    private EditText searchTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_activity_main);
        mContext = this;

        try{
            String value = getIntent().getStringExtra("value");
            if(value.equals("re")){
                createAlterDialog();
            }
        }catch (Exception e){
            Log.d(Tag, e.getMessage());
        }

        // 사진 선택하기 버튼
        Button pickPicBtn = findViewById(R.id.pickPicBtn);
        pickPicBtn.setOnClickListener(view->{
            createAlterDialog();
        });

        //공구 이미지버튼 누르면
        ImageButton editUserDataBtn = findViewById(R.id.editUserDataBtn);
        editUserDataBtn.setOnClickListener(view->{
            //EditUserData 액티비티 실행 후 대기
            Intent intent = new Intent(this, EditUserData.class);
            startActivity(intent);
        });

        //드래그 뷰의 검색 버튼을 누른 경우
        searchBtn = findViewById(R.id.searchBtn);
        searchTxt = findViewById(R.id.searchWordTxt);
        searchBtn.setOnClickListener(view->{
            //검색 창에 원재료명을 입력했는지 확인
            String inputMName = searchTxt.getText().toString();
            if(inputMName.equals("")||inputMName.equals(null)){   //입력을 하지 않은 경우
                //포커스를 원재료명 입력 창으로 두고
                searchTxt.requestFocus();
                //토스트 띄우기
                Toast.makeText(this, "검색하고자 하는 원재료명을 입력하세요.", Toast.LENGTH_LONG).show();
            }else{  //입력한 경우

            }
        });
    }

    public void createAlterDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageLoadActivity.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, (dialog, which) -> startGalleryChooser())
                .setNegativeButton(R.string.dialog_select_camera, (dialog, which) -> startCamera());
        builder.create().show();
    }
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            CropImage.activity(uri)
                    .start(this);
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uri = photoUri;
            CropImage.activity(uri)
                    .start(this);
        }

        CropImage.ActivityResult res = CropImage.getActivityResult(data);
        if(res!=null){
            Log.d(Tag,"ocrmainacititydml res:  "+res);
            Uri resUri = res.getUri();

            Log.d(Tag,"ocrmainacititydml uri:  "+resUri);
            Intent intent = new Intent(this, OCRMainActivity.class);
            intent.putExtra("imageUri", resUri);
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alBuilder = new android.app.AlertDialog.Builder(this);
        alBuilder.setMessage("종료하시겠습니까?");

        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // 현재 액티비티를 종료한다. (MainActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
            }
        });
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show();
    }
}