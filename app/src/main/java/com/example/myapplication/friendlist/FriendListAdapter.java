package com.example.myapplication.friendlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.checkinf.FriendInfActivity;
import com.example.myapplication.inf.Friend;
import com.example.myapplication.inf.FriendDetailedInf;
import com.example.myapplication.webService.FriendInfWebService;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {

    private ArrayList<Friend> friendList;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private FriendInfWebService friendInfWebService;
    public FriendListAdapter(ArrayList<Friend> friendList, Context context, Activity activity){
        this.friendList = friendList;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Friend friendI = friendList.get(i);
        view = layoutInflater.inflate(R.layout.activity_friend,null);
        TextView friendName = (TextView) view.findViewById(R.id.tv_friend_by_group_name);
        friendName.setText(friendI.getFriendName());
        TextView friendText = (TextView) view.findViewById(R.id.tv_friend_by_group_text);
        friendText.setText(friendI.getFriendText());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(friendInfWebService == null)
                            friendInfWebService = new FriendInfWebService();
                        FriendDetailedInf friendDetailedInf = friendInfWebService.search(friendI.getId());
                        if(friendDetailedInf != null){
                            Intent intent = new Intent(activity, FriendInfActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("friendDetailedInf", friendDetailedInf);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    }
                }).start();
            }
        });
        return view;
    }
}
