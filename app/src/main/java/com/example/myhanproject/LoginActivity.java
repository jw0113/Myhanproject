package com.example.myhanproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    EditText idText,passwordText;
    Button loginButton;
    Button joinButton;
    ImageButton exitButton;

    //String getID, getPW;
    String ID, PW;

    Boolean id_text, password_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);


        loginButton=(Button)findViewById(R.id.loginButton);
        joinButton=(Button)findViewById(R.id.joinButton);
        exitButton=(ImageButton)findViewById(R.id.exitButton);

        id_text = false;
        password_text = false;

        MainScreen();
        RegisterScreen();
        exit();

    }

    //로그인 버튼 눌렀을 때
    public void MainScreen(){
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (idText.length() > 0 && passwordText.length() > 0) {
                    ID = idText.getText().toString();
                    PW = passwordText.getText().toString();

                    try {
                        GetData task = new GetData();
                        String a = task.execute(ID,PW).get();
                        String b = getResult(a,ID,PW);
                        System.out.println(ID+","+PW);
                        //System.out.println(b);

                        if(b == "true"){
                            Intent myIntent=new Intent(getApplicationContext(),Main2Activity.class);
                            myIntent.putExtra("getid", ID);
                            startActivity(myIntent);
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("아이디나 비밀번호가 잘못되었습니다.");
                            builder.setTitle("로그인 알림창")
                                    .setCancelable(false)
                                    .setNegativeButton("close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.setTitle("로그인 알림창");
                            alert.show();
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(idText.length()==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("아이디를 입력하세요");
                    builder.setTitle("로그인 알림창")
                            .setCancelable(false)
                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("로그인 알림창");
                    alert.show();
                }

                else if(passwordText.length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("비밀번호를 입력하세요");
                    builder.setTitle("로그인 알림창")
                            .setCancelable(false)
                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("로그인 알림창");
                    alert.show();
                }

            }
        });
    }

    //회원가입
    public void RegisterScreen(){
        joinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 화면전환(Intent)
                Intent myIntent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(myIntent);

            }
        });
    }
    public void exit(){
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("정말로 종료하시겠습니까?");
                builder.setTitle("종료 알림창")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("종료 알림창");
                alert.show();
            }
        });
    }

    //DB불러오기
    private class GetData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            String getid = params[0];
            String getpw = params[1];
            String testurl = "http://54.180.42.62//dbdata.php";
            String postParameters = "id = " + getid + "pw = " + getpw;
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
                    sb.append(line);       //한줄씩 읽어와서 builder에 넣어둠
                }
                bufferedReader.close();
                re =  sb.toString();
                //System.out.println(re);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return re;   //여기 출력값은 트렐로에 올려둠
            //여기 출력값을 getResult에 넘겨주고 아이디랑 비밀번호 비교한다.
        }

    }

    //php파일의 배열형식을 전부 불러와서 태그에 해당하는 값을 배열에 넣는다 -> 입력받는 아이디랑 비밀번호 비교
    public String getResult(String jw, String id, String pw){
        //id,pw는 사용자가 입력한 값임
        String[] dbid = new String[100];
        String[] dbpw = new String[100];
        String result = "false";
        //String result1 = "false";

        try{

            JSONObject jobject = new JSONObject(jw);
            JSONArray jsonArray = jobject.getJSONArray("webnautes");
            for(int i=0; i<jsonArray.length(); i+=2){
                JSONObject array = jsonArray.getJSONObject(i);
                dbid[i] = array.getString("id");  //모든 아이디 넣은 배열
                dbpw[i] = array.getString("비번");  //모든 비밀번호 넣은 배열


                if(dbid[i].equals(id) && dbpw[i].equals(pw)){
                    result = "true";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}