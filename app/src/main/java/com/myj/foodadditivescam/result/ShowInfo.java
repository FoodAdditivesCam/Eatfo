package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.myj.foodadditivescam.R;

public class ShowInfo extends AppCompatActivity {
    private TextView nameText;
    private TextView tagText;
    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info); //activity_show_info);

        String name = getIntent().getStringExtra("name");
        String tag = getIntent().getStringExtra("tag");
        String info = getIntent().getStringExtra("info");

        nameText= (TextView)findViewById(R.id.mNameTxt);
        tagText= (TextView)findViewById(R.id.mTagTxt);
        infoText= (TextView)findViewById(R.id.mDescTxt);

        nameText.setText(name);
//        String tagstr="";
//        for(int i=0;i<tag.length;i++){
//            tagstr+=(tag[i]+" ");
//        }
        tagText.setText(tag);
        infoText.setText(info);
    }
}