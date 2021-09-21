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
            System.out.println(input);
            String str = input.toString();
            System.out.println(str);
            byte[] postDataBytes = str.toString().getBytes("UTF-8");

            URL url = new URL("http://3.35.255.25:80/" + address); // 3.35.255.25:80
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input2 = str.getBytes("utf-8");
                os.write(input2, 0, input2.length);
            }

            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d("http_test", "getResponseFail");
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            String result = "";
            while((line = reader.readLine()) != null){
                result += line;
            }
            System.out.println(result.toString());
            json = new JSONObject(result);

            //conn.getOutputStream().flush();
            conn.getOutputStream().close();
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