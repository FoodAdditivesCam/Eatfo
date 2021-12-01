package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowResult extends AppCompatActivity {
    private Button oneBtn;
    private Button tagBtn;
    private int DYNAMIC_TAG_ID = 1000;
    private Map<String, Integer> tag_weight = new HashMap<String, Integer>();
    Set<String> checked;   //사용자가 체크한 버튼의 텍스트를 저장할 리스트
    int taglength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //원재료 정보 객체 리스트, 이미지 url 가져오기
        RawMaterials[] rms = (RawMaterials[]) getIntent().getSerializableExtra("rms");
        String url = (String) getIntent().getSerializableExtra("url");
        //저장된 데이터 불러오기
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        pref.getStringSet("checked", checked);
        checked = pref.getStringSet("checked", new HashSet<String>());

        //태그 리스트 만들기
        String tags="";
        Map<String, Integer> tag_weight_temp = new HashMap<String, Integer>();
        try{
            setContentView(R.layout.activity_show_result);
            for(RawMaterials rm : rms){
                tags+=rm.getTags()+" ";
                String[] splitarr = rm.getTags().split(", ");
                for (int i=0; i<splitarr.length;i++){
                    if(splitarr[i]=="" || splitarr[i]==null) continue;
                    tag_weight_temp.put(splitarr[i],0);
                    taglength++;
                }
            }

            // 태그 버튼
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            String startag = "시작";
            oneContentLoad(rms, startag,0,0);
            // 가중치 별로 정렬
            for (String key: tag_weight_temp.keySet()) {
                for (Iterator<String> it = checked.iterator(); it.hasNext(); ) {
                    String f = it.next();
                    if (key.contains(f)) { // todo: matching 리스트 필요
                        tag_weight_temp.put(key,tag_weight_temp.get(key)+2);
                        continue;
                    }
                }
            }
            // sorting tag_weight(태그 가중치)
            tag_weight = tag_weight_temp.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            // 버튼 만들기
            int id=0;
            for (String key: tag_weight.keySet()) {
                tagBtn = new Button(this);
                tagBtn.setText(key);
                tagBtn.setId(DYNAMIC_TAG_ID+id);
                tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
                tagBtn.setBackground(getDrawable(R.drawable.tag_button_design));
                tagBtn.setElevation(20);
                if(tag_weight.get(key)>0) { //tag_temp
                    tagBtn.setBackground(getDrawable(R.drawable.tag_button_design_person)); // todo: 색 다시 정해서 하드코딩 고치기
                    tagBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tag_icon, 0,0,0); // todo: 아이콘 바꾸기, 하드코딩
                }
                linearLayout.addView(tagBtn);

                //버튼 클릭 이벤트
                int finalTaglength = taglength;
                tagBtn.setOnClickListener(v -> {
                    oneContentLoad(rms, key, v.getId(), finalTaglength);
                });
                id++;
            }
        }catch (java.lang.NullPointerException e){
            setContentView(R.layout.fail_to_get_result);
        }
    }

    //태그 버튼 클릭 이벤트(원재료 버튼 만들기)
    public void oneContentLoad(RawMaterials[] rms,String tag, int clickid, int taglength){
        FlexboxLayout linearLayoutname = (FlexboxLayout) findViewById(R.id.linearLayout3);
        linearLayoutname.removeAllViews();

        if(taglength!=0) { //태그 버튼 setting
            Button tmpBtn;
            int id=0;
            for (String key: tag_weight.keySet()) {
                tmpBtn = findViewById(DYNAMIC_TAG_ID + id);
                tmpBtn.setBackgroundResource(R.drawable.tag_button_design);
                tmpBtn.setTextColor(Color.BLACK);
                if(tag_weight.get(key)>0) {
                    tmpBtn.setBackground(getDrawable(R.drawable.tag_button_design_person)); // todo: 색 다시 정해서 하드코딩 고치기
                    tmpBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tag_icon, 0,0,0); // todo: 아이콘 바꾸기
                }
                id++;
            }
            tmpBtn = findViewById(clickid);
            tmpBtn.setBackgroundResource(R.drawable.button_design);
            tmpBtn.setTextColor(Color.WHITE);
        }

        //원재료명 버튼 생성
        for (RawMaterials r : rms) {
            oneBtn = new Button(this);
            oneBtn.setId(r.getId());
            oneBtn.setText(r.getName());
            oneBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            oneBtn.setBackgroundResource(R.drawable.button_design_white);
            oneBtn.setTextColor(getResources().getColor(R.color.colorPrimary));

            if(!tag.equals("시작")){ // 해당 원재료명 표시
                String[] splitarr = r.getTags().split(", ");
                for (int i = 0; i < splitarr.length; i++) {
                    if (splitarr[i].equals(tag)) {
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