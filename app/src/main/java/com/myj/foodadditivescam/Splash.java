package com.myj.foodadditivescam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.myj.foodadditivescam.OCR.ImageLoadActivity;
import com.myj.foodadditivescam.userData.getUserData;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {
    public static String Tag = Splash.class.getSimpleName();
    public String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
            boolean first = pref.getBoolean("isFirst", false);

            String url = "http://3.35.255.25/searchArray";
            // AsyncTask를 통해 HttpURLConnection 수행.
            NetworkTask networkTask = new NetworkTask(url, null);
            networkTask.execute();

            if(!first){ //최초실행일 경우
                Log.d(Tag, "first");
                //getUserData 액티비티로 이동
                intent = new Intent(this, getUserData.class);
            }else{
                //최초실행이 아니면 ImageLoadActivity 실행
                Log.d(Tag, "not first");
                intent = new Intent(this, ImageLoadActivity.class);
            }

            startActivity(intent);
            finish();
        }, 3000);
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        public NetworkTask(String url, ContentValues values) {
            this.url = url; this.values = values;
        }

        @Override protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);  // 해당 URL로 부터 결과물을 얻어온다.
            res = result;
            return result;
        }

        @Override protected void onPostExecute(String s) {
            super.onPostExecute(s); //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            // tv_outPut.setText(s);
        }
    }

}

