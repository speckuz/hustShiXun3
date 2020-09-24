package com.feiyueve.snsdemo.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.feiyueve.snsdemo.AddFriendOrGroupActivity;
import com.feiyueve.snsdemo.ChatActivity;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.HomeActivity;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.adapter.ChatListAdapter;
import com.feiyueve.snsdemo.cache.BitmapCache;
import com.feiyueve.snsdemo.chatlist.SwipeListLayout;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.inf.ChatMessage;
import com.feiyueve.snsdemo.inf.Friend;
import com.feiyueve.snsdemo.list.AccountList;
import com.feiyueve.snsdemo.list.MyListView;
import com.feiyueve.snsdemo.list.RecentChatList;
import com.feiyueve.snsdemo.permission.PermissionsUtilX;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageFragment extends Fragment {
    private ImageButton imgBtnProfilePicture;
    private ImageButton imgBtnAddFriendOrGroup;
    private MyListView lvChatList;
    private BaseAdapter adapter;
    private EditText etChatSearch;
    private LinearLayout llChatSearch;
    private Set<SwipeListLayout> sets = new HashSet();
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<Friend> lists;
    MyApplication myApplication;
    private Friend friend;
    private LocatiopnBroadcast locatiopnBroadcast;
    private Activity activity;
    private ChatMessage chatMessage;
    private PersonalInf personalInf;

    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = db.rawQuery("select * from "+personalInf.getUsername()+"Friend where chatText not null", null);
        lists = new ArrayList<>();
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String friendHead = cursor.getString(cursor.getColumnIndex("friendHead"));
                String friendName = cursor.getString(cursor.getColumnIndex("friendName"));
                String recentChatText = cursor.getString(cursor.getColumnIndex("chatText"));
                String chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                String isTop = cursor.getString(cursor.getColumnIndex("isTop"));
                friend = new Friend(friendName, recentChatText, friendHead, chatTimeText, isTop);
                friend.setId(cursor.getString(cursor.getColumnIndex("friendId")));
                friend.setAliasName(cursor.getString(cursor.getColumnIndex("aliasName")));
                friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                friend.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                friend.setFriendNickName(cursor.getString(cursor.getColumnIndex("friendNickName")));
                friend.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                friend.setRecentChatLength(cursor.getString(cursor.getColumnIndex("fileLength")));
                friend.setRecentChatType(cursor.getString(cursor.getColumnIndex("chatTextType")));
                if (isTop != null && isTop.equals("true"))
                    lists.add(0, friend);
                else
                    lists.add(friend);
            }
            if(lists.size()>0) {
                adapter = new ChatListAdapter(lists, this.getContext(), this.getActivity(), sets);
                lvChatList.setAdapter(adapter);
            }
        }
        cursor.close();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public class LocatiopnBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra("bundle");
            chatMessage = (ChatMessage)bundle.getSerializable("chatMessage");
            PersonalInf personalInf = (PersonalInf)bundle.getSerializable("personalInf");
            myApplication.setPersonalInf(personalInf);
            Cursor cursor = db.rawQuery("select * from "+personalInf.getUsername()+"Friend where friendId="+chatMessage.getSendId(), null);
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    String friendHead = cursor.getString(cursor.getColumnIndex("friendHead"));
                    String friendName = cursor.getString(cursor.getColumnIndex("friendName"));
                    String recentChatText = cursor.getString(cursor.getColumnIndex("chatText"));
                    String chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                    String isTop = cursor.getString(cursor.getColumnIndex("isTop"));
                    friend = new Friend(friendName, recentChatText, friendHead, chatTimeText, isTop);
                    friend.setId(cursor.getString(cursor.getColumnIndex("friendId")));
                    friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                    friend.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                    friend.setFriendNickName(cursor.getString(cursor.getColumnIndex("friendNickName")));
                    friend.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    friend.setRecentChatLength(cursor.getString(cursor.getColumnIndex("fileLength")));
                    friend.setRecentChatType(cursor.getString(cursor.getColumnIndex("chatTextType")));
                    if(lists.contains(friend))
                        lists.remove(friend);
                    if (isTop != null && isTop.equals("true"))
                        lists.add(0, friend);
                    else
                        lists.add(friend);
                }
                if(lists.size()>0) {
                    adapter = new ChatListAdapter(lists, context, activity, sets);
                    lvChatList.setAdapter(adapter);
                }
            }
            cursor.close();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_chat_list, container, false);
        dbHelper = new DBHelper(getContext(), "sns.db", null, 1);
        db = dbHelper.getWritableDatabase();
        myApplication = (MyApplication) getActivity().getApplication();
        navigationView = getActivity().findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        lvChatList = (MyListView) root.findViewById(R.id.lv_chat_list);
        imgBtnAddFriendOrGroup = (ImageButton) root.findViewById(R.id.img_btn_chat_add_friend_or_group);
        etChatSearch = (EditText)root.findViewById(R.id.et_chat_search);
        llChatSearch = (LinearLayout) root.findViewById(R.id.ll_chat_search);
        activity = getActivity();
        locatiopnBroadcast = new LocatiopnBroadcast();
        HomeActivity homeActivity = (HomeActivity)getActivity();
        personalInf = homeActivity.getPersonInf();
        IntentFilter intentFilter = new IntentFilter();
        // 2. 设置接收广播的类型
        intentFilter.addAction("cn.programmer.CUSTOM_INTENT");// 只有持有相同的action的接受者才能接收此广播
        // 3. 动态注册：调用Context的registerReceiver（）方法
        getActivity().registerReceiver(locatiopnBroadcast, intentFilter);

        Cursor cursor = db.rawQuery("select * from "+personalInf.getUsername()+"Friend where chatText not null", null);
        lists = new ArrayList<>();
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String friendHead = cursor.getString(cursor.getColumnIndex("friendHead"));
                String friendName = cursor.getString(cursor.getColumnIndex("friendName"));
                String recentChatText = cursor.getString(cursor.getColumnIndex("chatText"));
                String chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                String isTop = cursor.getString(cursor.getColumnIndex("isTop"));
                friend = new Friend(friendName, recentChatText, friendHead, chatTimeText, isTop);
                friend.setId(cursor.getString(cursor.getColumnIndex("friendId")));
                friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                friend.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                friend.setFriendNickName(cursor.getString(cursor.getColumnIndex("friendNickName")));
                friend.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                friend.setRecentChatLength(cursor.getString(cursor.getColumnIndex("fileLength")));
                friend.setRecentChatType(cursor.getString(cursor.getColumnIndex("chatTextType")));
                if (isTop != null && isTop.equals("true"))
                    lists.add(0, friend);
                else
                    lists.add(friend);
            }
            if(lists.size()>0) {
                adapter = new ChatListAdapter(lists, this.getContext(), this.getActivity(), sets);
                lvChatList.setAdapter(adapter);
            }
        }
        cursor.close();

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
                String permissions[] = {PermissionsUtilX.Permission.Microphone.RECORD_AUDIO};
                PermissionsUtilX permissionsUtilX = PermissionsUtilX.with(getActivity())
                        .requestCode(0)
                        .isDebug(true)
                        .permissions(permissions)
                        .request();
                Friend recentChatList = lists.get(i);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                myApplication.setCurrentFriend(new Friend(recentChatList.getFriendName()));
                dbHelper.createChatMessageTable(db,personalInf.getUsername(),recentChatList.getFriendName());
                bundle.putSerializable("friend", recentChatList);
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
                startActivity(new Intent(getActivity(), AddFriendOrGroupActivity.class));
            }
        });

        //查看我的信息
        imgBtnProfilePicture = (ImageButton) root.findViewById(R.id.img_btn_chat_profile_picture);
        imgBtnProfilePicture.setImageBitmap(EncodeBase64.stringtoBitmap(personalInf.getThumbnail()));
        imgBtnProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawer(navigationView);
                }else{
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });



        return root;
    }
}
