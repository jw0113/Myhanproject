package com.example.myhanproject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Calender extends Activity implements OnItemClickListener, OnClickListener
{
    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;
    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;
    private PopupWindow pwindo;
    private int mWidthPixels, mHeightPixels;

    String b,jiwoo;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    Button btnClosePopup;
    Button btnmemo;
    Button btnenroll;

    ImageButton btnadd;

    TextView memo;
    TextView product;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        //데이터 값 가져오기(사용자 아이디)
        Intent myintent = getIntent();
        String getId = myintent.getExtras().getString("getid");
        getResult(getId);

        ImageButton bLastMonth = (ImageButton)findViewById(R.id.leftbtn);
        ImageButton bNextMonth = (ImageButton)findViewById(R.id.rightbtn);
        memo = (TextView)findViewById(R.id.dbmemo);
        product = (TextView)findViewById(R.id.dbproduct);

        mTvCalendarTitle = (TextView)findViewById(R.id.data);
        mGvCalendar = (GridView)findViewById(R.id.gridview);

        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics matrics = new DisplayMetrics();
        d.getMetrics(matrics);
        mWidthPixels = matrics.widthPixels;
        mHeightPixels = matrics.heightPixels;

        bLastMonth.setOnClickListener(this);
        bNextMonth.setOnClickListener(this);
        mGvCalendar.setOnItemClickListener(this);

        mDayList = new ArrayList<DayInfo>();
    }

    private void getResult(String getid) {
        this.b = getid;
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    /**
     * 달력을 셋팅한다.
     *
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar(Calendar calendar)
    {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        if(dayOfMonth == SUNDAY)
        {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 " + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        Log.e("DayOfMOnth", dayOfMonth+"");

        for(int i=0; i<dayOfMonth-1; i++)
        {
            int date = lastMonthStartDay+i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);

            mDayList.add(day);
        }
        for(int i=1; i <= thisMonthLastDay; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }
        for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);
        }

        initCalendarAdapter();
    }

    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    //int selectedItem = -1;
    //날짜별 눌렀을때 이벤트 처리
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
    {
        showDB(Integer.parseInt(mDayList.get(position).getDay()),mThisMonthCalendar.get(Calendar.MONTH)+1,mThisMonthCalendar.get(Calendar.YEAR));
//        if(position == selectedItem){
//            v.setBackgroundColor(getColor(R.color.coloryellow));
//        }
//        else{
//            v.setBackgroundColor(getColor(R.color.lineColor));
//        }
    }


    public void btnClick(View view){
        if(view.getId() == R.id.plus){
            //System.out.println(b);
            Intent myIntent=new Intent(getApplicationContext(),Popup.class);
            myIntent.putExtra("getid",b);
            startActivity(myIntent);
        }
    }

    //DB내용 보이기
    private void showDB(int day, int mon, int year) {

        String day1 = String.valueOf(day);
        String mon1 = String.valueOf(mon);
        String datee = null;

        if(day < 10){
            day1 = "0"+day;
        }

        if(mon < 10){
            mon1 = "0"+mon;
        }

        datee = year + "-" + mon1 + "-" + day1;

        //b는 사용자 아이디
        try{
            GetData my = new GetData();
            GetData1 my1 = new GetData1();
            String a = my.execute(b).get();
            String a1  = my1.execute(b).get();
            //System.out.println(b);

            //db정보 보이기
            String dbmeno = null;
            String memodate[] = new String[500];
            String dbproduct = null;
            String proddate[] = new String[500];


            try{
                JSONObject jobject = new JSONObject(a);
                JSONObject jobject1 = new JSONObject(a1);
                //System.out.println(jobject1);

                JSONArray jsonArray = jobject.getJSONArray("webnautes");
                JSONArray jsonArray1 = jobject1.getJSONArray("webnautes");

                //memo 가져오기
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject array = jsonArray.getJSONObject(i);
                    //dbmeno = array.getString("Memo");
                    memodate[i] = array.getString("date");

                    if(memodate[i].equals(datee)){
                        dbmeno = array.getString("memo");
                    }
                }

                //물건 list 가져오기
                for(int i=0; i<jsonArray1.length(); i++){
                    JSONObject array1 = jsonArray1.getJSONObject(i);
                    //dbmeno = array.getString("Memo");
                    proddate[i] = array1.getString("date");

                    if(proddate[i].equals(datee)){
                        dbproduct = array1.getString("등록물건");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            memo.setText(dbmeno);
            product.setText(dbproduct);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    //memeo DB연동하기기
    private class GetData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String getid = params[0];
            String testurl = "http://54.180.42.62//memoopen.php?id="+getid;
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

    //물건db가져오기
    private class GetData1 extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String getid = params[0];
            String testurl = "http://54.180.42.62//prod.php?id="+getid;
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


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.leftbtn:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.rightbtn:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter()
    {
        mCalendarAdapter = new CalendarAdapter(this, R.layout.calendarday, mDayList);
        mGvCalendar.setAdapter((ListAdapter) mCalendarAdapter);
    }
}
