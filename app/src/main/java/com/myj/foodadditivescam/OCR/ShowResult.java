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

public class ShowResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        String[] res = getIntent().getStringArrayExtra("itemName");
        Log.d(ShowResult.class.getSimpleName(), "인텐트 가져옴 "+res.length+"개");

        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        for(int i = 0; i<res.length; i++){
            Button tagBtn = new Button(this);
            tagBtn.setId(i);
            tagBtn.setText(res[i]);
            tagBtn.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(tagBtn);
            tagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ShowInfo.class);
                    intent.putExtra("itemName", tagBtn.getText());
                    intent.putExtra("tag", "태그"+tagBtn.getId());
                    intent.putExtra("info", tagBtn.getText()+"입니다.");
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