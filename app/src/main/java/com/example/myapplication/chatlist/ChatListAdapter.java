package com.example.myapplication.chatlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.inf.RecentChatList;

import java.util.ArrayList;
import java.util.Set;

public class ChatListAdapter extends BaseAdapter {

    private ArrayList<RecentChatList> recentChatLists;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private Set<SwipeListLayout> sets;

    public ChatListAdapter(ArrayList<RecentChatList> recentChatLists, Context context,Activity activity,Set<SwipeListLayout> sets){
        this.recentChatLists = recentChatLists;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
        this.sets = sets;
    }

    @Override
    public int getCount() {
        return recentChatLists.size();
    }

    @Override
    public Object getItem(int i) {
        return recentChatLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        RecentChat recentChat = new RecentChat();

        view = layoutInflater.inflate(R.layout.adapter_item_recent_chat,null);
        recentChat.tvRecentName = (TextView) view.findViewById(R.id.recentName);
        recentChat.tvRecentText = (TextView) view.findViewById(R.id.recentText);
        recentChat.tvRecentName.setText(recentChatLists.get(i).getUserName());
        recentChat.tvRecentText.setText(recentChatLists.get(i).getRecentText());


        LinearLayout clicked = (LinearLayout) view.findViewById(R.id.clicked);
        clicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentChatList recentChatList = recentChatLists.get(i);
                Intent intent = new Intent(activity, ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", recentChatList.getUserName());
                intent.putExtras(bundle);

                activity.startActivity(intent);
            }
        });
        final SwipeListLayout sll_main = (SwipeListLayout) view.findViewById(R.id.sll_main);
        TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(sll_main,sets));
        tv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                RecentChatList recentChatList =recentChatLists.get(i);
                recentChatLists.remove(i);
                recentChatLists.add(0, recentChatList);
                notifyDataSetChanged();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                recentChatLists.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private class RecentChat {
        public TextView tvRecentName;
        public TextView tvRecentText;
    }
}





