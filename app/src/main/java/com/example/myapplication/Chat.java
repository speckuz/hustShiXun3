package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import Inf.ChatRecord;
import Inf.Friend;
import adapter.ChatAdapter;
import webService.OkHttp;

public class Chat extends AppCompatActivity {

    private EditText editText;
    private ArrayList<ChatRecord> chatRecords;
    private ListView chatRecord;
    private BaseAdapter adapter;
    private ImageButton sendText;
    private ImageButton backToChatList;
    private ImageButton voiceMessage;
    private ImageButton more;
    private String message;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
    }
    public void initView(){
        //聊天记录

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println(id);

        chatRecords = new ArrayList<ChatRecord>();
        chatRecords.add(new ChatRecord("123",true));
        chatRecords.add(new ChatRecord("123",false));


        editText = (EditText) findViewById(R.id.textMessage);

        chatRecord = (ListView) findViewById(R.id.chatRecord);
        adapter = new ChatAdapter(chatRecords,this);
        chatRecord.setAdapter(adapter);
        backToChatList = (ImageButton) findViewById(R.id.backToChatList);
        voiceMessage = (ImageButton) findViewById(R.id.voiceMessage);
        more = (ImageButton) findViewById(R.id.more);
        sendText = (ImageButton) findViewById(R.id.sendText);


        backToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Chat.this,ChatList.class));
                finish();
            }
        });

        //发送语音
        voiceMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //获取更多
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this,FriendInf.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //发送文字消息

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取文字消息
                sendMessage();

            }
        });
    }
    private void sendMessage(){
        message = editText.getText().toString();
        new Thread(){
            @Override
            public void run() {
                super.run();
                new OkHttp().test(message);
            }
        }.start();

        chatRecords.add(new ChatRecord(message,false));
        editText.setText("");
        adapter.notifyDataSetChanged();
        chatRecord.smoothScrollToPosition(chatRecords.size());

    }


}