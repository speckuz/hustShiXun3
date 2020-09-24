package com.feiyueve.snsdemo.moment;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.inf.Comment;
import com.feiyueve.snsdemo.inf.Moment;

import java.util.ArrayList;

public class MomentAdapter extends BaseAdapter {

    private ArrayList<Moment> moments ;
    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public MomentAdapter(ArrayList<Moment> moments, Context context,Activity activity){
        this.moments = moments;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        view = layoutInflater.inflate(R.layout.adapter_item_moment,null);
        String userName = moments.get(i).getUserName();
        String content = moments.get(i).getContent();
        String time = moments.get(i).getTime();
        holder.tvContent = (TextView) view.findViewById(R.id.content);
        holder.tvUserName = (TextView) view.findViewById(R.id.userName);
        holder.tvTime = (TextView) view.findViewById(R.id.time);
        holder.imgBtnLike = (ImageButton) view.findViewById(R.id.like);
        holder.imgBtnComment = (ImageButton) view.findViewById(R.id.tv_comment);
        holder.lvCommentList = (ListView) view.findViewById(R.id.lv_comment_list);
        holder.tvLikeUserName = (TextView) view.findViewById(R.id.tv_moment_like_user_name);
        holder.tvContent.setText(content);
        holder.tvUserName.setText(userName);
        holder.tvTime.setText(time);
        ArrayList<String> likeUserName = moments.get(i).getLikeUserName();
        StringBuilder strB = new StringBuilder();
        if(likeUserName != null){
            for(int j = 0;j < likeUserName.size();j++){
                strB.append(likeUserName.get(j));
                holder.tvLikeUserName.setText(strB.toString());
            }
        }
        holder.adapter = new CommentAdapter(moments.get(i).getComments(),context,activity);
        holder.lvCommentList.setAdapter(holder.adapter);



        holder.imgBtnComment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                holder.dialog = new CommentDialog(activity);
                holder.dialog.setCanceledOnTouchOutside(true);
                holder.dialog.show();
                holder.dialog.imgBtnSendComment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String comment = holder.dialog.etCommentMessage.getText().toString();
                        if(comment != null){

                            moments.get(i).getComments().add(new Comment(moments.get(i).getComments().size()+1,0,moments.get(i).getComments().size()+1,comment));
                            System.out.println(i);
                            holder.dialog.dismiss();
                        }
                    }
                });
                Window window = holder.dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);

            }
        });

        holder.imgBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(moments.get(i).getLike()){
                    moments.get(i).setLike(false);
                    holder.imgBtnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moment_like_false));
                }else {
                    moments.get(i).setLike(true);
                    holder.imgBtnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moment_like_true));
                }
            }
        });

        if(moments.get(i).getLike()){
            holder.imgBtnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moment_like_true));
        }else{
            holder.imgBtnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moment_like_false));
        }

        return view;
    }

    private class Holder {
        public TextView tvContent;
        public TextView tvUserName;
        public TextView tvTime;
        public ImageButton imgBtnLike;
        public ImageButton imgBtnComment;
        public ListView lvCommentList;
        public TextView tvLikeUserName;
        public CommentDialog dialog;
        public ListAdapter adapter;
    }
}

