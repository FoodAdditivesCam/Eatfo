package com.myj.foodadditivescam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.myj.foodadditivescam.OCR.OCRMainActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
            boolean first = pref.getBoolean("isFirst", false);
            if(!first){ //최초실행일 경우
                Log.d("Is first Time?", "first");

                //getUserData 액티비티로 이동
                intent = new Intent(this, getUserData.class);

            }else{
                //최초실행이 아니면 OCRMainActivity 실행
                Log.d("Is first Time?", "not first");
                intent = new Intent(this, OCRMainActivity.class);
            }

            startActivity(intent);
            finish();

        }, 3000);
    }
}