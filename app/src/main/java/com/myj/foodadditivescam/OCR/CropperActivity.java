package com.myj.foodadditivescam.OCR;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.myj.foodadditivescam.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

        CropImage.activity(Uri.parse(getIntent().getExtras().getString("imageUri")))
                .start(this);
    }
}