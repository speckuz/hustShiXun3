package com.example.myapplication.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.AddFriendOrGroup;
import com.example.myapplication.Chat;
import com.example.myapplication.Mine;
import com.example.myapplication.R;
import com.example.myapplication.SwipeListLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.myapplication.Inf.RecentChatList;

public class ChatList extends AppCompatActivity {


    private ImageButton profilePicture;
    private ImageButton toFriendList;
    private ImageButton toMoment;
    private ImageButton addFriendOrGroup;
    private ArrayList<RecentChatList> recentChatLists;
    private ListView chatList;
    private BaseAdapter adapter;
    private Set<SwipeListLayout> sets = new HashSet();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recentChatLists = new ArrayList<RecentChatList>();
        recentChatLists.add(new RecentChatList("123","1","223"));
        recentChatLists.add(new RecentChatList("234","2","223"));
        recentChatLists.add(new RecentChatList("57","3","223"));
        chatList = (ListView) findViewById(R.id.chatList);

        adapter = new ChatListAdapter(recentChatLists,this);
        chatList.setAdapter(adapter);


//        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//

//            }
//        });

        chatList.setOnItemClickListener(new AbsListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecentChatList recentChatList = recentChatLists.get(i);
                Intent intent = new Intent(ChatList.this, Chat.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", recentChatList.getUserName());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        chatList.setOnScrollListener(new AbsListView.OnScrollListener(){

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

        addFriendOrGroup = (ImageButton) findViewById(R.id.addFriendOrGroup);
        addFriendOrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatList.this, AddFriendOrGroup.class));
            }
        });

        //查看我的信息
        profilePicture = (ImageButton) findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatList.this, Mine.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
        //查看好友列表
        toFriendList = (ImageButton) findViewById(R.id.toFriendList);
        toFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( ChatList.this, FriendList.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        });
        //查看朋友圈
        toMoment = (ImageButton) findViewById(R.id.toMoment);
        toMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatList.this, MyMoment.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", "mine");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        });

    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }




    public class ChatListAdapter extends BaseAdapter {

        private ArrayList<RecentChatList> recentChatLists;
        private LayoutInflater layoutInflater;

        public ChatListAdapter(ArrayList<RecentChatList> recentChatLists, Context context){
            this.recentChatLists = recentChatLists;
            this.layoutInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return recentChatLists.size();
        }

        @Override
        public Object getItem(int i) {
            return recentChatLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            RecentChat recentChat = new RecentChat();

            view = layoutInflater.inflate(R.layout.activity_recent_chat_list,null);
            recentChat.recentName = (TextView) view.findViewById(R.id.recentName);
            recentChat.recentText = (TextView) view.findViewById(R.id.recentText);
            recentChat.recentName.setText(recentChatLists.get(i).getUserName());
            recentChat.recentText.setText(recentChatLists.get(i).getRecentText());


            LinearLayout clicked = (LinearLayout) view.findViewById(R.id.clicked);
            clicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecentChatList recentChatList = recentChatLists.get(i);
                    Intent intent = new Intent(ChatList.this,Chat.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id", recentChatList.getUserName());
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
            final SwipeListLayout sll_main = (SwipeListLayout) view.findViewById(R.id.sll_main);
            TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            sll_main.setOnSwipeStatusListener(new ChatList.MyOnSlipStatusListener(sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    RecentChatList recentChatList =recentChatLists.get(i);
                    recentChatLists.remove(i);
                    recentChatLists.add(0, recentChatList);
                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    recentChatLists.remove(i);
                    notifyDataSetChanged();
                }
            });
            return view;
        }


    }

    private class RecentChat {
        public TextView recentName;
        public TextView recentText;
    }
}