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
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {


    private static String IP_ADDRESS = "54.180.42.62";
    private static String TAG = "phptest";

    EditText IdText;
    EditText PwText;
    EditText NicText;
    EditText NameText;
    TextView mTextViewResult;


    Button backButton;
    Button joinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IdText = (EditText)findViewById(R.id.IdText);
        PwText  = (EditText)findViewById(R.id.PwText);
        NicText  = (EditText)findViewById(R.id.NicText);
        NameText  = (EditText)findViewById(R.id.NameText);

        joinButton=(Button)findViewById(R.id.joinButton);
        backButton=(Button)findViewById(R.id.backButton);

        //mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        joinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 화면전환(Intent)
                String id = IdText.getText().toString();
                String pw = PwText.getText().toString();
                String name = NameText.getText().toString();
                String nick = NicText.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "//insert0.php", id,pw,nick,name);

                IdText.setText("");
                PwText.setText("");
                NameText.setText("");
                NicText.setText("");

                Intent myIntent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(myIntent);

            }
        });

        join();
        back();

    }

    public void join(){

    }
    public void back(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("지금 내용을 지우고 뒤로가시겠습니까?");
                builder.setTitle("뒤로가기 알림창")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent= new Intent(getApplicationContext(),LoginActivity.class);
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


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RegisterActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String pw = (String)params[2];
            String nick = (String)params[3];
            String name = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&pw=" + pw+ "&nick=" + nick+ "&name=" + name;


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
                Log.d(TAG, "POST response code - " + responseStatusCode);

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

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


}


