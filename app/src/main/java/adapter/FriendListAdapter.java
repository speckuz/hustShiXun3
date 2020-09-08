package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

import Inf.Friend;
import Inf.RecentChatList;

public class FriendListAdapter extends BaseExpandableListAdapter {
    private ArrayList<ArrayList<Friend>> friendLists;
    private ArrayList<String> groupName;
    private LayoutInflater layoutInflater;

    public FriendListAdapter(ArrayList<ArrayList<Friend>> friendLists, Context context,ArrayList<String> groupName){
        this.friendLists = friendLists;
        this.layoutInflater = LayoutInflater.from(context);
        this.groupName = groupName;
    }

//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        FriendList friendList = new FriendList();
//
//        view = layoutInflater.inflate(R.layout.activity_friend,null);
//        friendList.friendName = (TextView) view.findViewById(R.id.friendName);
//        friendList.friendText = (TextView) view.findViewById(R.id.friendText);
//        friendList.friendName.setText(friendLists.get(i).getFriendName());
//        friendList.friendText.setText(friendLists.get(i).getFriendText());
//        return view;
//    }

    @Override
    public int getGroupCount() {
        return groupName.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return friendLists.get(i).size();
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
        return false;
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
        Friend friendList = friendLists.get(i).get(i1);

        view = layoutInflater.inflate(R.layout.activity_friend,null);
        TextView friendName = (TextView) view.findViewById(R.id.friendName);
        friendName.setText(friendList.getFriendText());
        TextView friendText = (TextView) view.findViewById(R.id.friendText);
        friendText.setText(friendList.getFriendText());

//        friendList.friendName = (TextView) view.findViewById(R.id.friendName);
//        friendList.friendText = (TextView) view.findViewById(R.id.friendText);
//        friendList.friendName.setText(friendLists.get(i).getFriendName());
//        friendList.friendText.setText(friendLists.get(i).getFriendText());
        return view;

//        TextView tvChild = (TextView)convertView.findViewById(R.id.tv_name);
//        tvChild.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext,child,Toast.LENGTH_SHORT).show();
//            }
//        });
//        tvChild.setText(child);
//        return convertView;
//        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
