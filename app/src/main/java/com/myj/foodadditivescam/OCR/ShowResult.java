package com.myj.foodadditivescam.OCR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.search.SearchAPI;
import com.myj.foodadditivescam.search.Symspell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ShowResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        String data = getIntent().getStringExtra("data");
        String[] res = data.split("\n");
//        String[] tmp = data.split("title : ");
//
//        Log.d(ShowResult.class.getSimpleName(), "인텐트 가져옴 "+tmp.length+"개");
//
//        String[][] res = new String[3][tmp.length];
//        for (int i=1; i<=tmp.length; i++){
//            res[0][i-1] = tmp[i].split("link : ")[0];
//            res[1][i-1] = tmp[i].split("link : ")[1].split("description : ")[0];
//            res[2][i-1] = tmp[i].split("link : ")[1].split("description : ")[1].replace("\n", "");
//        }

        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        for(int i = 2; i<res.length; i++){
            Button tagBtn = new Button(this);
            tagBtn.setId(i);
            tagBtn.setText(res[i]);
            tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(tagBtn);

            String[] nameLst= {res[i], res[i], res[i], res[i], res[i]};
            String[] tagLst= {"태그", "태그, 태그", "태그, 태그, 태그", "태그, 태그, 태그, 태그", "태그, 태그, 태그, 태그, 태그"};
            String[] infoLst= {res[i]+"입니다.", res[i]+"입니다.", res[i]+"입니다.", res[i]+"입니다.", res[i]+"입니다."};

            tagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ShowInfo.class);
                    intent.putExtra("itemName", nameLst);
                    intent.putExtra("tag", tagLst);
                    intent.putExtra("info", infoLst);
                    startActivity(intent);
                }
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