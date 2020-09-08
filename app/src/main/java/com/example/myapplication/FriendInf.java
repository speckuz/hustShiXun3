package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Inf.Friend;

public class FriendInf extends AppCompatActivity {

    Button send;
    Friend friend;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_inf);
        initView();
    }
    void initView(){
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        friend = new Friend("1","123","123");
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendInf.this,Chat.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",friend.getId());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }
}