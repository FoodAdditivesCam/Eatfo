package com.myj.foodadditivescam.userData;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.myj.foodadditivescam.OCR.ImageLoadActivity;
import com.myj.foodadditivescam.R;

public class getUserData extends AppCompatActivity {
    Set<String> checked = new HashSet<String>();   //사용자가 체크한 버튼의 텍스트를 저장할 리스트
    Button completeBtn, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12;
    int[] checkList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_data);

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);

        completeBtn = findViewById(R.id.completeBtn);    //완료 버튼
        btn1 = findViewById(R.id.btn1); // 소화불량 6
        btn2 = findViewById(R.id.btn2); // 충치 0
        btn3 = findViewById(R.id.btn3); // 변비 9
        btn4 = findViewById(R.id.btn4); // 빈혈 8
        btn5 = findViewById(R.id.btn5); // 당뇨 2
        btn6 = findViewById(R.id.btn6); // 혈당 7
        btn7 = findViewById(R.id.btn7); // 고혈압 4
        btn8 = findViewById(R.id.btn8); // 위암 5
        btn9 = findViewById(R.id.btn9); // 직장암 10
        btn10 = findViewById(R.id.btn10); // 유방암 3
        btn11 = findViewById(R.id.btn11); // 심장질환 1
        btn12 = findViewById(R.id.btn12); // 골다공증 11

        //질병 버튼 클릭 시
        btn1.setOnClickListener(v -> { // 소화불량 6
            btnClicked(btn1, 6);
        });
        btn2.setOnClickListener(v -> { // 충치 0
            btnClicked(btn2, 0);
        });
        btn3.setOnClickListener(v -> { // 변비 9
            btnClicked(btn3, 9);
        });
        btn4.setOnClickListener(v -> { // 빈혈 8
            btnClicked(btn4, 8);
        });
        btn5.setOnClickListener(v -> { // 당뇨 2
            btnClicked(btn5, 2);
        });
        btn6.setOnClickListener(v -> { // 혈당 7
            btnClicked(btn6, 7);
        });
        btn7.setOnClickListener(v -> { // 고혈압 4
            btnClicked(btn7, 4);
        });
        btn8.setOnClickListener(v -> { // 위암 5
            btnClicked(btn8, 5);
        });
        btn9.setOnClickListener(v -> { // 직장암 10
            btnClicked(btn9, 10);
        });
        btn10.setOnClickListener(v -> { // 유방암 3
            btnClicked(btn10, 3);
        });
        btn11.setOnClickListener(v -> { // 심장질환 1
            btnClicked(btn11, 1);
        });
        btn12.setOnClickListener(v -> { // 골다공증 11
            btnClicked(btn12, 11);
        });

        //사용자가 완료 버튼을 누르면
        completeBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();

            //최초실행 했다고 저장
            editor.putBoolean("isFirst", true);
            editor.apply();
            //클릭한 버튼의 정보를 저장한 String Set을 sharedPreference에 저장
            editor.putStringSet("checked", checked);
            editor.apply();
            //클릭한 버튼의 정보를 저장한 int 배열을 string으로 변환 후 sharedPreference에 저장
            String stringForIntArray = Arrays.toString(checkList);
            editor.putString("index", stringForIntArray);
            editor.apply();
            //ImageLoadActivity 실행
            Intent intent = new Intent(v.getContext(), ImageLoadActivity.class);
            intent.putExtra("value", "first");
            startActivity(intent);
            finish();
        });

    }

    private void btnClicked(Button btn, int sIndex){
        if(checkList[sIndex]==0){    //버튼이 선택되지 않았으면
            //버튼 색 변경 후
            btn.setBackgroundResource(R.drawable.button_design);
            btn.setTextColor(Color.WHITE);
            //리스트에 질병 명 추가
            checked.add((String)btn.getText());
            //서버로 보낼 인덱스 추가(1이면 선택)
            checkList[sIndex] = 1;
        }else{  //선택되어있으면
            //버튼 색 변경 후
            btn.setBackgroundResource(R.drawable.button_design_white);
            btn.setTextColor(Color.parseColor("#303F9F"));
            //리스트에 있는 질병 명 삭제
            checked.remove(btn.getText());
            //리스트에 있는 인덱스 삭제
            checkList[sIndex] = 0;
        }

    }


}