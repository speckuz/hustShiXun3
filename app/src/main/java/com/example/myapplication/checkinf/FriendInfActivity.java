package com.example.myapplication.checkinf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.chatlist.ChatListActivity;
import com.example.myapplication.inf.Comment;
import com.example.myapplication.inf.Friend;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.inf.FriendDetailedInf;
import com.example.myapplication.moment.CommentDialog;
import com.example.myapplication.moment.MyMomentActivity;
import com.example.myapplication.webService.FriendInfWebService;
import com.example.myapplication.webService.SceneryWebService;

public class FriendInfActivity extends AppCompatActivity {

    Button btnFriendSendMessage;
    Button btnFriendMoment;
    Button btnDeleteFriend;
    Button btnFriendAddFriend;
    ImageView ivFriendInfHead;
    TextView tvFriendInfAlias;
    TextView tvFriendInfUserName;
    TextView tvFriendInfSetAlias;
    TextView tvFriendInfBirthday;
    TextView tvFriendInfLocation;
    TextView tvFriendInfGender;
    TextView tvFriendInfSign;
    TextView tvFriendInfInterests;
    FriendDetailedInf friendDetailedInf;
    Activity activity;
    FriendInfWebService friendInfWebService;
    SceneryWebService sceneryWebService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_inf);
        initView();
    }
    void initView(){
        activity = this;
        //id = bundle.getString("id");
        //friend = new Friend("1","123","123");
        friendDetailedInf = (FriendDetailedInf)this.getIntent().getSerializableExtra("friendDetailedInf");
        btnFriendSendMessage = findViewById(R.id.btn_friend_send_message);
        btnFriendMoment = findViewById(R.id.btn_friend_moment);
        btnDeleteFriend = findViewById(R.id.btn_delete_friend);
        btnFriendAddFriend = findViewById(R.id.btn_friend_add_friend);
        ivFriendInfHead = findViewById(R.id.iv_friend_inf_head);
        tvFriendInfAlias = findViewById(R.id.tv_friend_inf_alias);
        tvFriendInfUserName = findViewById(R.id.tv_friend_inf_user_name);
        tvFriendInfSetAlias = findViewById(R.id.tv_friend_inf_set_alias);
        tvFriendInfBirthday = findViewById(R.id.tv_friend_inf_birthday);
        tvFriendInfLocation = findViewById(R.id.tv_friend_inf_location);
        tvFriendInfGender = findViewById(R.id.tv_friend_inf_gender);
        tvFriendInfSign = findViewById(R.id.tv_friend_inf_sign);
        tvFriendInfInterests = findViewById(R.id.tv_friend_inf_interests);

//        ivFriendInfHead.setImageResource();
        //初始化
        tvFriendInfAlias.setText(friendDetailedInf.getAlias() == null? friendDetailedInf.getUserName():friendDetailedInf.getAlias());
        tvFriendInfUserName.setText(friendDetailedInf.getUserName());
        if(friendDetailedInf.getBirthday() != null)
            tvFriendInfBirthday.setText(tvFriendInfBirthday.getText()+" "+friendDetailedInf.getBirthday().substring(0,10));
        if(friendDetailedInf.getLocation() != null)
            tvFriendInfLocation.setText(tvFriendInfLocation.getText()+" "+friendDetailedInf.getLocation());
        tvFriendInfGender.setText(tvFriendInfGender.getText()+" "+ (friendDetailedInf.isGender()?"女":"男"));
        if(friendDetailedInf.getSign() != null)
            tvFriendInfSign.setText(tvFriendInfSign.getText()+" "+friendDetailedInf.getSign());
        String interests = tvFriendInfInterests.getText()+" ";
        for(int i = 0;i < friendDetailedInf.getInterests().size();i++){
            interests = interests+" "+friendDetailedInf.getInterests().get(i);
        }
        tvFriendInfInterests.setText(interests);

        if(friendDetailedInf.isFriend()){
            btnFriendAddFriend.setVisibility(View.GONE);
        }else{
            tvFriendInfSetAlias.setVisibility(View.GONE);
            btnDeleteFriend.setVisibility(View.GONE);
            btnFriendSendMessage.setVisibility(View.GONE);
        }
        //设置备注
        tvFriendInfSetAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommentDialog dialog = new CommentDialog(activity);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.imgBtnSendComment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        final String newAlias = dialog.etCommentMessage.getText().toString().trim();
                        if(newAlias != null){
                            System.out.println(newAlias);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(friendInfWebService == null)
                                        friendInfWebService = new FriendInfWebService();
                                    //friendInfWebService.change(friendDetailedInf.getUserId(),newAlias,friendDetailedInf);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                }
                            }).start();
//                            moments.get(i).getComment().add(comment);
////                            moments.get(i).getCommentName().add("澳哥哥的小迷弟");
////                            System.out.println(i);
                            dialog.dismiss();
                        }
                    }
                });
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
            }
        });
        //给好友发消息
        btnFriendSendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendInfActivity.this, ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",friendDetailedInf.getUserId()+"");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //删除好友
        btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(friendInfWebService == null)
                            friendInfWebService = new FriendInfWebService();
                        //friendInfWebService.change(friendDetailedInf.getUserId(),newAlias,friendDetailedInf);
                        friendInfWebService.deleteFriend(friendDetailedInf.getUserId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }).start();
            }
        });
        //查看朋友圈
        btnFriendMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //friendInfWebService.change(friendDetailedInf.getUserId(),newAlias,friendDetailedInf);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(FriendInfActivity.this, MyMomentActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", friendDetailedInf.getUserId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}