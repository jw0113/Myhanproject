package com.example.myhanproject;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick{
        public void onClick(View view, int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick){
        this.itemClick = itemClick;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView text;
        TextView text1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            text = itemView.findViewById(R.id.textviewlist);
            text1 = itemView.findViewById(R.id.textviewlist1);
        }
    }

    private ArrayList<Dust.AreaList> areaArraylist;

    HanAdapter(ArrayList<Dust.AreaList> areaArraylist){
        this.areaArraylist = areaArraylist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int Position = i;
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.img.setImageResource(areaArraylist.get(i).imgId);
        myViewHolder.text.setText(areaArraylist.get(i).areaname);
        myViewHolder.text1.setText(areaArraylist.get(i).mangname);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClick != null){
                    itemClick.onClick(view,Position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return areaArraylist.size();
    }

}
