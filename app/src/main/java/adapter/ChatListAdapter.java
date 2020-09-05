package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import Inf.RecentChatList;

import java.util.ArrayList;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecentChat recentChat = new RecentChat();

        view = layoutInflater.inflate(R.layout.activity_recent_chat_list,null);
        recentChat.recentName = (TextView) view.findViewById(R.id.recentName);
        recentChat.recentText = (TextView) view.findViewById(R.id.recentText);
        recentChat.recentName.setText(recentChatLists.get(i).getUserName());
        recentChat.recentText.setText(recentChatLists.get(i).getRecentText());
        return view;
    }

    private class RecentChat {
        public TextView recentName;
        public TextView recentText;
    }
}
