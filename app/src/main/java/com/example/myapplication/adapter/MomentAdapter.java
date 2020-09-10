package com.example.myapplication.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Inf.Moment;

import com.example.myapplication.R;

import java.util.ArrayList;

public class MomentAdapter extends BaseAdapter {

    private ArrayList<Moment> moments ;
    private Context context;
    private LayoutInflater layoutInflater;
    public MomentAdapter(ArrayList<Moment> moments, Context context){
        this.moments = moments;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return moments.size();
    }

    @Override
    public Object getItem(int i) {
        return moments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        view = layoutInflater.inflate(R.layout.activity_moment,null);
        String userName = moments.get(i).getUserName();
        String content = moments.get(i).getContent();
        String time = moments.get(i).getTime();
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.userName = (TextView) view.findViewById(R.id.userName);
        holder.time = (TextView) view.findViewById(R.id.time);
        holder.like = (ImageButton) view.findViewById(R.id.like);
        holder.comment = (ImageButton) view.findViewById(R.id.comment);
        holder.content.setText(content);
        holder.userName.setText(userName);
        holder.time.setText(time);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(moments.get(i).getLike()){
                    moments.get(i).setLike(false);
                    holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.like0));
                }else {
                    moments.get(i).setLike(true);
                    holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.like1));
                }
            }
        });

        if(moments.get(i).getLike()){
            holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.like1));
        }else{
            holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.like0));
        }

        return view;
    }

    private class Holder {
        public TextView content;
        public TextView userName;
        public TextView time;
        public ImageButton like;
        public ImageButton comment;

    }
}

