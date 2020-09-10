package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Inf.FriendDetailedInf;

import webService.OkHttp;

public class AddFriendOrGroup extends AppCompatActivity {

    ImageButton searchFriend;
    ImageButton searchGroup;
    OkHttp okHttp;
    EditText groupName;
    EditText friendName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_or_group);
        initView();
    }
    public void initView(){
        searchFriend = findViewById(R.id.searchFriend);
        searchGroup = findViewById(R.id.searchGroup);
        groupName = findViewById(R.id.groupName);
        friendName = findViewById(R.id.friendName);

        okHttp = new OkHttp();
        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String searchFriend = friendName.getText().toString();
                        FriendDetailedInf friendDetailedInf = okHttp.searchFriend(searchFriend);
                        Looper.prepare();
                        if(friendDetailedInf == null){
                            Toast.makeText(getApplicationContext(),"该用户不存在", Toast.LENGTH_SHORT).show();
                        }else{
                            System.out.println("37r2y8723rh8wb");
                            Toast.makeText(getApplicationContext(),"该用户存在", Toast.LENGTH_SHORT).show();
                        }

                        Looper.loop();
                    }
                }).start();


            }
        });


        searchGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchGroup = groupName.getText().toString();
                okHttp.searchGroup(searchGroup);
            }
        });
    }
}