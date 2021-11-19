package com.myj.foodadditivescam.result;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myj.foodadditivescam.R;
import com.myj.foodadditivescam.RawMaterials;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class ShowInfo extends AppCompatActivity {
    private TextView nameText;
    private TextView tagText;
    private TextView infoText;
    private ImageButton backBtn2;
    public JSONObject jsonResult;
    private String name="";
    private String tag="";
    private String info="";

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            nameText= (TextView)findViewById(R.id.mNameTxt);
            tagText= (TextView)findViewById(R.id.mTagTxt);
            infoText= (TextView)findViewById(R.id.mDescTxt);

            nameText.setText(name);
            tagText.setText(tag);
            infoText.setText(info);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info); //activity_show_info);

        SharedPreferences pref = getSharedPreferences("isSearch", Activity.MODE_PRIVATE);
        boolean isSearch = pref.getBoolean("isSearch", false);


        if (isSearch == true) { // 검색 화면에서 넘어온 경우
            System.out.println("isSearch");

            // AsyncTask를 통해 HttpURLConnection 수행.(비동기 방식)
            String url = "http://3.35.255.25:80/" + getIntent().getStringExtra("word");
            System.out.println(url);

            NetworkTask2 networkTask = new NetworkTask2(url, null);
            networkTask.execute();

            // 다시 false로 되돌려줌
            SharedPreferences prefs = getSharedPreferences("isSearch", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isSearch", false);
            editor.apply();

        } else { // 결과화면에서 넘어온 경우
            System.out.println("not Search");

            name = getIntent().getStringExtra("name");
            tag = getIntent().getStringExtra("tag");
            info = getIntent().getStringExtra("info");

            nameText= (TextView)findViewById(R.id.mNameTxt);
            tagText= (TextView)findViewById(R.id.mTagTxt);
            infoText= (TextView)findViewById(R.id.mDescTxt);

            nameText.setText(name);
            tagText.setText(tag);
            infoText.setText(info);
        }
        
        backBtn2 = findViewById(R.id.backBtn2);
        backBtn2.setOnClickListener(view->{
            finish();
        });
    }

    public class NetworkTask2 extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        public NetworkTask2(String url, ContentValues values) {
            this.url = url; this.values = values;
        }

        @Override protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection2 requestHttpURLConnection = new RequestHttpURLConnection2();
            result = requestHttpURLConnection.request(url, values);  // 해당 URL로 부터 결과물을 얻어온다.
            System.out.println(result);

            return result;
        }

        @Override protected void onPostExecute(String s) {
            super.onPostExecute(s); //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            // tv_outPut.setText(s);
        }
    }

    public class RequestHttpURLConnection2 {
        public String request(String _url, ContentValues _params) {
            HttpURLConnection urlConn = null;

            /**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             * */
            try {
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                // [2-1]. urlConn 설정.
                urlConn.setReadTimeout(10000);
                urlConn.setConnectTimeout(15000);
                urlConn.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : GET/POST.
                //urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestProperty("Accept-Charset", "utf-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded");


                // [2-3]. 연결 요청 확인.
                // 실패 시 null을 리턴하고 메서드를 종료.
                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return null;

                // [2-4]. 읽어온 결과물 리턴.
                // 요청한 URL의 출력물을 BufferedReader로 받는다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                // 출력물의 라인과 그 합에 대한 변수.
                String line;
                String page = "";

                // 라인을 받아와 합친다.
                while ((line = reader.readLine()) != null) {
                    page += line;
                }

                // JSONObjct로 받아와 name, tag, info 파싱해서 화면에 보여줌
                JSONObject jsonObj = new JSONObject(page);
                JSONObject jsonObject = new JSONObject(jsonObj.getString("result").replace("[", "").replace("]", ""));
                System.out.println(jsonObject);
                name = jsonObject.getString("name");

                String tagString = "";
                System.out.println(jsonObject.getString("tag1"));

                for(int i = 1; i <= 5; i++) {
                    if (!jsonObject.isNull("tag" + i)) {
                        tagString += jsonObject.getString("tag" + i) + ", ";
                        System.out.println(tagString);
                    }
                }
                tag = tagString;
                try{
                    tag = tag.substring(0, tag.length()-2); // 마지막 컴마 제거
                }catch(Exception e){
                    tag = "";
                }
                info = jsonObject.getString("description");

                backBtn2 = findViewById(R.id.backBtn2);

                Message msg = handler.obtainMessage();
                handler.handleMessage(msg);

                return page;

            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
    }

}



