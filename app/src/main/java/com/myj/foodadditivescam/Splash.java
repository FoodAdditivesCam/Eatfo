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

    public class RequestHttpURLConnection {
        public String request(String _url, ContentValues _params) {
            HttpURLConnection urlConn = null;
            // URL 뒤에 붙여서 보낼 파라미터.
            StringBuffer sbParams = new StringBuffer();

            /**
             * 1. StringBuffer에 파라미터 연결
             * */
            // 보낼 데이터가 없으면 파라미터를 비운다.
            if (_params == null)
                sbParams.append("");
                // 보낼 데이터가 있으면 파라미터를 채운다.
            else {
                // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
                boolean isAnd = false;
                // 파라미터 키와 값.
                String key;
                String value;

                for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                    key = parameter.getKey();
                    value = parameter.getValue().toString();

                    // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                    if (isAnd)
                        sbParams.append("&");

                    sbParams.append(key).append("=").append(value);

                    // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                    if (!isAnd)
                        if (_params.size() >= 2)
                            isAnd = true;
                }
            }

            /**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             * */
            try {
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                // [2-1]. urlConn 설정.
                urlConn.setReadTimeout(10000);
                urlConn.setConnectTimeout(15000);
                urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : GET/POST.
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestProperty("Accept-Charset", "utf-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded");

                // [2-2]. parameter 전달 및 데이터 읽어오기.
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(urlConn.getOutputStream()));
                pw.write(sbParams.toString());
                pw.flush(); // 출력 스트림을 flush. 버퍼링 된 모든 출력 바이트를 강제 실행.
                pw.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

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
                return page;

            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
    }

}

