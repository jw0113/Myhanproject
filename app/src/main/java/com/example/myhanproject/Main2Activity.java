package com.example.myhanproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //데이터 값 가져오기(사용자 아이디)
        Intent myintent = getIntent();
        final String getId = myintent.getExtras().getString("getid");
        getResult(getId);


        ImageButton weather = (ImageButton)findViewById(R.id.sun);
        ImageButton calender = (ImageButton)findViewById(R.id.calender);
        ImageButton dust = (ImageButton)findViewById(R.id.dust);


        //날씨 아이콘 누를때
        weather.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent=new Intent(getApplicationContext(),Weather.class);
                startActivity(myIntent);

            }
        });

        //캘린더 아이콘 누를때
        calender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent=new Intent(getApplicationContext(),Calender.class);
                myIntent.putExtra("getid",getId);
                startActivity(myIntent);

            }
        });

        //미세먼지 아이콘 누를때
        dust.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent=new Intent(getApplicationContext(),Dust.class);
                startActivity(myIntent);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void getResult(String a){
        this.b = a;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.enroll) {
            Intent myIntent=new Intent(getApplicationContext(),EnrollStuffActivity.class);
            myIntent.putExtra("getid",b);
            startActivity(myIntent);

        } else if (id == R.id.show) {
            Intent myIntent=new Intent(getApplicationContext(),ListActivity.class);
            myIntent.putExtra("getid",b);
            startActivity(myIntent);

        } else if (id == R.id.mydata) {
            Intent myIntent=new Intent(getApplicationContext(),Mydata.class);
            myIntent.putExtra("getid",b);
            startActivity(myIntent);

        } else if (id == R.id.del) {

        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
