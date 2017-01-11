package com.kportal.android.app.http;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KR8 on 2016-11-30.
 */

public class HttpController {

    private final String TAG = "HttpController";

    public String requestApi(String url, HashMap<String, String> map, String type) {
        String response = "";
        try {
            URL mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)mUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setUseCaches(false);
            conn.setRequestMethod(type);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Auth-Token", "testToken");
            conn.setDoInput(true);
            if(type.equals("DELETE") || type.equals("GET")) {
                conn.setDoOutput(false);
            } else {
                conn.setDoOutput(true);
            }
            if(map != null) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(setRequestData(map).toString().getBytes());
                outputStream.flush();
            }
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                String line = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject setRequestData(HashMap<String, String> map) {
        JSONObject jo = new JSONObject();
        if(map != null) {
            try{
                for(Map.Entry<String, String> entry : map.entrySet()) {
//                    Log.i(TAG, "Key > "+entry.getKey()+" / Value > "+entry.getValue());
                    jo.put(URLEncoder.encode(entry.getKey(), "UTF-8"), URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return jo;
    }
}
