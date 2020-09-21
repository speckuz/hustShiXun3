package com.example.myapplication.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.myapplication.chatlist.ChatListActivity;
import com.example.myapplication.friendlist.FriendListActivity;
import com.example.myapplication.inf.Comment;
import com.example.myapplication.inf.Moment;

import com.example.myapplication.R;

public class MyMomentActivity extends AppCompatActivity {


    private ImageButton imgBtnToFriendList;
    private ImageButton imgBtnToChatList;
    private String id;
    private ArrayList<Moment> moments;
    private ListView lvMomentList;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment);
        initView();

    }
    public void initView(){
        imgBtnToChatList = (ImageButton) findViewById(R.id.img_btn_moment_to_chat_list);
        imgBtnToFriendList = (ImageButton) findViewById(R.id.img_btn_moment_to_friend_list);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println(id);
        lvMomentList = (ListView) findViewById(R.id.lv_moment_list);
        moments = new ArrayList<Moment>();

        ArrayList<Comment> comments1 = new ArrayList<Comment>();
        ArrayList<Comment> comments2 = new ArrayList<Comment>();
        ArrayList<Comment> comments3 = new ArrayList<Comment>();

        comments1.add(new Comment(1,0,1,"澳哥哥"));
        comments2.add(new Comment(1,0,1,"澳哥哥"));
        comments3.add(new Comment(1,0,1,"澳哥哥"));
        comments1.add(new Comment(2,0,2,"澳哥哥"));
        comments2.add(new Comment(2,0,2,"澳哥哥"));
        comments3.add(new Comment(2,0,2,"澳哥哥"));
        comments1.add(new Comment(3,1,3,"澳哥哥"));
        comments2.add(new Comment(3,2,3,"澳哥哥"));
        comments3.add(new Comment(3,1,3,"澳哥哥"));


        ArrayList<String> likeUserName = new ArrayList<String>();
        likeUserName.add("迷弟1");
        likeUserName.add("迷弟12");
        likeUserName.add("迷弟122");
        likeUserName.add("迷弟1222");

        ArrayList<String> likeUserName1 = new ArrayList<String>();
        likeUserName1.add("迷弟1");
        likeUserName1.add("迷弟12");
        likeUserName1.add("迷弟122");
        likeUserName1.add("迷弟1222");

        ArrayList<String> likeUserName2 = new ArrayList<String>();
        likeUserName2.add("迷弟1");
        likeUserName2.add("迷弟12");
        likeUserName2.add("迷弟122");
        likeUserName2.add("迷弟1222");

        moments.add(new Moment("leo","1","今天好开心","2019.01.01 12：30",false,likeUserName,comments1));
        moments.add(new Moment("leo","1","我爱夏天","2019.01.01 12：30",true,likeUserName1,comments2));
        moments.add(new Moment("leo","1","我不喜欢吃柑橘","2019.01.01 12：30",false,likeUserName2,comments3));

        adapter = new MomentAdapter(moments,this,this);
        lvMomentList.setAdapter(adapter);

        imgBtnToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MyMomentActivity.this, ChatListActivity.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
        });

        imgBtnToFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MyMomentActivity.this, FriendListActivity.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
        });
    }
}