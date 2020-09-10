package com.example.myapplication.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;

import com.example.myapplication.Inf.Friend;

import com.example.myapplication.AddFriendOrGroup;
import com.example.myapplication.Mine;
import com.example.myapplication.R;
import com.example.myapplication.adapter.FriendListAdapter;

public class FriendList extends AppCompatActivity {

    private ImageButton profilePicture;
    private ImageButton toFriendList;
    private ImageButton toChatList;
    private ImageButton toMoment;
    private ImageButton addFriendOrGroup;
    private ArrayList<String> groupName;
    private ArrayList<ArrayList<Friend>> friendLists;
    private ExpandableListView friendList;
    private BaseExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        initView();

    }
    public void initView(){
        profilePicture = (ImageButton) findViewById(R.id.profilePicture);
        toChatList = (ImageButton) findViewById(R.id.toChatList);
        toFriendList = (ImageButton) findViewById(R.id.toFriendList);
        toMoment = (ImageButton) findViewById(R.id.toMoment);
        friendList = (ExpandableListView) findViewById(R.id.friendList);
        friendLists = new ArrayList<ArrayList<Friend>>();

        groupName = new ArrayList<String>();
        groupName.add("leo的男朋友");
        ArrayList<Friend> friendList1 = new ArrayList<Friend>();
        friendList1.add(new Friend("123","11","22"));
        friendList1.add(new Friend("456","1122","232"));

        groupName.add("leo的女朋友");
        ArrayList<Friend> friendList2 = new ArrayList<Friend>();
        friendList2.add(new Friend("123","11","22"));
        friendList2.add(new Friend("456","1122","232"));


        friendLists.add(friendList1);
        friendLists.add(friendList2);
        adapter = new FriendListAdapter(friendLists,this,groupName);
        friendList.setAdapter(adapter);

//        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Friend friend = friendLists.get(i);
//                Intent intent = new Intent(FriendList.this,FriendInf.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("id", friend.getId());
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//            }
//        });



        profilePicture = (ImageButton) findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendList.this, Mine.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        addFriendOrGroup = (ImageButton) findViewById(R.id.addFriendOrGroup);
        addFriendOrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendList.this, AddFriendOrGroup.class));
            }
        });

        toChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendList.this, ChatList.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
        });

        toMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendList.this, MyMoment.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", "mine");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        });
    }
}