package com.example.myapplication.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.myapplication.chatlist.ChatListActivity;
import com.example.myapplication.friendlist.FriendListActivity;
import com.example.myapplication.inf.Comment;
import com.example.myapplication.inf.Moment;

import com.example.myapplication.R;
import com.example.myapplication.webService.SceneryWebService;

public class MyMomentActivity extends AppCompatActivity {


    private ImageButton imgBtnToFriendList;
    private ImageButton imgBtnToChatList;
    private ImageButton imgBtnPostMoment;
    private LinearLayout llBar;
    private int id;
    private ArrayList<Moment> moments;
    private ListView lvMomentList;
    private BaseAdapter adapter;
    private SceneryWebService sceneryWebService;
    private Context context;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment);
        initView();

    }
    public void initView(){
        imgBtnToChatList = (ImageButton) findViewById(R.id.img_btn_moment_to_chat_list);
        imgBtnToFriendList = (ImageButton) findViewById(R.id.img_btn_moment_to_friend_list);
        imgBtnPostMoment = (ImageButton) findViewById(R.id.img_btn_post_moment);
        llBar = (LinearLayout) findViewById(R.id.ll_bar);
        context = this;
        activity = this;
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        System.out.println(id);
        lvMomentList = (ListView) findViewById(R.id.lv_moment_list);

        if(id == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(sceneryWebService == null)
                        sceneryWebService = new SceneryWebService();
                    moments = new ArrayList<Moment>();
                    moments = sceneryWebService.getWorldScenery(1+"",2);
                    adapter = new MomentAdapter(moments,context,activity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvMomentList.setAdapter(adapter);
                        }
                    });
                }
            }).start();
        }else if(id == -1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(sceneryWebService == null)
                        sceneryWebService = new SceneryWebService();
                    moments = new ArrayList<Moment>();
                    //moments = sceneryWebService.getWorldScenery(1+"",2);
                    adapter = new MomentAdapter(moments,context,activity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvMomentList.setAdapter(adapter);
                        }
                    });
                }
            }).start();
        }else{
            llBar.setVisibility(View.GONE);
            imgBtnPostMoment.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(sceneryWebService == null)
                        sceneryWebService = new SceneryWebService();
                    moments = new ArrayList<Moment>();
                    //moments = sceneryWebService.getWorldScenery(1+"",2);
                    moments = sceneryWebService.getOneFriendScenery(1+"",2,id);
                    adapter = new MomentAdapter(moments,context,activity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvMomentList.setAdapter(adapter);
                        }
                    });
                }
            }).start();

        }

        imgBtnPostMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(sceneryWebService == null)
                            sceneryWebService = new SceneryWebService();
                        sceneryWebService.postScenery("123",null);
                        ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
                        moments.clear();
                        moments.addAll(momentsTemp);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("adsidjsijdisjdis");
                                ((BaseAdapter) adapter).notifyDataSetChanged();
                            }
                        });

                    }
                }).start();
            }
        });

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