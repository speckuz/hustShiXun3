package com.example.myapplication.moment;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.inf.Comment;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {


    ArrayList<Comment> comments;
    private LayoutInflater layoutInflater;
    private Activity activity;
    public CommentAdapter(ArrayList<Comment> comments, Context context,Activity activity) {
        this.comments = comments;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        view = layoutInflater.inflate(R.layout.adapter_item_moment_comment_list,null);
        holder.tvCommentName = (TextView) view.findViewById(R.id.tv_comment_name);
        holder.tvComment = (TextView) view.findViewById(R.id.tv_comment);
        holder.tvComment.setText(comments.get(i).getCommentText());
        if(comments.get(i).getCommentToIndex() == 0){
            holder.tvCommentName.setText(comments.get(i).getCommentUserId()+"");
        }else{
            holder.tvCommentName.setText(comments.get(i).getCommentUserId()+"回复"+comments.get(i).getCommentToIndex());
        }

        view.setOnClickListener(new View.OnClickListener() {
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
                            comments.add(new Comment(comments.size()+1,i+1,comments.size()+1,comment));
//                            moments.get(i).getComment().add(comment);
////                            moments.get(i).getCommentName().add("澳哥哥的小迷弟");
////                            System.out.println(i);
                            holder.dialog.dismiss();
                        }
                    }
                });
                Window window = holder.dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
            }
        });
        return view;
    }

    private class Holder {
        public TextView tvCommentName;
        public TextView tvComment;
        public CommentDialog dialog;
    }
}
