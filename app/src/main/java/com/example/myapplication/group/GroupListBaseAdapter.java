package com.example.myapplication.group;

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
import com.example.myapplication.inf.Group;
import com.example.myapplication.webService.GroupWebService;

import java.util.ArrayList;

public class GroupListBaseAdapter extends BaseAdapter {
    private ArrayList<Group> groupList;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private GroupWebService groupWebService;
    public GroupListBaseAdapter(ArrayList<Group> groupList, Context context,Activity activity){
        this.groupList = groupList;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Group groupI = groupList.get(i);
        view = layoutInflater.inflate(R.layout.adapter_item_group_list,null);
        TextView groupName = (TextView) view.findViewById(R.id.tv_group_name);
        groupName.setText(groupI.getGroupName());
        TextView groupText = (TextView) view.findViewById(R.id.tv_group_text);
        groupText.setText(groupI.getGroupId() + "");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(groupWebService == null)
                            groupWebService = new GroupWebService();
                        Group group = groupWebService.searchGroup(groupI.getGroupId()+"");
                        if(group != null){
                            Intent intent = new Intent(activity, GroupInfActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("group", group);
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
