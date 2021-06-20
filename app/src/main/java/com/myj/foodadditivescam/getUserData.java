package com.myj.foodadditivescam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
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
        Button btn1 = findViewById(R.id.btn1); // 소화불량 6
        Button btn2 = findViewById(R.id.btn2); // 충치 0
        Button btn3 = findViewById(R.id.btn3); // 변비 9
        Button btn4 = findViewById(R.id.btn4); // 빈혈 8
        Button btn5 = findViewById(R.id.btn5); // 당뇨 2
        Button btn6 = findViewById(R.id.btn6); // 혈당 7
        Button btn7 = findViewById(R.id.btn7); // 고혈압 4
        Button btn8 = findViewById(R.id.btn8); // 위암 5
        Button btn9 = findViewById(R.id.btn9); // 직장암 10
        Button btn10 = findViewById(R.id.btn10); // 유방암 3
        Button btn11 = findViewById(R.id.btn11); // 심장질환 1
        Button btn12 = findViewById(R.id.btn12); // 골다공증 11
        Boolean[] isClicked;
        isClicked = new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
        ArrayList checkList = new ArrayList();

        //질병 버튼 클릭 시
        btn1.setOnClickListener(v -> { // 소화불량 6
            if(!isClicked[0]){    //버튼이 선택되지 않았으면
                //선택됨으로 바꾸고
                isClicked[0]=true;
                //버튼 색 변경 후
                btn1.setBackgroundResource(R.drawable.button_design);
                btn1.setTextColor(Color.WHITE);
                //리스트에 질병 명 추가
                checked.add((String)btn1.getText());
                //서버로 보낼 인덱스 추가
                checkList.add(6, 1);
            }else{  //선택되어있으면
                //선택되지 않음으로 바꾸고
                isClicked[0]=false;
                //버튼 색 변경 후
                btn1.setBackgroundResource(R.drawable.button_design_white);
                btn1.setTextColor(Color.parseColor("#303F9F"));
                //리스트에 있는 질병 명 삭제
                checked.remove(btn1.getText());
                //리스트에 있는 인덱스 삭제
                checkList.add(6, 0);
            }
        });
        btn2.setOnClickListener(v -> { // 충치 0
            if(!isClicked[1]){
                isClicked[1]=true;

                btn2.setBackgroundResource(R.drawable.button_design);
                btn2.setTextColor(Color.WHITE);

                checked.add((String)btn2.getText());
                checkList.add(0, 1);

            }else{
                isClicked[1]=false;

                btn2.setBackgroundResource(R.drawable.button_design_white);
                btn2.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn2.getText());
                checkList.add(6, 0);
            }
        });
        btn3.setOnClickListener(v -> { // 변비 9
            if(!isClicked[2]){
                isClicked[2]=true;

                btn3.setBackgroundResource(R.drawable.button_design);
                btn3.setTextColor(Color.WHITE);

                checked.add((String)btn3.getText());
                checkList.add(9, 1);
            }else{
                isClicked[2]=false;

                btn3.setBackgroundResource(R.drawable.button_design_white);
                btn3.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn3.getText());
                checkList.add(9, 0);
            }
        });
        btn4.setOnClickListener(v -> { // 빈혈 8
            if(!isClicked[3]){
                isClicked[3]=true;

                btn4.setBackgroundResource(R.drawable.button_design);
                btn4.setTextColor(Color.WHITE);

                checked.add((String)btn4.getText());
                checkList.add(8, 1);
            }else{
                isClicked[3]=false;

                btn4.setBackgroundResource(R.drawable.button_design_white);
                btn4.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn4.getText());
                checkList.add(8, 0;
            }
        });
        btn5.setOnClickListener(v -> { // 당뇨 2
            if(!isClicked[4]){
                isClicked[4]=true;

                btn5.setBackgroundResource(R.drawable.button_design);
                btn5.setTextColor(Color.WHITE);

                checked.add((String)btn5.getText());
                checkList.add(2, 1);
            }else{
                isClicked[4]=false;

                btn5.setBackgroundResource(R.drawable.button_design_white);
                btn5.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn5.getText());
                checkList.add(2, 0);
            }
        });
        btn6.setOnClickListener(v -> { // 혈당 7
            if(!isClicked[5]){
                isClicked[5]=true;

                btn6.setBackgroundResource(R.drawable.button_design);
                btn6.setTextColor(Color.WHITE);

                checked.add((String)btn6.getText());
                checkList.add(7, 1);
            }else{
                isClicked[5]=false;

                btn6.setBackgroundResource(R.drawable.button_design_white);
                btn6.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn6.getText());
                checkList.add(7, 0);
            }
        });
        btn7.setOnClickListener(v -> { // 고혈압 4
            if(!isClicked[6]){
                isClicked[6]=true;

                btn7.setBackgroundResource(R.drawable.button_design);
                btn7.setTextColor(Color.WHITE);

                checked.add((String)btn7.getText());
                checkList.add(4, 1);
            }else{
                isClicked[6]=false;

                btn7.setBackgroundResource(R.drawable.button_design_white);
                btn7.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn7.getText());
                checkList.add(4, 0);
            }
        });
        btn8.setOnClickListener(v -> { // 위암 5
            if(!isClicked[7]){
                isClicked[7]=true;

                btn8.setBackgroundResource(R.drawable.button_design);
                btn8.setTextColor(Color.WHITE);

                checked.add((String)btn8.getText());
                checkList.add(5, 1);
            }else{
                isClicked[7]=false;

                btn8.setBackgroundResource(R.drawable.button_design_white);
                btn8.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn8.getText());
                checkList.add(5, 0);
            }
        });
        btn9.setOnClickListener(v -> { // 직장암 10
            if(!isClicked[8]){
                isClicked[8]=true;

                btn9.setBackgroundResource(R.drawable.button_design);
                btn9.setTextColor(Color.WHITE);

                checked.add((String)btn9.getText());
                checkList.add(10, 1);
            }else{
                isClicked[8]=false;

                btn9.setBackgroundResource(R.drawable.button_design_white);
                btn9.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn9.getText());
                checkList.add(10, 0);
            }
        });
        btn10.setOnClickListener(v -> { // 유방암 3
            if(!isClicked[9]){
                isClicked[9]=true;

                btn10.setBackgroundResource(R.drawable.button_design);
                btn10.setTextColor(Color.WHITE);

                checked.add((String)btn10.getText());
                checkList.add(3, 1);
            }else{
                isClicked[9]=false;

                btn10.setBackgroundResource(R.drawable.button_design_white);
                btn10.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn10.getText());
                checkList.add(3, 0);
            }
        });
        btn11.setOnClickListener(v -> { // 심장질환 1
            if(!isClicked[10]){
                isClicked[10]=true;

                btn11.setBackgroundResource(R.drawable.button_design);
                btn11.setTextColor(Color.WHITE);

                checked.add((String)btn11.getText());
                checkList.add(1, 1);
            }else{
                isClicked[10]=false;

                btn11.setBackgroundResource(R.drawable.button_design_white);
                btn11.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn11.getText());
                checkList.add(1, 0);
            }
        });
        btn12.setOnClickListener(v -> { // 골다공증 11
            if(!isClicked[11]){
                isClicked[11]=true;

                btn12.setBackgroundResource(R.drawable.button_design);
                btn12.setTextColor(Color.WHITE);

                checked.add((String)btn12.getText());
                checkList.add(11, 1);
            }else{
                isClicked[11]=false;

                btn12.setBackgroundResource(R.drawable.button_design_white);
                btn12.setTextColor(Color.parseColor("#303F9F"));

                checked.remove(btn12.getText());
                checkList.add(11, 0);
            }
        });

        //사용자가 완료 버튼을 누르면
        completeBtn.setOnClickListener(v -> {
//            RadioButton radioYes = findViewById(R.id.yes);
//            RadioButton radioNo = findViewById(R.id.no);
//
//            //라디오 버튼을 체크 했는지 확인
//            if(radioNo.isChecked() || radioYes.isChecked()){    //체크 했으면
                //최초실행 했다고 저장
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isFirst",true);
                editor.apply();

                //클릭한 버튼의 정보를 저장한 String Set을 sharedPreference에 저장
                editor.putStringSet("checked", checked);
                editor.apply();

//                //크롤링 데이터 수신 여부 저장
//                editor.putBoolean("crawling", radioYes.isChecked());
//                editor.apply();
//
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
//            }else{  //라디오버튼 체크 안했으면
//                Toast.makeText(getApplicationContext(), "크롤링 사용 여부를 선택해 주세요.", Toast.LENGTH_LONG).show();
//            }
        });

    }



}