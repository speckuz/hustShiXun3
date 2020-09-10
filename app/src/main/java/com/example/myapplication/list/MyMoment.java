package com.example.myapplication.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.myapplication.Inf.Moment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MomentAdapter;

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
        moments.add(new Moment("leo","1","今天好开心","2019.01.01 12：30",false));
        moments.add(new Moment("leo","1","我爱夏天","2019.01.01 12：30",true));
        moments.add(new Moment("leo","1","我不喜欢吃柑橘","2019.01.01 12：30",false));

        adapter = new MomentAdapter(moments,this);
        momentList.setAdapter(adapter);

        toChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MyMoment.this, ChatList.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
        });

        toFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MyMoment.this, FriendList.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
        });
    }
}