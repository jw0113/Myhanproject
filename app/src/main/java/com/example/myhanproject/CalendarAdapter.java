package com.example.myhanproject;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter
    {
        private ArrayList<DayInfo> mDayList;
        private Context mContext;
        private int mResource;
        private LayoutInflater mLiInflater;
        private Calendar mcal;

        public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList)
        {
            this.mContext = context;
            this.mDayList = dayList;
            this.mResource = textResource;
            this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount()
        {
            return mDayList.size();
        }

        @Override
        public Object getItem(int position)
        {
            // TODO Auto-generated method stub
            return mDayList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            DayInfo day = mDayList.get(position);
            //int day1 = Integer.parseInt(day.getDay());
            //Date today = new Date();

            DayViewHolde dayViewHolder;

            if(convertView == null)
            {
                convertView = mLiInflater.inflate(mResource, null);

                if(position % 7 == 6)
                {
                    convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
                }
                else
                {
                    convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
                }


                dayViewHolder = new DayViewHolde();

                dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.daybg);
                dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.calendarday);

                convertView.setTag(dayViewHolder);
            }
            else
            {
                dayViewHolder = (DayViewHolde) convertView.getTag();
            }

            if(day != null)
            {
                dayViewHolder.tvDay.setTypeface(null,Typeface.NORMAL);
                dayViewHolder.tvDay.setText(day.getDay());

                if(day.isInMonth())
                {
                    if(position % 7 == 0)
                    {
                        dayViewHolder.tvDay.setTypeface(null,Typeface.NORMAL);
                        dayViewHolder.tvDay.setTextColor(Color.RED);
                    }
                    else if(position % 7 == 6)
                    {
                        dayViewHolder.tvDay.setTypeface(null,Typeface.NORMAL);
                        dayViewHolder.tvDay.setTextColor(Color.BLUE);
                    }
                    else
                    {
                        dayViewHolder.tvDay.setTypeface(null,Typeface.NORMAL);
                        dayViewHolder.tvDay.setTextColor(Color.BLACK);
                    }
                }
                else
                {
                    dayViewHolder.tvDay.setTypeface(null,Typeface.NORMAL);
                    dayViewHolder.tvDay.setTextColor(Color.GRAY);
                }

                //현재 날짜 표시하기기
                mcal = Calendar.getInstance();
                Integer today = mcal.get(Calendar.DAY_OF_MONTH);  //현재 몇일인지 불러오기
                String sToday = String.valueOf(today);

                if(sToday.equals(mDayList.get(position).getDay())){   //mDayList에서 해당 month에 대한 day를 불러오면서 현재 day가 나오면
                                                                        //글씨 크기와 두껍게 한다.
                    dayViewHolder.tvDay.setTextSize(20);
                    dayViewHolder.tvDay.setTypeface(null,Typeface.BOLD);
                }
            }

            return convertView;
        }

        public class DayViewHolde
        {
            public LinearLayout llBackground;
            public TextView tvDay;

        }

        private int getCellWidthDP()
        {
//      int width = mContext.getResources().getDisplayMetrics().widthPixels;
            int cellWidth = 480/3;
            //int cellWidth = 950/7;
            return cellWidth;
        }

        private int getRestCellWidthDP()
        {
//      int width = mContext.getResources().getDisplayMetrics().widthPixels;
            int cellWidth = 480%3;
            //int cellWidth = 950%7;
            return cellWidth;
        }

        private int getCellHeightDP()
        {
//      int height = mContext.getResources().getDisplayMetrics().widthPixels;
            int cellHeight = 480/2;
            //int cellHeight = 1500/5;
            return cellHeight;
        }

    }
