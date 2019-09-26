package com.example.myhanproject;

import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Dustget2 extends AsyncTask<String, Void, String[]> {

    String areaname;
    static String key = "vXtdSx%2FMT19g8Wz7vhgfIEgQnOtT6B11jBkt1Fw0UrAeq3bLs7s2Y9%2BztKIuHY1biONWP7vQaax%2BPRz7s4UX3Q%3D%3D";
    boolean pm10 = false;
    String pm_10 = null;
    int countt = 0;

    @Override
    protected String[] doInBackground(String... strings) {
        String[] lii = new String[500];

        try{
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey="+key+"&numOfRows=500&pageNo=1&sidoName=서울&ver=1.3");
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            parser.setInput(url.openStream(), "UTF-8");

            int parserEvent = parser.getEventType();
            while(parserEvent != XmlPullParser.END_DOCUMENT){
                switch (parserEvent){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("item"));
                        else if(parser.getName().equals("pm10Value24")){
                            pm10 = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(pm10){
                            pm_10 = parser.getText();
                            lii[countt] = pm_10;
                            countt+=2;
                            pm10 = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item"));
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lii;
    }
}
