package com.feiyueve.snsdemo.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyueve.snsdemo.ChatActivity;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.chatlist.MyOnSlipStatusListener;
import com.feiyueve.snsdemo.chatlist.SwipeListLayout;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.inf.Friend;

import java.util.ArrayList;
import java.util.Set;

public class ChatListAdapter extends BaseAdapter {

    private ArrayList<Friend> recentChatLists;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private Set<SwipeListLayout> sets;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    MyApplication myApplication;
    private Friend friend;

    public ChatListAdapter(ArrayList<Friend> recentChatLists, Context context,Activity activity,Set<SwipeListLayout> sets){
        this.recentChatLists = recentChatLists;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
        this.sets = sets;
        dbHelper = new DBHelper(context, "sns.db", null, 1);
        db = dbHelper.getWritableDatabase();
        myApplication = MyApplication.getInstance();
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

        view = layoutInflater.inflate(R.layout.adapter_item_recent_chat,viewGroup,false);
        TextView tvRecentName = (TextView) view.findViewById(R.id.recentName);
        TextView tvRecentText = (TextView) view.findViewById(R.id.recentText);
        TextView tvTrcentTime = (TextView) view.findViewById(R.id.recentTime);
        ImageView imFriendHead = (ImageView) view.findViewById(R.id.recentHead);
        friend = recentChatLists.get(i);
        if(friend.getRecentChatText()!=null) {
            tvRecentName.setText(friend.getFriendNickName());
            if(friend.getRecentChatType().equals("0"))
                tvRecentText.setText(friend.getRecentChatText());
            if(friend.getRecentChatType().equals("3"))
                tvRecentText.setText("【语音："+friend.getRecentChatLength()+"秒】");
            if(friend.getRecentChatType().equals("1"))
                tvRecentText.setText("【图片】");
            tvTrcentTime.setText(friend.getRecentChatTime());
            if(friend.getFriendHead()!=null)
                imFriendHead.setImageBitmap(EncodeBase64.stringtoBitmap(friend.getFriendHead()));
        }else{
            recentChatLists.remove(i);
        }

        LinearLayout clicked = (LinearLayout) view.findViewById(R.id.clicked);
        clicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("friend",friend);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        final SwipeListLayout sll_main = (SwipeListLayout) view.findViewById(R.id.sll_main);
        TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(sll_main,sets));

        tv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                Friend recentChatList =recentChatLists.get(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put("isTop","true");
                dbHelper.update(db,myApplication.getPersonalInf().getUsername()+"Friend",contentValues,"friendName",new String[]{recentChatList.getFriendName()});

                recentChatLists.remove(i);
                recentChatLists.add(0, recentChatList);
                notifyDataSetChanged();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);

                Friend recentChatList =recentChatLists.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("recentChatText","null");
                contentValues.put("chatTimeText","null");
                dbHelper.update(db,myApplication.getPersonalInf().getUsername(),contentValues,"friendName",new String[]{recentChatList.getFriendName()});
                recentChatLists.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}





