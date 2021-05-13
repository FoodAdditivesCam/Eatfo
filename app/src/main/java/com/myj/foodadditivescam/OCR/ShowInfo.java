package com.myj.foodadditivescam.OCR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.myj.foodadditivescam.R;

public class ShowInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        TextView imageDetail = (TextView) findViewById(R.id.image_details);
        imageDetail.setVisibility(View.GONE);

        String name = getIntent().getStringExtra("itemName");
        String tag = getIntent().getStringExtra("tag");
        String info = getIntent().getStringExtra("info");
        Log.d(ShowResult.class.getSimpleName(), "인텐트 가져옴: "+name);
        String[] nameLst = {name};
        String[] tagLst = {tag};
        String[] infoLst= {info};
        RecyclerAdapter adapter = new RecyclerAdapter(nameLst, tagLst, infoLst);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}