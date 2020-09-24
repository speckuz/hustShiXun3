package com.feiyueve.snsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.inf.ChatMessage;
import com.feiyueve.snsdemo.inf.Friend;

public class FriendInfActivity extends AppCompatActivity {

    Button send;
    Friend friend;
    String id;
    private PersonalInf personalInf;
    private ChatMessage chatMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_inf);
        initView();

    }
    void initView(){
        Bundle bundle = this.getIntent().getExtras();
        friend = (Friend) bundle.getSerializable("friend");
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendInfActivity.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("friend", friend);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}