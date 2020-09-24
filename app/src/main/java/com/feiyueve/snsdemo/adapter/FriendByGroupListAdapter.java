package com.feiyueve.snsdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyueve.snsdemo.ChatActivity;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.inf.Friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendByGroupListAdapter extends BaseExpandableListAdapter {
    private HashMap<String, ArrayList<Friend>> friendLists;
    private ArrayList<String> groupName;
    private LayoutInflater layoutInflater;
    private Context context;
    public FriendByGroupListAdapter(HashMap<String, ArrayList<Friend>> friendLists, Context context, ArrayList<String> groupName){
        this.friendLists = friendLists;
        this.layoutInflater = LayoutInflater.from(context);
        this.groupName = groupName;
        this.context = context;
    }


    @Override
    public int getGroupCount() {
        return friendLists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return friendLists.get(groupName.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return friendLists.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return friendLists.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.activity_friend_group,null);
        String group = groupName.get(i);
        TextView tvGroup = (TextView) view.findViewById(R.id.tv_group);
        tvGroup.setText(group);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final Friend friend = friendLists.get(groupName.get(i)).get(i1);
        view = layoutInflater.inflate(R.layout.activity_friend,null);
        TextView friendName = (TextView) view.findViewById(R.id.tv_friend_by_group_name);
        friendName.setText(friend.getFriendNickName());

        TextView friendText = (TextView) view.findViewById(R.id.tv_friend_by_group_text);
        friendText.setText(friend.getRecentChatText());

        ImageView friendHead = (ImageView)view.findViewById(R.id.iv_friend_by_group_head);
        if(friend.getFriendHead()!=null)
            friendHead.setImageBitmap(EncodeBase64.stringtoBitmap(friend.getFriendHead()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                MyApplication.getInstance().setCurrentFriend(friend);
                bundle.putSerializable("friend", friend);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }



    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
