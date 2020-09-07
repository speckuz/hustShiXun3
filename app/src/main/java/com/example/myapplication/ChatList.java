package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import Inf.RecentChatList;
import adapter.ChatListAdapter;

public class ChatList extends AppCompatActivity {


    private ImageButton profilePicture;
    private ImageButton toFriendList;
    private ImageButton toMoment;
    private ArrayList<RecentChatList> recentChatLists;
    private ListView chatList;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recentChatLists = new ArrayList<RecentChatList>();
        recentChatLists.add(new RecentChatList("123","123","223"));
        recentChatLists.add(new RecentChatList("234","123","223"));
        recentChatLists.add(new RecentChatList("57","123","223"));
        chatList = (ListView) findViewById(R.id.chatList);

        adapter = new ChatListAdapter(recentChatLists,this);
        chatList.setAdapter(adapter);

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RecentChatList recentChatList = recentChatLists.get(i);
                Intent intent = new Intent(ChatList.this,Chat.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", recentChatList.getUserName());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        //查看我的信息
        profilePicture = (ImageButton) findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatList.this,Mine.class));
            }
        });
        //查看好友列表
        toFriendList = (ImageButton) findViewById(R.id.toFriendList);
        toFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatList.this,FriendList.class));
                finish();
            }
        });
        //查看朋友圈
        toMoment = (ImageButton) findViewById(R.id.toMoment);
        toMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatList.this,MyMoment.class));
                finish();
            }
        });

    }
}