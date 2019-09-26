package com.example.myhanproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class Popup2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup2);

        TextView text1 = (TextView)findViewById(R.id.area_name);
        TextView text2 = (TextView)findViewById(R.id.dustnum);
        ImageView img = (ImageView)findViewById(R.id.dustimg);

        //데이터 값 가져오기
        Intent intent = getIntent();

        String areaname = intent.getExtras().getString("areaname");
        String dustnum = intent.getExtras().getString("dustnum");
        String result = "미세먼지 농도는"+dustnum+"㎍/m³입니다.";

        //textview에 보이기
        text1.setText(areaname);
        text2.setText(result);

        int dustnum1 = Integer.parseInt(dustnum);
        if(dustnum1>=0 && dustnum1 <= 30){
            img.setImageResource(R.drawable.fine);
        }
        else if(dustnum1 >= 31 && dustnum1 <= 80){
            img.setImageResource(R.drawable.soso);
        }
        else if(dustnum1 >= 81 && dustnum1 <= 100){
            img.setImageResource(R.drawable.bad);
        }
        else if(dustnum1 >= 101 && dustnum1 <= 151){
            img.setImageResource(R.drawable.sobad);
        }

    }
}
