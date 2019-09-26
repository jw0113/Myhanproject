package com.example.myhanproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.PopupWindow;

public class Popup extends Activity {

    private PopupWindow pwindo;
    private int mWidthPixels, mHeightPixels;
    Button btnClosePopup;
    Button btnmemo;
    Button btnenroll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        //데이터 값 가져오기(사용자 아이디)
        Intent myintent = getIntent();
        final String getId = getIntent().getStringExtra("getid");

        btnClosePopup = (Button) findViewById(R.id.close);
        btnmemo = (Button) findViewById(R.id.memo);
        btnenroll = (Button) findViewById(R.id.enrollproduct);

        btnClosePopup.setOnClickListener(cancel_button_click_listener);

        btnenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(getApplicationContext(),EnrollStuffActivity.class);
                myIntent.putExtra("getid",getId);
                startActivity(myIntent);
            }
        });

        btnmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(getApplicationContext(),Memo.class);
                myIntent.putExtra("getid",getId);
                startActivity(myIntent);
            }
        });

    }
    public View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}
