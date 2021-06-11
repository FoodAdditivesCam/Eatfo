package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.myj.foodadditivescam.OCR.ImageLoadActivity;
import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.RawMaterials;

public class ShowResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        //인텐트로 넘어온 원재료 정보 객체 리스트 가져오기
        RawMaterials[] rms = (RawMaterials[]) getIntent().getSerializableExtra("rms");
        String url = (String) getIntent().getSerializableExtra("url");

        //태그 리스트 만들기
        String tags="";
        for(RawMaterials rm : rms){
            tags+=rm.getTags()+" ";
        }
        Log.d("tags",tags);
//        wordCloud wcd = new wordCloud();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //태그 리스트 주고 워드클라우드 그려서 imageView 수정해주기
        Glide.with(this).load(url).into(imageView);

        LinearLayout linearLayout = findViewById(R.id.linearLayout2);

        // 전체 원재료 버튼 추가
        Button tallBtn = new Button(this);
        tallBtn.setId(-1);
        tallBtn.setText("전체 원재료");
        tallBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(tallBtn);
        //넘겨줄 데이터 변수로 저장
        String[] alltag = {"전체 원재료"};
        //버튼 클릭 이벤트
        tallBtn.setOnClickListener(v -> {
            //원재료명, 태그, 설명을 showInfo 액티비티로 넘겨줌
            Intent intent = new Intent(getApplicationContext(), ShowInfo.class);
            intent.putExtra("tag", alltag);
            intent.putExtra("rms",rms);
            startActivity(intent);
        });

        // 각 테그에 대해서 버튼 생성
        for (RawMaterials rm : rms) {
            String[] splitarr = rm.getTags().split(" ");
            for (int i=0; i<splitarr.length;i++){
                if(splitarr[i]=="" || splitarr[i]==null) continue;
                // 버튼 만들기
                Button tagBtn = new Button(this);
                tagBtn.setId(rm.getId());
                tagBtn.setText(splitarr[i]);
                tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.addView(tagBtn);

                //넘겨줄 데이터 변수로 저장
                String[] tagLst = {splitarr[i]};

                //버튼 클릭 이벤트
                tagBtn.setOnClickListener(v -> {
                    //원재료명, 태그, 설명을 showInfo 액티비티로 넘겨줌
                    Intent intent = new Intent(getApplicationContext(), ShowInfo.class);
                    intent.putExtra("tag", tagLst);
                    intent.putExtra("rms",rms);
                    startActivity(intent);
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ImageLoadActivity.class);
        startActivity(intent);
        finish();
    }
}