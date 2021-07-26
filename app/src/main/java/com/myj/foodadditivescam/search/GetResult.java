package com.myj.foodadditivescam.search;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
public class GetResult {
    public static JSONObject POST(JSONObject input, String address) {
        JSONObject json = null;
        try{
            URL url = new URL("http://3.35.255.25:80/" + address); // 3.35.255.25:80
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept-Charset", "utf-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Content-Type", "application/json");
            System.out.println(input);
            String str = input.toString();
            System.out.println(str);
            OutputStream outputStream = urlConn.getOutputStream();
            outputStream.write(str.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d("http_test", "getResponseFail");
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            String line;
            String result = "";
            while((line = reader.readLine()) != null){
                result += line;
            }
            System.out.println(result.toString());
            json = new JSONObject(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}