package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.RawMaterials;

import java.util.ArrayList;
import java.util.List;

public class ShowInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info); //activity_show_info);

        String[] tag = getIntent().getStringArrayExtra("tag");
        RawMaterials[] rms = (RawMaterials[]) getIntent().getSerializableExtra("rms");


        List<String> finalname=new ArrayList<String>();
        List<String> finaltag=new ArrayList<String>();
        List<String> finalinfo=new ArrayList<String>();
        if (tag[0].equals("전체 원재료")){
            for (RawMaterials rm : rms) {
                finalname.add(rm.getName());
                finaltag.add(rm.getTags());
                finalinfo.add(rm.getDescription() + "\n\n출처: " + rm.getReference());

//                Button tagBtn = new Button(this);
//                tagBtn.setId(rm.getId());
//                tagBtn.setText(finalname.get(finalname.size()-1));
//                tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                linearLayoutname.addView(tagBtn);
            }
        }else {
            // 해당 태그가 있는 원재료만 그 태그를 누르면 보여짐
            for (RawMaterials rm : rms) {
                String[] splitarr = rm.getTags().split(" ");
                for (int i = 0; i < splitarr.length; i++) {
                    if (splitarr[i].equals(tag[0])) {
                        finalname.add(rm.getName());
                        finaltag.add(rm.getTags());
                        finalinfo.add(rm.getDescription() + "\n\n출처: " + rm.getReference());

//                        Button tagBtn = new Button(this);
//                        tagBtn.setId(rm.getId());
//                        tagBtn.setText(finalname.get(i));
//                        tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                        linearLayoutname.addView(tagBtn);
//                        break;
                    }
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter = new RecyclerAdapter(finalname, finaltag, finalinfo);
        recyclerView.setAdapter(adapter);
    }
}