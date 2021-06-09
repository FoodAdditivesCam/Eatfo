package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //태그 리스트 주고 워드클라우드 그려서 imageView 수정해주기
        Glide.with(this).load(url).into(imageView);

        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        for (RawMaterials rm : rms) {
            //버튼 생성
            Button tagBtn = new Button(this);
            tagBtn.setId(rm.getId());
            tagBtn.setText(rm.getName());
            tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(tagBtn);

            //넘겨줄 데이터 변수로 저장
            String[] name = {rm.getName()};
            String[] tagLst = {rm.getTags()};
            String[] info = {rm.getDescription() + "\n\n출처: " + rm.getReference()};

            //버튼 클릭 이벤트
            tagBtn.setOnClickListener(v -> {
                //원재료명, 태그, 설명을 showInfo 액티비티로 넘겨줌
                Intent intent = new Intent(getApplicationContext(), ShowInfo.class);
                intent.putExtra("itemName", name);
                intent.putExtra("tag", tagLst);
                intent.putExtra("info", info);
                startActivity(intent);
            });
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