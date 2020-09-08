package adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import Inf.ChatRecord;
import Inf.Moment;

import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MomentAdapter extends BaseAdapter {

    private ArrayList<Moment> moments ;
    private LayoutInflater layoutInflater;
    public MomentAdapter(ArrayList<Moment> moments, Context context){
        this.moments = moments;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return moments.size();
    }

    @Override
    public Object getItem(int i) {
        return moments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        view = layoutInflater.inflate(R.layout.activity_moment,null);
        String userName = moments.get(i).getUserName();
        String content = moments.get(i).getContent();
        String time = moments.get(i).getTime();
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.userName = (TextView) view.findViewById(R.id.userName);
        holder.time = (TextView) view.findViewById(R.id.time);
        holder.content.setText(content);
        holder.userName.setText(userName);
        holder.time.setText(time);

        return view;
    }

    private class Holder {
        public TextView content;
        public TextView userName;
        public TextView time;

    }
}

