package com.example.myhanproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Memo extends AppCompatActivity {

    private static String IP_ADDRESS = "54.180.42.62";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Button close = (Button) findViewById(R.id.close1);
        Button en = (Button) findViewById(R.id.mm1);
        final EditText memodate = (EditText) findViewById(R.id.memodate);
        final EditText mm = (EditText) findViewById(R.id.mm);

        //데이터 값 가져오기(사용자 아이디)
        Intent myintent = getIntent();
        final String getId = myintent.getStringExtra("getid");

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String memodate1 = memodate.getText().toString();
                String mm1 = mm.getText().toString();


                GetData my = new GetData();
                my.execute("http://" + IP_ADDRESS + "//insertmemo.php", getId,memodate1,mm1);

                memodate.setText("");
                mm.setText("");

                Intent myIntent=new Intent(getApplicationContext(), Calender.class);
                myIntent.putExtra("getid",getId);
                startActivity(myIntent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Memo.this);
                builder.setMessage("지금 내용을 지우고 뒤로가시겠습니까?");
                builder.setTitle("뒤로가기 알림창")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent= new Intent(getApplicationContext(),Calender.class);
                                startActivity(myIntent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("뒤로가기 알림창");
                alert.show();
            }
        });

    }

    private class GetData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Memo.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // mTextViewResult.setText(result);
            //Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String mdate = (String)params[2];
            String m = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&date=" + mdate + "&memo=" + m;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
               // Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {

                //Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }
}
