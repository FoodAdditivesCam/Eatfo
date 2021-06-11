package com.myj.foodadditivescam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.myj.foodadditivescam.OCR.ImageLoadActivity;

public class getUserData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_data);

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);

        Set<String> checked = new HashSet<String>();   //사용자가 체크한 버튼의 텍스트를 저장할 리스트
        pref.getStringSet("checked", checked);
        Button completeBtn = findViewById(R.id.completeBtn);    //완료 버튼
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btn10 = findViewById(R.id.btn10);
        Button btn11 = findViewById(R.id.btn11);
        Button btn12 = findViewById(R.id.btn12);
        Boolean[] isClicked;
        isClicked = new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};

        //질병 버튼 클릭 시
        btn1.setOnClickListener(v -> {
            if(!isClicked[0]){    //버튼이 선택되지 않았으면
                //선택됨으로 바꾸고
                isClicked[0]=true;
                //버튼 색 변경 후
                btn1.setBackgroundColor(Color.LTGRAY);
                //리스트에 질병 명 추가
                checked.add((String)btn1.getText());
            }else{  //선택되어있으면
                //선택되지 않음으로 바꾸고
                isClicked[0]=false;
                //버튼 색 변경 후
                btn1.setBackgroundColor(Color.WHITE);
                //리스트에 있는 질병 명 삭제
                checked.remove(btn1.getText());
            }
        });
        btn2.setOnClickListener(v -> {
            if(!isClicked[1]){
                isClicked[1]=true;
                btn2.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn2.getText());
            }else{
                isClicked[1]=false;
                btn2.setBackgroundColor(Color.WHITE);
                checked.remove(btn2.getText());
            }
        });
        btn3.setOnClickListener(v -> {
            if(!isClicked[2]){
                isClicked[2]=true;
                btn3.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn3.getText());
            }else{
                isClicked[2]=false;
                btn3.setBackgroundColor(Color.WHITE);
                checked.remove(btn3.getText());
            }
        });
        btn4.setOnClickListener(v -> {
            if(!isClicked[3]){
                isClicked[3]=true;
                btn4.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn4.getText());
            }else{
                isClicked[3]=false;
                btn4.setBackgroundColor(Color.WHITE);
                checked.remove(btn4.getText());
            }
        });
        btn5.setOnClickListener(v -> {
            if(!isClicked[4]){
                isClicked[4]=true;
                btn5.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn5.getText());
            }else{
                isClicked[4]=false;
                btn5.setBackgroundColor(Color.WHITE);
                checked.remove(btn5.getText());
            }
        });
        btn6.setOnClickListener(v -> {
            if(!isClicked[5]){
                isClicked[5]=true;
                btn6.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn6.getText());
            }else{
                isClicked[5]=false;
                btn6.setBackgroundColor(Color.WHITE);
                checked.remove(btn6.getText());
            }
        });
        btn7.setOnClickListener(v -> {
            if(!isClicked[6]){
                isClicked[6]=true;
                btn7.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn7.getText());
            }else{
                isClicked[6]=false;
                btn7.setBackgroundColor(Color.WHITE);
                checked.remove(btn7.getText());
            }
        });
        btn8.setOnClickListener(v -> {
            if(!isClicked[7]){
                isClicked[7]=true;
                btn8.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn8.getText());
            }else{
                isClicked[7]=false;
                btn8.setBackgroundColor(Color.WHITE);
                checked.remove(btn8.getText());
            }
        });
        btn9.setOnClickListener(v -> {
            if(!isClicked[8]){
                isClicked[8]=true;
                btn9.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn9.getText());
            }else{
                isClicked[8]=false;
                btn9.setBackgroundColor(Color.WHITE);
                checked.remove(btn9.getText());
            }
        });
        btn10.setOnClickListener(v -> {
            if(!isClicked[9]){
                isClicked[9]=true;
                btn10.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn10.getText());
            }else{
                isClicked[9]=false;
                btn10.setBackgroundColor(Color.WHITE);
                checked.remove(btn10.getText());
            }
        });
        btn11.setOnClickListener(v -> {
            if(!isClicked[10]){
                isClicked[10]=true;
                btn11.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn11.getText());
            }else{
                isClicked[10]=false;
                btn11.setBackgroundColor(Color.WHITE);
                checked.remove(btn11.getText());
            }
        });
        btn12.setOnClickListener(v -> {
            if(!isClicked[11]){
                isClicked[11]=true;
                btn12.setBackgroundColor(Color.LTGRAY);
                checked.add((String)btn12.getText());
            }else{
                isClicked[11]=false;
                btn12.setBackgroundColor(Color.WHITE);
                checked.remove(btn12.getText());
            }
        });

        //사용자가 완료 버튼을 누르면
        completeBtn.setOnClickListener(v -> {
            RadioButton radioYes = findViewById(R.id.yes);
            RadioButton radioNo = findViewById(R.id.no);

            //라디오 버튼을 체크 했는지 확인
            if(radioNo.isChecked() || radioYes.isChecked()){    //체크 했으면
                //최초실행 했다고 저장
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isFirst",true);
                editor.apply();

                //클릭한 버튼의 정보를 저장한 String Set을 sharedPreference에 저장
                editor.putStringSet("checked", checked);
                editor.apply();

                //크롤링 데이터 수신 여부 저장
                editor.putBoolean("crawling", radioYes.isChecked());
                editor.apply();

//                    //제대로 클릭한거 저장됐는지 출력
//                    String str = "";
//                    Iterator<String> iterator = pref.getStringSet("checked", Collections.singleton("")).iterator();
//                    while (iterator.hasNext()){
//                        str+= iterator.next();
//                        str+=" ";
//                    }
//                    Log.d("checked values: ", str);

                //ImageLoadActivity 실행
                Intent intent = new Intent(v.getContext(), ImageLoadActivity.class);
                startActivity(intent);
                finish();
            }else{  //라디오버튼 체크 안했으면
                Toast.makeText(getApplicationContext(), "크롤링 사용 여부를 선택해 주세요.", Toast.LENGTH_LONG).show();
            }
        });

    }



}