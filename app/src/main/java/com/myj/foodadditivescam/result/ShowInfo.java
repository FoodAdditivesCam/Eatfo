package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.myj.foodadditivescam.R;

public class ShowInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        String[] name = getIntent().getStringArrayExtra("itemName");
        String[] tag = getIntent().getStringArrayExtra("tag");
        String[] info = getIntent().getStringArrayExtra("info");

        Log.d(ShowResult.class.getSimpleName(), "인텐트 가져옴: "+name.length + "개");

        RecyclerAdapter adapter = new RecyclerAdapter(name, tag, info);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}