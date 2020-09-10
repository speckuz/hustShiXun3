package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Inf.ChatRecord;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<ChatRecord> chatRecords ;
    private LayoutInflater layoutInflater;
    public ChatAdapter(ArrayList<ChatRecord> chatRecords, Context context){
        this.chatRecords = chatRecords;
        this.layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return chatRecords.size();
    }

    @Override
    public Object getItem(int i) {
        return chatRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        if(chatRecords.get(i).isReceive()){
            view = layoutInflater.inflate(R.layout.activity_chat_receive,null);
        }else{
            view = layoutInflater.inflate(R.layout.activity_chat_send,null);
        }
        holder.chatContent = (TextView) view.findViewById(R.id.text);
        holder.chatContent.setText(chatRecords.get(i).getMessage());
        return view;
    }

    private class Holder {
        public TextView chatContent;
    }
}
