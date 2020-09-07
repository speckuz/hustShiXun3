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

import Inf.Friend;
import Inf.RecentChatList;
import adapter.FriendListAdapter;

public class FriendList extends AppCompatActivity {

    private ImageButton profilePicture;
    private ImageButton toFriendList;
    private ImageButton toChatList;
    private ImageButton toMoment;
    private ArrayList<Friend> friendLists;
    private ListView friendList;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        initView();

    }
    public void initView(){
        profilePicture = (ImageButton) findViewById(R.id.profilePicture);
        toChatList = (ImageButton) findViewById(R.id.toChatList);
        toFriendList = (ImageButton) findViewById(R.id.toFriendList);
        toMoment = (ImageButton) findViewById(R.id.toMoment);
        friendList = (ListView) findViewById(R.id.friendList);
        friendLists = new ArrayList<Friend>();
        friendLists.add(new Friend("123","11","22"));
        friendLists.add(new Friend("456","1122","232"));
        adapter = new FriendListAdapter(friendLists,this);
        friendList.setAdapter(adapter);

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Friend friend = friendLists.get(i);
                Intent intent = new Intent(FriendList.this,FriendInf.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", friend.getId());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        toChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendList.this,ChatList.class));
                finish();
            }
        });

        toMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendList.this,MyMoment.class));
                finish();
            }
        });
    }
}