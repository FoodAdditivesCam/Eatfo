package com.myj.foodadditivescam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.myj.foodadditivescam.OCR.OCRMainActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //여기에 sharedPreference 생성하고 최초 실행인지 판단해서
                //어떤 액티비티로 넘길지 결정하는 코드 추가하는게 좋을 것 같음
                Intent intent = new Intent(getApplicationContext(), OCRMainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}