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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.inf.Moment;

import com.example.myapplication.R;
import com.example.myapplication.webService.SceneryWebService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MomentAdapter extends BaseAdapter {

    private ArrayList<Moment> moments ;
    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private SceneryWebService sceneryWebService;

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
        holder.imgBtnComment = (ImageButton) view.findViewById(R.id.img_btn_comment_moment);
        holder.lvCommentList = (ListView) view.findViewById(R.id.lv_comment_list);
        holder.tvLikeUserName = (TextView) view.findViewById(R.id.tv_moment_like_user_name);
        holder.imgBtnDelete = (ImageButton) view.findViewById(R.id.img_btn_delete_moment);
        holder.tvContent.setText(content);
        holder.tvUserName.setText(userName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            Date date = simpleDateFormat.parse(time);
            SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String bjDate = bjSdf.format(date);
            holder.tvTime.setText(bjDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<String> likeUserName = moments.get(i).getLikeUserName();
        StringBuilder strB = new StringBuilder();
        if(likeUserName != null){
            for(int j = 0;j < likeUserName.size();j++){
                strB.append(likeUserName.get(j));
            }
        }
        holder.tvLikeUserName.setText(strB.toString());
        holder.adapter = new CommentAdapter(moments.get(i).getMomentId(),moments,moments.get(i).getComments(),context,activity,this);
        holder.lvCommentList.setAdapter(holder.adapter);


        if(moments.get(i).getUserId() == 2){
            holder.imgBtnDelete.setVisibility(View.VISIBLE);
            holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sceneryWebService = new SceneryWebService();
                            sceneryWebService.deleteScenery(moments.get(i).getMomentId());
                            moments = sceneryWebService.getWorldScenery(1+"",2);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });

                        }
                    }).start();
                }
            });
        }


        holder.imgBtnComment.setOnClickListener(new View.OnClickListener(){

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
                                    sceneryWebService.comment(moments.get(i).getMomentId(),comment,0);
                                    moments = sceneryWebService.getWorldScenery(1+"",2);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });

                                }
                            }).start();


                            //moments.get(i).getComments().add(new Comment(moments.get(i).getComments().size()+1,0,moments.get(i).getComments().size()+1,comment));
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(sceneryWebService == null)
                                sceneryWebService = new SceneryWebService();
                            sceneryWebService.dislikeMoment(moments.get(i).getMomentId());
                            ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
                            moments.clear();
                            moments.addAll(momentsTemp);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                    holder.imgBtnLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moment_like_false));

                }else {
                    moments.get(i).setLike(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(sceneryWebService == null)
                                sceneryWebService = new SceneryWebService();
                            sceneryWebService.likeMoment(moments.get(i).getMomentId());
                            ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
                            moments.clear();
                            moments.addAll(momentsTemp);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            });

                        }
                    }).start();
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
        public ImageButton imgBtnDelete;
    }
    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sceneryWebService = new SceneryWebService();
                moments = sceneryWebService.getWorldScenery(1+"",2);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }
}

