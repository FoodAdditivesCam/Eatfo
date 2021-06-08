package com.myj.foodadditivescam.OCR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.RawMaterials;
import com.myj.foodadditivescam.search.SearchAPI;
import com.myj.foodadditivescam.search.Symspell;
import com.myj.foodadditivescam.wordCloud;

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

        RawMaterials[] rms = (RawMaterials[]) getIntent().getSerializableExtra("rms");

        //wordCloud wcd = new wordCloud();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //단어 리스트 주고 워드클라우드 그려서 imageView 수정해주기
        //imageView.setImageBitmap(wcd.createWordCloud(res));
        LinearLayout linearLayout = findViewById(R.id.linearLayout2);

        for (RawMaterials rm : rms) {
            Button tagBtn = new Button(this);
            tagBtn.setId(rm.getId());
            tagBtn.setText(rm.getName());
            tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(tagBtn);

            String[] name = {rm.getName()};
            String[] tagLst = {rm.getTags()};
            String[] info = {rm.getDescription() + "\n\n출처: " + rm.getReference()};

            tagBtn.setOnClickListener(v -> {
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