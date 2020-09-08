package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import Inf.Friend;
import Inf.Moment;
import Inf.RecentChatList;
import adapter.FriendListAdapter;
import adapter.MomentAdapter;

public class MyMoment extends AppCompatActivity {

    private ImageButton profilePicture;
    private ImageButton toFriendList;
    private ImageButton toChatList;
    private ImageButton toMoment;
    private String id;
    private ArrayList<Moment> moments;
    private ListView momentList;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment);
        initView();

    }
    public void initView(){
        profilePicture = (ImageButton) findViewById(R.id.profilePicture);
        toChatList = (ImageButton) findViewById(R.id.toChatList);
        toFriendList = (ImageButton) findViewById(R.id.toFriendList);
        toMoment = (ImageButton) findViewById(R.id.toMoment);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println(id);
        momentList = (ListView) findViewById(R.id.momentList);
        moments = new ArrayList<Moment>();
        moments.add(new Moment("leo","1","我爱男人","2019.01.01 12：30"));
        moments.add(new Moment("leo","1","我爱女人","2019.01.01 12：30"));
        moments.add(new Moment("leo","1","我爱null","2019.01.01 12：30"));

        adapter = new MomentAdapter(moments,this);
        momentList.setAdapter(adapter);

        toChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MyMoment.this,ChatList.class));
                finish();
            }
        });

        toFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MyMoment.this,FriendList.class));
                finish();
            }
        });
    }
}