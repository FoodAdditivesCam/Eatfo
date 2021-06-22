package com.myj.foodadditivescam.result;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.internal.FlowLayout;
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
        oneContentLoad(rms, startag);
        // 전체 원재료 버튼 추가
        Button tallBtn = new Button(this);
        tallBtn.setId(DYNAMIC_TAG_ID+0);
        tallBtn.setText("전체 원재료");
        tallBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        tallBtn.setBackground(getDrawable(R.drawable.tag_button_design));
        linearLayout.addView(tallBtn);
        //넘겨줄 데이터 변수로 저장
        String[] alltag = new String[]{"전체 원재료"};
        //버튼 클릭 이벤트
        tallBtn.setOnClickListener(v -> {
            oneContentLoad(rms, alltag);
        });
        // 각 테그에 대해서 버튼 생성
        int id=1;
        for (RawMaterials rm : rms) {
            String[] splitarr = rm.getTags().split(" ");
            for (int i=0; i<splitarr.length;i++){
                if(splitarr[i]=="" || splitarr[i]==null) continue;
                // 버튼 만들기
                tagBtn = new Button(this);
                tagBtn.setText(splitarr[i]);
                tagBtn.setId(DYNAMIC_TAG_ID+id);
                id++;
                tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
                tagBtn.setBackground(getDrawable(R.drawable.tag_button_design));
                tagBtn.setElevation(20);
                linearLayout.addView(tagBtn);

                //넘겨줄 데이터 변수로 저장
                String[] tagLst = {splitarr[i]};

                //버튼 클릭 이벤트
                tagBtn.setOnClickListener(v -> {
                    Log.d("buttonid", tagBtn.getId()+"tag");
                    Log.d("buttonid", oneBtn.getId()+"one");

                    oneContentLoad(rms, tagLst);
                });
            }
        }

    }

    public void allContentLoad(RawMaterials[] rms){
        // 원재료 버튼 생성
        //LinearLayout linearLayoutname = findViewById(R.id.linearLayout3);
        FlexboxLayout linearLayoutname = (FlexboxLayout) findViewById(R.id.linearLayout3);

        List<String> finalname=new ArrayList<String>();
        List<String> finaltag=new ArrayList<String>();
        List<String> finalinfo=new ArrayList<String>();
        for (RawMaterials r : rms) {
            String[] arr = r.getTags().split(" ");
            for (int ii = 0; ii < arr.length; ii++) {
                finalname.add(r.getName());
                finaltag.add(r.getTags());
                finalinfo.add(r.getDescription() + "\n\n출처: " + r.getReference());

                oneBtn = new Button(this);
                oneBtn.setId(r.getId());
                Log.d("id", oneBtn.getId()+"");
                oneBtn.setText(finalname.get(finalname.size()-1));
                oneBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
                layoutManager.setFlexWrap(FlexWrap.WRAP);

                linearLayoutname.addView(oneBtn);
                break;
            }
        }
    }

    public void oneContentLoad(RawMaterials[] rms,String[] tag){
        // 원재료 버튼 생성
        FlexboxLayout linearLayoutname = (FlexboxLayout) findViewById(R.id.linearLayout3);
        linearLayoutname.removeAllViews();

        List<String> finalname=new ArrayList<String>();
        List<String> finaltag=new ArrayList<String>();
        List<String> finalinfo=new ArrayList<String>();
        for (RawMaterials r : rms) {
            String[] arr = r.getTags().split(" ");
            for (int ii = 0; ii < arr.length; ii++) {
                finalname.add(r.getName());
                finaltag.add(r.getTags());
                finalinfo.add(r.getDescription() + "\n\n출처: " + r.getReference());

                oneBtn = new Button(this);
                oneBtn.setId(r.getId());
                Log.d("id", oneBtn.getId()+"");
                oneBtn.setText(finalname.get(finalname.size()-1));
                oneBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);

                if(tag[0].equals("시작")){

                }
                else if(tag[0].equals("전체 원재료")) {
                    oneBtn.setBackgroundColor(Color.BLUE);
                }else {
                    String[] splitarr = r.getTags().split(" ");
                    for (int i = 0; i < splitarr.length; i++) {
                        if (splitarr[i].equals(tag[0])) {
                            oneBtn.setBackgroundColor(Color.BLUE);
                        }
                    }
                }
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