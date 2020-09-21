package com.example.myapplication.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.myapplication.checkinf.FriendInfActivity;
import com.example.myapplication.inf.ChatRecord;

import com.example.myapplication.R;
import com.example.myapplication.chatlist.ChatListActivity;

public class ChatActivity extends AppCompatActivity {

    private EditText etTextMessage;
    private ArrayList<ChatRecord> chatRecords;
    private ListView lvChatRecord;
    private TextView tvFriendName;
    private BaseAdapter adapter;
    private ImageButton imgBtnSendText;
    private ImageButton imgBtnBackToChatList;
    private ImageButton imgBtnVoiceMessage;
    private ImageButton imgBtnShowOthers;
    private ImageButton imgBtnMore;
    private LinearLayout llShowVoice;
    private LinearLayout llShowPicture;
    private CustomRecordImagineButton imgBtnSoundRecording;
    private boolean isVoiceOpened;
    private boolean isPictureOpened;
    private String message;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        this.getExternalFilesDir(null);
    }
    public void initView(){
        //聊天记录

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println(id);

        chatRecords = new ArrayList<ChatRecord>();
        chatRecords.add(new ChatRecord("123",ChatRecord.RECEIVE,ChatRecord.TEXT));
        chatRecords.add(new ChatRecord("123",ChatRecord.SEND,ChatRecord.TEXT));
        chatRecords.add(new ChatRecord("1'",ChatRecord.SEND,ChatRecord.VOICE));
        chatRecords.add(new ChatRecord("1'12''",ChatRecord.RECEIVE,ChatRecord.VOICE));
        chatRecords.add(new ChatRecord("森林公园",ChatRecord.RECEIVE,ChatRecord.LOCATION));
        chatRecords.add(new ChatRecord("华中科技大学",ChatRecord.SEND,ChatRecord.LOCATION));
        chatRecords.add(new ChatRecord("123",ChatRecord.RECEIVE,ChatRecord.PICTURE));
        chatRecords.add(new ChatRecord("123",ChatRecord.SEND,ChatRecord.PICTURE));
        isVoiceOpened = false;
        isPictureOpened = false;

        etTextMessage = (EditText) findViewById(R.id.et_text_message);
        lvChatRecord = (ListView) findViewById(R.id.tv_chat_record);
        imgBtnBackToChatList = (ImageButton) findViewById(R.id.img_btn_back_to_chatList);
        imgBtnVoiceMessage = (ImageButton) findViewById(R.id.img_btn_voice_message);
        imgBtnMore = (ImageButton) findViewById(R.id.img_btn_more);
        imgBtnSendText = (ImageButton) findViewById(R.id.img_btn_send_text);
        llShowVoice = (LinearLayout)findViewById(R.id.ll_show_voice);
        llShowPicture = (LinearLayout)findViewById(R.id.ll_others);
        imgBtnShowOthers = (ImageButton)findViewById(R.id.img_btn_show_others);
        tvFriendName = (TextView)findViewById(R.id.tv_friend_name);
        imgBtnSoundRecording = (CustomRecordImagineButton)findViewById(R.id.img_btn_sound_recording);


        adapter = new ChatAdapter(chatRecords,this);
        lvChatRecord.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvChatRecord.smoothScrollToPosition(chatRecords.size());

        imgBtnSoundRecording.init(chatRecords,this,adapter);

        imgBtnBackToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatActivity.this, ChatListActivity.class));
                finish();
            }
        });

        //发送语音
        imgBtnVoiceMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPictureOpened){
                    imgBtnShowOthers.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_show_picture_false));
                    llShowPicture.setVisibility(View.GONE);
                    isPictureOpened = false;
                }
                if(isVoiceOpened){
                    imgBtnVoiceMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_voice_show_false));
                    llShowVoice.setVisibility(View.GONE);
                    isVoiceOpened = false;
                }else{
                    imgBtnVoiceMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_voice_show_true));
                    llShowVoice.setVisibility(View.VISIBLE);
                    isVoiceOpened = true;
                }

            }
        });

        imgBtnShowOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVoiceOpened){
                    imgBtnVoiceMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_voice_show_false));
                    llShowVoice.setVisibility(View.GONE);
                    isVoiceOpened = false;
                }
                if(isPictureOpened){
                    imgBtnShowOthers.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_show_picture_false));
                    llShowPicture.setVisibility(View.GONE);
                    isPictureOpened = false;
                }else{
                    imgBtnShowOthers.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_show_picture_true));
                    llShowPicture.setVisibility(View.VISIBLE);
                    isPictureOpened = true;
                }

            }
        });

        //获取更多
        imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, FriendInfActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //发送文字消息

        imgBtnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取文字消息
                sendMessage();

            }
        });
    }


    private void sendMessage(){
        message = etTextMessage.getText().toString();
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                new OkHttp().searchFriend(message);
//            }
//        }.start();

        chatRecords.add(new ChatRecord(message,ChatRecord.SEND,ChatRecord.TEXT));
        etTextMessage.setText("");
        adapter.notifyDataSetChanged();
        lvChatRecord.smoothScrollToPosition(chatRecords.size());

    }


}