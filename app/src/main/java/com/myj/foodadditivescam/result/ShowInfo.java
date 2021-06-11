package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.RawMaterials;

import java.util.ArrayList;
import java.util.List;

public class ShowInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        String[] tag = getIntent().getStringArrayExtra("tag");
        RawMaterials[] rms = (RawMaterials[]) getIntent().getSerializableExtra("rms");


        List<String> finalname=new ArrayList<String>();
        List<String> finaltag=new ArrayList<String>();
        List<String> finalinfo=new ArrayList<String>();
        // 해당 태그가 있는 원재료만 그 태그를 누르면 보여짐
        for (RawMaterials rm : rms) {
            String[] splitarr = rm.getTags().split(" ");
            for (int i=0; i<splitarr.length;i++) {
                if(splitarr[i].equals(tag[0])) {
                    finalname.add(rm.getName());
                    finaltag.add(rm.getTags());
                    finalinfo.add(rm.getDescription() + "\n\n출처: " + rm.getReference());
                    break;
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter = new RecyclerAdapter(finalname, finaltag, finalinfo);
        recyclerView.setAdapter(adapter);
    }
}