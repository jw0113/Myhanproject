package com.example.myhanproject;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Weatherget extends AsyncTask<String, Void, String> {

    String key = "4619479d76a27b8c72691cb1425a7c0c";
    String city = "Seoul";
    String res,buffermsg;


    @Override
    protected String doInBackground(String... datas){
        URL url = null;
        try{
            //httpconnection 객체를 사용해 연결
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", key);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){   //응답코드가 OK로 승인 되었을 경우
                InputStream is = conn.getInputStream();  //연결 되었으면 값을 가져온다.
                InputStreamReader reader = new InputStreamReader(is,"UTF-8");  //가져온 값을 읽음
                BufferedReader in = new BufferedReader(reader);   //버퍼에 저장 -> 중간 버퍼링을 통해 중간 장치를 거친 후 전송
                StringBuffer buffer = new StringBuffer();

                while((res = in.readLine()) != null){
                    buffer.append(res);
                }
                buffermsg = buffer.toString();
                reader.close();
            }else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listjsonParser(buffermsg);
    }


    public String listjsonParser(String json){
        //String description = null;
        String icon = null;
        //String name = null;

        try{
            JSONObject jobject = new JSONObject(json);
            //description = jobject.getJSONArray("weather").getJSONObject(0).getString("description");
            icon = jobject.getJSONArray("weather").getJSONObject(0).getString("icon");
            //name = jobject.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return icon;
    }
}
