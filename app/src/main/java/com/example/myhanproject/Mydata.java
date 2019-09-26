package com.example.myhanproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Mydata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);

        //데이터 값 가져오기(사용자 아이디)
        Intent myintent = getIntent();
        String getId = getIntent().getStringExtra("getid");
        //System.out.println(getId);

        TextView name = (TextView)findViewById(R.id.name);
        TextView nic = (TextView)findViewById(R.id.nic);
        TextView id = (TextView)findViewById(R.id.id);
        TextView date = (TextView)findViewById(R.id.enroodate);
        TextView count = (TextView)findViewById(R.id.count);

        try{
            GetData my = new GetData();
            String a = my.execute(getId).get();

            //db정보 보이기
            String dbname = null;
            String dbnic = null;
            String dbid = null;
            String dbdate = null;
            String dbcount = null;


            try{
                JSONObject jobject = new JSONObject(a);
                JSONArray jsonArray = jobject.getJSONArray("webnautes");
                for(int i=0; i<jsonArray.length(); i+=2){
                    JSONObject array = jsonArray.getJSONObject(i);
                    dbname = array.getString("이름");
                    dbnic = array.getString("닉네임");
                    dbid = array.getString("id");
                    dbdate = array.getString("가입날짜");
                    dbcount = array.getString("갯수");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            id.setText(dbid);
            name.setText(dbname);
            nic.setText(dbnic);
            date.setText(dbdate);
            count.setText(dbcount);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    //DB불러오기
    private class GetData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            String getid = params[0];
            String testurl = "http://54.180.42.62//mydata1.php?id="+getid;
            String postParameters = "id=" + getid;
            String re = null;

            try{
                URL url = new URL(testurl);
                HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();

                httpurlconnection.setReadTimeout(5000);
                httpurlconnection.setConnectTimeout(5000);
                httpurlconnection.setRequestMethod("POST");
                httpurlconnection.setDoInput(true);
                httpurlconnection.connect();

                OutputStream outputStream = httpurlconnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

//                OutputStreamWriter outStream = new OutputStreamWriter(httpurlconnection.getOutputStream(),"UTF-8");
//                PrintWriter writer = new PrintWriter(outStream);
//                writer.write(getid);
//                writer.flush();

                int responseStatusCode = httpurlconnection.getResponseCode();
                //Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpurlconnection.getInputStream();
                }
                else{
                    inputStream = httpurlconnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                re =  sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return re;
        }

    }
}
