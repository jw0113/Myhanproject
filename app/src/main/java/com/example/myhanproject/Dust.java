package com.example.myhanproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.concurrent.ExecutionException;

public class Dust extends AppCompatActivity {

    RecyclerView.LayoutManager mrecymanager;
    String[] lis = new String[500];
    String dust1,dust2;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust);

        RecyclerView recy = (RecyclerView)findViewById(R.id.arealist);
        recy.setHasFixedSize(true);
        //LayoutManager에 따라 아이템의 배치 방법 달라짐 -> 일렬로 나열하므로 LinearLayout씀
        mrecymanager = new LinearLayoutManager(this);
        recy.setLayoutManager(mrecymanager);

        //리스트 생성
        final ArrayList<AreaList> areaArraylist = new ArrayList<>();
        try {
            lis = new Dustget().execute().get();
            for(int i=0; i<lis.length;i+=2){
                areaArraylist.add(new AreaList(R.drawable.pin,lis[i],lis[i+1]));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HanAdapter hanAdapter = new HanAdapter(areaArraylist);
        recy.setAdapter(hanAdapter);

        hanAdapter.setItemClick(new HanAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                AreaList dict = areaArraylist.get(position);
                dust2 = dict.areaname;
                try {
                    data = new Dustget2().execute().get();
                    for(int i=0; i<data.length;i+=2){
                        if(lis[i] == dust2){
                            dust1 = data[i];
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //새로운 엑티비티에 값 넘기기
                Intent intent = new Intent(getApplicationContext(),Popup2.class);
                intent.putExtra("areaname",dust2);
                intent.putExtra("dustnum",dust1);
                startActivity(intent);

            }
        });

    }

    public static class AreaList {
        public String areaname;
        public int imgId;
        public String mangname;

        public AreaList(int imgId, String areaname,String mangname){
            this.imgId = imgId;
            this.areaname = areaname;
            this.mangname = mangname;
        }
    }
}
