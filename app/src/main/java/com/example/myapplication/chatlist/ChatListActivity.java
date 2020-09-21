package com.example.myapplication.chatlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myapplication.others.AddFriendOrGroupActivity;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.checkinf.MineActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.myapplication.inf.RecentChatList;
import com.example.myapplication.friendlist.FriendListActivity;
import com.example.myapplication.moment.MyMomentActivity;

public class ChatListActivity extends AppCompatActivity {


    private ImageButton imgBtnProfilePicture;
    private ImageButton imgBtnToFriendList;
    private ImageButton imgBtnToMoment;
    private ImageButton imgBtnAddFriendOrGroup;
    private ArrayList<RecentChatList> recentChatLists;
    private ListView lvChatList;
    private BaseAdapter adapter;
    private EditText etChatSearch;
    private LinearLayout llChatNavigationBar;
    private LinearLayout llChatSearch;
    private Set<SwipeListLayout> sets = new HashSet();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recentChatLists = new ArrayList<RecentChatList>();
        recentChatLists.add(new RecentChatList("123", "1", "223"));
        recentChatLists.add(new RecentChatList("234", "2", "223"));
        recentChatLists.add(new RecentChatList("57", "3", "223"));
        lvChatList = (ListView) findViewById(R.id.lv_chat_list);
        imgBtnAddFriendOrGroup = (ImageButton) findViewById(R.id.img_btn_chat_add_friend_or_group);
        etChatSearch = (EditText)findViewById(R.id.et_chat_search);
        llChatNavigationBar = (LinearLayout)findViewById(R.id.ll_chat_navigation_bar);
        llChatSearch = (LinearLayout) findViewById(R.id.ll_chat_search);
        adapter = new ChatListAdapter(recentChatLists, this,this,sets);
        lvChatList.setAdapter(adapter);

        etChatSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    llChatSearch.setVisibility(View.GONE);
//                    lvChatList.setVisibility(View.GONE);
//                    llChatNavigationBar.setVisibility(View.GONE);
                }else {
//                    lvChatList.setVisibility(View.VISIBLE);
//                    llChatNavigationBar.setVisibility(View.VISIBLE);
                }
            }
        });

        lvChatList.setOnItemClickListener(new AbsListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecentChatList recentChatList = recentChatLists.get(i);
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", recentChatList.getUserName());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        lvChatList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


        imgBtnAddFriendOrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatListActivity.this, AddFriendOrGroupActivity.class));
            }
        });

        //查看我的信息
        imgBtnProfilePicture = (ImageButton) findViewById(R.id.img_btn_chat_profile_picture);
        imgBtnProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatListActivity.this, MineActivity.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
        //查看好友列表
        imgBtnToFriendList = (ImageButton) findViewById(R.id.img_btn_chat_to_friend_list);
        imgBtnToFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatListActivity.this, FriendListActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        });
        //查看朋友圈
        imgBtnToMoment = (ImageButton) findViewById(R.id.img_btn_chat_to_moment);
        imgBtnToMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatListActivity.this, MyMomentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "mine");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        });

    }


}




