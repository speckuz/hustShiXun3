package com.example.myapplication.friendlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

import com.example.myapplication.inf.Group;

public class GroupListAdapter extends BaseExpandableListAdapter {
    private ArrayList<ArrayList<Group>> groupList;
    private ArrayList<String> groupName;
    private LayoutInflater layoutInflater;
    private Context context;
    public GroupListAdapter(ArrayList<ArrayList<Group>> friendLists, Context context, ArrayList<String> groupName){
        this.groupList = friendLists;
        this.layoutInflater = LayoutInflater.from(context);
        this.groupName = groupName;
        this.context = context;
    }


    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groupList.get(i).get(i1);
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
        final Group group = groupList.get(i).get(i1);
        view = layoutInflater.inflate(R.layout.adapter_item_group_list,null);
        TextView groupName = (TextView) view.findViewById(R.id.tv_group_name);
        groupName.setText(group.getGroupId()+"");
        TextView groupText = (TextView) view.findViewById(R.id.tv_group_text);
        groupText.setText(group.getGroupName());
        return view;
    }



    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
