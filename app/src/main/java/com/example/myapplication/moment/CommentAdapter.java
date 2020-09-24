package com.example.myapplication.moment;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.inf.Comment;
import com.example.myapplication.inf.Moment;
import com.example.myapplication.webService.SceneryWebService;

import java.util.ArrayList;

import kotlin.reflect.KVisibility;

public class CommentAdapter extends BaseAdapter {


    private String momentId;
    private ArrayList<Moment> moments ;
    private ArrayList<Comment> comments;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private SceneryWebService sceneryWebService;
    private MomentAdapter adapter;
    public CommentAdapter(String momentId,ArrayList<Moment> moments,ArrayList<Comment> comments, Context context,Activity activity,MomentAdapter adapter) {
        this.momentId = momentId;
        this.moments = moments;
        this.comments = comments;
        this.layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
        this.adapter = adapter;

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
        holder.tvComment = (TextView) view.findViewById(R.id.tv_comment);
        holder.imgBtnDeleteComment = (ImageButton) view.findViewById(R.id.img_btn_delete_comment);
        String comment = new String();
        if(comments.get(i).getCommentToIndex() == 0){
            comment = comments.get(i).getCommentUserName()+":  ";
        }else{
            comment = comments.get(i).getCommentUserName()+" 回复 "+comments.get(i).getCommentToUserName()+":  ";
        }
        comment = comment + comments.get(i).getCommentText();
        holder.tvComment.setText(comment);
        if(comments.get(i).getCommentUserId() == 2){
            //holder.imgBtnDeleteComment.setVisibility(View.VISIBLE);
            holder.imgBtnDeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sceneryWebService = new SceneryWebService();
                            sceneryWebService.deleteComment(momentId,comments.get(i).getCommentIndex());
                            ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
                            moments.clear();
                            moments.addAll(momentsTemp);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                }
            });
        }else {
            holder.imgBtnDeleteComment.setVisibility(View.GONE);
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
                        final String comment = holder.dialog.etCommentMessage.getText().toString();
                        if(comment != null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    sceneryWebService = new SceneryWebService();
                                    System.out.println(comments.get(i).getCommentIndex());
                                    sceneryWebService.comment(momentId,comment,comments.get(i).getCommentIndex());
                                    ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
                                    moments.clear();
                                    moments.addAll(momentsTemp);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).start();
                            //comments.add(new Comment(comments.size()+1,i+1,comments.size()+1,comment));
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
        public TextView tvComment;
        public CommentDialog dialog;
        public ImageButton imgBtnDeleteComment;
    }
}
