package com.myj.foodadditivescam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.myj.foodadditivescam.OCR.OCRMainActivity;

public class getUserData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first == false){ //최초실행일 경우
            //로그 찍고 isFirst를 true로 수정
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();

            //get user data 액티비티 뷰 보여주기
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_get_user_data);
            Button completeBtn = findViewById(R.id.completeBtn);

            //사용자가 완료 버튼을 누르면
            completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }else{
            Log.d("Is first Time?", "not first");
            Intent intent = new Intent(this, OCRMainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}