package com.myj.foodadditivescam.userData;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.myj.foodadditivescam.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public class EditUserData extends AppCompatActivity {
    Set<String> checked;   //사용자가 체크한 버튼의 텍스트를 저장할 리스트
    Button completeBtn, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12;
    Boolean[] isClicked = new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
    int[] checkList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);

        completeBtn = findViewById(R.id.completeBtn2);    //완료 버튼
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

        //저장된 데이터 불러오기
        checked = pref.getStringSet("checked", new HashSet<String>());
        String tmp = pref.getString("index", "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0");
        tmp = tmp.replace("[", "");
        tmp = tmp.replace("]", "");
        checkList = Stream.of(tmp.split(", ")).mapToInt(Integer::parseInt).toArray();

        //최초실행시 선택한 버튼 색 변경
        Button[] btnlst = {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12};
        for(int i=0; i<12; i++){
            Button btn = btnlst[i];
            String dName = (String) btn.getText();
            Iterator<String> iter = checked.iterator();

            while(iter.hasNext()) { // iterator에 다음 값이 있는 동안 반복
                if(iter.next().equals(dName)){
                    //선택됨으로 바꾸고
                    isClicked[i]=true;
                    //버튼 색 변경 후
                    btn.setBackgroundResource(R.drawable.button_design);
                    btn.setTextColor(Color.WHITE);
                    //리스트에 질병 명 추가
                    checked.add((String)btn.getText());
                    break;
                }
            }
        }

        //질병 버튼 클릭 시
        btn1.setOnClickListener(v -> { // 소화불량 6
            btnClicked(btn1, 0, 6); });
        btn2.setOnClickListener(v -> { // 충치 0
            btnClicked(btn2, 1, 0); });
        btn3.setOnClickListener(v -> { // 변비 9
            btnClicked(btn3, 2, 9); });
        btn4.setOnClickListener(v -> { // 빈혈 8
            btnClicked(btn4, 3, 8); });
        btn5.setOnClickListener(v -> { // 당뇨 2
            btnClicked(btn5, 4, 2); });
        btn6.setOnClickListener(v -> { // 혈당 7
            btnClicked(btn6, 5, 7); });
        btn7.setOnClickListener(v -> { // 고혈압 4
            btnClicked(btn7, 6, 4); });
        btn8.setOnClickListener(v -> { // 위암 5
            btnClicked(btn8, 7, 5); });
        btn9.setOnClickListener(v -> { // 직장암 10
            btnClicked(btn9, 8, 10); });
        btn10.setOnClickListener(v -> { // 유방암 3
            btnClicked(btn10, 9, 3); });
        btn11.setOnClickListener(v -> { // 심장질환 1
            btnClicked(btn11, 10, 1); });
        btn12.setOnClickListener(v -> { // 골다공증 11
            btnClicked(btn12, 11, 11); });

        //사용자가 완료 버튼을 누르면
        completeBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();

            //클릭한 버튼의 정보를 저장한 String Set을 sharedPreference에 저장
            editor.putStringSet("checked", checked);
            editor.apply();
            //클릭한 버튼의 정보를 저장한 int 배열을 string으로 변환 후 sharedPreference에 저장
            String stringForIntArray = Arrays.toString(checkList);
            editor.putString("index", stringForIntArray);
            editor.apply();
            finish();
        });
    }

    private void btnClicked(Button btn, int index, int sIndex){
        if(!isClicked[index]){    //버튼이 선택되지 않았으면
            //선택됨으로 바꾸고
            isClicked[index]=true;
            //버튼 색 변경 후
            btn.setBackgroundResource(R.drawable.button_design);
            btn.setTextColor(Color.WHITE);
            //리스트에 질병 명 추가
            checked.add((String)btn.getText());
            //서버로 보낼 인덱스 추가(1이면 선택)
            checkList[sIndex] = 1;
        }else{  //선택되어있으면
            //선택되지 않음으로 바꾸고
            isClicked[index]=false;
            //버튼 색 변경 후
            btn.setBackgroundResource(R.drawable.button_design_white);
            btn.setTextColor(Color.parseColor("#303F9F")); // todo: 하드코딩 수정
            //리스트에 있는 질병 명 삭제
            checked.remove(btn.getText());
            //리스트에 있는 인덱스 삭제
            checkList[sIndex] = 0;
        }
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alBuilder = new android.app.AlertDialog.Builder(this);
        alBuilder.setMessage("변경된 사항을 저장하고 나가겠습니까?");

        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // 현재 액티비티를 종료
            }
        });
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // 아무런 작업도 하지 않고 돌아간다
                // todo: return 안한다면 여기도 finish 필요
            }
        });
        alBuilder.setTitle("편집 종료");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }
}