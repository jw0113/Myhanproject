package com.example.myhanproject;

import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Dustget extends AsyncTask<String, Void, String[]> {

    static String key = "vXtdSx%2FMT19g8Wz7vhgfIEgQnOtT6B11jBkt1Fw0UrAeq3bLs7s2Y9%2BztKIuHY1biONWP7vQaax%2BPRz7s4UX3Q%3D%3D";
    static boolean stationname = false, mangname = false;
    static String station_name = null, mang_name = null;


    @Override
    protected String[] doInBackground(String... strings) {
        String[] li = new String[500];
        int count = 0;

        try{
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey="+key+"&numOfRows=500&pageNo=1&sidoName=서울&ver=1.3");
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            parser.setInput(url.openStream(), "UTF-8");

            int parserEvent = parser.getEventType();
            while(parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("item"));
                        else if(parser.getName().equals("stationName")){
                            stationname = true;
                        }
                        else if(parser.getName().equals("mangName")){
                            mangname = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(stationname){
                            station_name = parser.getText();
                            li[count] = station_name;
                            count++;
                            stationname = false;
                        }
                        if(mangname){
                            mang_name = parser.getText();
                            li[count] = mang_name;
                            count++;
                            mangname = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item"));
                        break;
                }
                parserEvent = parser.next();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return li;
    }
}
