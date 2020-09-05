package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

import Inf.Friend;
import Inf.RecentChatList;

public class FriendListAdapter extends BaseAdapter {
    private ArrayList<Friend> friendLists;
    private LayoutInflater layoutInflater;

    public FriendListAdapter(ArrayList<Friend> friendLists, Context context){
        this.friendLists = friendLists;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return friendLists.size();
    }

    @Override
    public Object getItem(int i) {
        return friendLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FriendList friendList = new FriendList();

        view = layoutInflater.inflate(R.layout.activity_friend,null);
        friendList.friendName = (TextView) view.findViewById(R.id.friendName);
        friendList.friendText = (TextView) view.findViewById(R.id.friendText);
        friendList.friendName.setText(friendLists.get(i).getFriendName());
        friendList.friendText.setText(friendLists.get(i).getFriendText());
        return view;
    }

    private class FriendList {
        public TextView friendName;
        public TextView friendText;
    }
}
