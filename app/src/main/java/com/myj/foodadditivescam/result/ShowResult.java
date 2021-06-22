package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.myj.foodadditivescam.OCR.ImageLoadActivity;
import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.RawMaterials;

import java.util.ArrayList;
import java.util.List;

public class ShowResult extends AppCompatActivity {
    private Button oneBtn;
    private Button tagBtn;
    private int DYNAMIC_TAG_ID = 1000;

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
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //태그 리스트 주고 워드클라우드 그려서 imageView 수정해주기
        Glide.with(this).load(url).into(imageView);

        // 태그 버튼
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        String[] startag = {"시작"};
        oneContentLoad(rms, startag,0,0);
        // 각 테그에 대해서 버튼 생성
        int id=0; int taglength = 0;
        for (RawMaterials rm : rms) {
            String[] splitarr = rm.getTags().split(" ");
            for (int i = 0; i < splitarr.length; i++) {
                if(splitarr[i]=="" || splitarr[i]==null) continue;
                taglength++;
            }
        }
        for (RawMaterials rm : rms) {
            String[] splitarr = rm.getTags().split(" ");
            for (int i=0; i<splitarr.length;i++){
                if(splitarr[i]=="" || splitarr[i]==null) continue;
                // 버튼 만들기
                tagBtn = new Button(this);
                tagBtn.setText(splitarr[i]);
                tagBtn.setId(DYNAMIC_TAG_ID+id);
                Log.d("tempbtn", DYNAMIC_TAG_ID + id+"temp");
                tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
                tagBtn.setBackground(getDrawable(R.drawable.tag_button_design));
                tagBtn.setElevation(20);
                linearLayout.addView(tagBtn);

                //넘겨줄 데이터 변수로 저장
                String[] tagLst = {splitarr[i]};

                //버튼 클릭 이벤트
                int finalId = id;
                int finalTaglength = taglength;
                tagBtn.setOnClickListener(v -> {
                    //전체 버튼 색 초기화
                    Button tmpBtn;
                    Log.d("mjview", v.getId()+"");
                    for(int j=0; j<splitarr.length;j++){
                        tmpBtn = findViewById(DYNAMIC_TAG_ID+j);
                        tmpBtn.setBackgroundResource(R.drawable.tag_button_design);
                        tmpBtn.setTextColor(Color.BLACK);
                    }
                    //선택된 버튼의 색 바꾸기
                    tmpBtn = findViewById(DYNAMIC_TAG_ID+ finalId);
                    tmpBtn.setBackgroundResource(R.drawable.button_design);
                    tmpBtn.setTextColor(Color.WHITE);

                    oneContentLoad(rms, tagLst, v.getId(), finalTaglength);
                });
                id++;
            }
        }
    }

    public void oneContentLoad(RawMaterials[] rms,String[] tag, int clickid, int taglength){
        // 원재료 버튼 생성
        FlexboxLayout linearLayoutname = (FlexboxLayout) findViewById(R.id.linearLayout3);
        linearLayoutname.removeAllViews();

        if(taglength!=0) {
            Button tmpBtn;
            for (int j = 0; j < taglength; j++) {
                Log.d("tempbtn", DYNAMIC_TAG_ID + j+"");
                tmpBtn = findViewById(DYNAMIC_TAG_ID + j);
                tmpBtn.setBackgroundResource(R.drawable.tag_button_design);
                tmpBtn.setTextColor(Color.BLACK);
            }
            //선택된 버튼의 색 바꾸기
            tmpBtn = findViewById(clickid);
            tmpBtn.setBackgroundResource(R.drawable.button_design);
            tmpBtn.setTextColor(Color.WHITE);
        }

        for (RawMaterials r : rms) {
            String[] arr = r.getTags().split(" ");
            for (int ii = 0; ii < arr.length; ii++) {

                oneBtn = new Button(this);
                oneBtn.setId(r.getId());
                oneBtn.setText(r.getName());
                oneBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
                oneBtn.setBackgroundResource(R.drawable.button_design_white);
                oneBtn.setTextColor(getResources().getColor(R.color.colorPrimary));

                if(tag[0].equals("시작")){

                }
                else {
                    String[] splitarr = r.getTags().split(" ");
                    for (int i = 0; i < splitarr.length; i++) {
                        if (splitarr[i].equals(tag[0])) {
                            oneBtn.setBackgroundResource(R.drawable.button_design);
                            oneBtn.setTextColor(Color.WHITE);
                        }
                    }
                }
                //버튼 클릭 이벤트
                oneBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ShowInfo.class);
                    intent.putExtra("name", r.getName());
                    intent.putExtra("tag", r.getTags());
                    intent.putExtra("info", r.getDescription() + "\n\n출처: " + r.getReference());
                    startActivity(intent);
                });

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
                layoutManager.setFlexWrap(FlexWrap.WRAP);

                linearLayoutname.addView(oneBtn);
                break;
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