package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import webService.OkHttp;

public class AddFriendOrGroup extends AppCompatActivity {

    ImageButton searchFriend;
    ImageButton searchGroup;
    OkHttp okHttp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_or_group);
        initView();
    }
    public void initView(){
        searchFriend = findViewById(R.id.searchFriend);
        searchGroup = findViewById(R.id.searchGroup);

        okHttp = new OkHttp();
        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okHttp.searchGroup("");
            }
        });


        searchGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okHttp.test("");
            }
        });
    }
}