package com.example.myapplication.group;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.inf.Group;
import com.example.myapplication.moment.CommentDialog;
import com.example.myapplication.webService.GroupWebService;

public class GroupInfActivity extends AppCompatActivity {

    private ImageView ivGroupDetailHead;
    private TextView tvGroupDetailName;
    private TextView tvGroupDetailNum;
    private ImageView ivGroupFriend1Head;
    private ImageView ivGroupFriend2Head;
    private ImageView ivGroupFriend3Head;
    private ImageView ivGroupFriend4Head;
    private TextView tvGroupFriend1Name;
    private TextView tvGroupFriend2Name;
    private TextView tvGroupFriend3Name;
    private TextView tvGroupFriend4Name;
    private ImageView ivGroupInvent;
    private LinearLayout llGroupShowMembers;
    private LinearLayout llGroupFounder;
    private LinearLayout llGroupMember;
    private LinearLayout llGroupUnknown;
    private Button btnAddGroup;
    private Button btnDismissGroup;
    private Button btnExitGroup;
    private Button btnFounderSendMessage;
    private Button btnMemberSendMessage;
    private LinearLayout llFounderUpdateGroupName;
    private LinearLayout llMemberUpdateMemberName;
    private LinearLayout llMemberUpdateGroupName;
    private LinearLayout llFounderUpdateFounderName;
    private TextView tvMemberGroupAlias;
    private TextView tvFounderAlias;
    private TextView tvMemberAlias;
    private GroupWebService groupWebService;
    private Group group;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_inf);
        activity = this;
        group = (Group)this.getIntent().getSerializableExtra("group");

        ivGroupDetailHead = findViewById(R.id.iv_group_detail_head);
        tvGroupDetailName = findViewById(R.id.tv_group_detail_name);
        tvGroupDetailNum = findViewById(R.id.tv_group_detail_num);
        ivGroupFriend1Head = findViewById(R.id.iv_group_friend1_head);
        ivGroupFriend2Head = findViewById(R.id.iv_group_friend2_head);
        ivGroupFriend3Head = findViewById(R.id.iv_group_friend3_head);
        ivGroupFriend4Head = findViewById(R.id.iv_group_friend4_head);
        tvGroupFriend1Name = findViewById(R.id.tv_group_friend1_name);
        tvGroupFriend2Name = findViewById(R.id.tv_group_friend2_name);
        tvGroupFriend3Name = findViewById(R.id.tv_group_friend3_name);
        tvGroupFriend4Name = findViewById(R.id.tv_group_friend4_name);
        ivGroupInvent = findViewById(R.id.iv_group_invent);
        llGroupShowMembers = findViewById(R.id.ll_group_show_members);
        llGroupFounder = findViewById(R.id.ll_group_founder);
        llGroupMember = findViewById(R.id.ll_group_member);
        llGroupUnknown = findViewById(R.id.ll_group_unknown);
        btnFounderSendMessage = findViewById(R.id.btn_founder_send_message);
        btnMemberSendMessage = findViewById(R.id.btn_member_send_message);
        btnAddGroup = findViewById(R.id.btn_add_group);
        btnDismissGroup = findViewById(R.id.btn_dismiss_group);
        btnExitGroup = findViewById(R.id.btn_exit_group);
        llFounderUpdateGroupName = findViewById(R.id.ll_founder_update_group_name);
        llMemberUpdateGroupName = findViewById(R.id.ll_member_update_group_name);
        llMemberUpdateMemberName = findViewById(R.id.ll_member_update_member_name);
        llFounderUpdateFounderName = findViewById(R.id.ll_founder_update_founder_name);
        tvMemberGroupAlias = findViewById(R.id.tv_member_group_alias);
        tvFounderAlias = findViewById(R.id.tv_founder_alias);
        tvMemberAlias = findViewById(R.id.tv_member_alias);


        //ivGroupDetailHead.setImageResource(R.drawable.sns);
        tvGroupDetailName.setText(group.getGroupName());
        tvMemberAlias.setText(group.getMyGroupAlias());
        tvMemberGroupAlias.setText(group.getGroupAlias());
        tvFounderAlias.setText(group.getMyGroupAlias());
//        ivGroupFriend1Head.setImageResource();
//        ivGroupFriend2Head.setImageResource();
//        ivGroupFriend3Head.setImageResource();
//        ivGroupFriend4Head.setImageResource();
//        tvGroupFriend1Name.setText();
//        tvGroupFriend2Name.setText();
//        tvGroupFriend3Name.setText();
//        tvGroupFriend4Name.setText();

        tvGroupDetailNum.setText("共"+(group.getMembersNum()+1)+"人");
        tvGroupFriend1Name.setText(group.getFounderId()+"");
        if(group.getMembersNum() == 1){
            tvGroupFriend2Name.setText(group.getMembersId().get(0)+"");
            ivGroupFriend3Head.setVisibility(View.INVISIBLE);
            ivGroupFriend4Head.setVisibility(View.INVISIBLE);
            tvGroupFriend3Name.setVisibility(View.INVISIBLE);
            tvGroupFriend4Name.setVisibility(View.INVISIBLE);
        }else if(group.getMembersNum() == 2){
            tvGroupFriend2Name.setText(group.getMembersId().get(0)+"");
            tvGroupFriend3Name.setText(group.getMembersId().get(1)+"");
            tvGroupFriend4Name.setVisibility(View.INVISIBLE);
            ivGroupFriend4Head.setVisibility(View.INVISIBLE);
        }else if(group.getMembersNum() == 0){
            ivGroupFriend2Head.setVisibility(View.INVISIBLE);
            ivGroupFriend3Head.setVisibility(View.INVISIBLE);
            ivGroupFriend4Head.setVisibility(View.INVISIBLE);
            tvGroupFriend2Name.setVisibility(View.INVISIBLE);
            tvGroupFriend3Name.setVisibility(View.INVISIBLE);
            tvGroupFriend4Name.setVisibility(View.INVISIBLE);
        }else{
            tvGroupFriend2Name.setText(group.getMembersId().get(0)+"");
            tvGroupFriend3Name.setText(group.getMembersId().get(1)+"");
            tvGroupFriend4Name.setText(group.getMembersId().get(2)+"");
        }

        if(group.getIdentity().equals("FOUNDER")){
            llGroupMember.setVisibility(View.GONE);
            llGroupUnknown.setVisibility(View.GONE);
        }else if(group.getIdentity().equals("MEMBER")){
            llGroupFounder.setVisibility(View.GONE);
            llGroupUnknown.setVisibility(View.GONE);
        }else if(group.getIdentity().equals("UNKNOWN")){
            llGroupFounder.setVisibility(View.GONE);
            llGroupMember.setVisibility(View.GONE);

        }

        llGroupShowMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfActivity.this, GroupMembersList.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("group", group);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //群成员发送消息
        btnMemberSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //群主发送消息
        btnFounderSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //退出群聊
        btnExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(groupWebService == null)
                            groupWebService = new GroupWebService();
                        groupWebService.escapeGroup(group.getGroupId());
                    }
                }).start();
            }
        });
        //解散群聊
        btnDismissGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(groupWebService == null)
                            groupWebService = new GroupWebService();
                        groupWebService.deleteGroup(group.getGroupId());
                    }
                }).start();
                finish();
            }
        });
        //加入群聊
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(groupWebService == null)
                            groupWebService = new GroupWebService();
                        groupWebService.addGroup(group.getGroupId());
                    }
                }).start();
            }
        });
        //成员修改自己的备注
        llMemberUpdateMemberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommentDialog dialog = new CommentDialog(activity);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.imgBtnSendComment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        final String comment = dialog.etCommentMessage.getText().toString();
                        if(comment != null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(groupWebService == null)
                                        groupWebService = new GroupWebService();

//                                    sceneryWebService.comment(momentId,comment,comments.get(i).getCommentIndex());
//                                    ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
//                                    moments.clear();
//                                    moments.addAll(momentsTemp);
//                                    activity.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    });
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    }
                });
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
            }
        });
        //成员修改群备注
        llMemberUpdateGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommentDialog dialog = new CommentDialog(activity);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.imgBtnSendComment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        final String newGroupName = dialog.etCommentMessage.getText().toString();
                        if(newGroupName != null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(groupWebService == null)
                                        groupWebService = new GroupWebService();

//                                    sceneryWebService.comment(momentId,comment,comments.get(i).getCommentIndex());
//                                    ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
//                                    moments.clear();
//                                    moments.addAll(momentsTemp);
//                                    activity.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    });
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    }
                });
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
            }
        });
        //群主修改自己的备注
        llFounderUpdateFounderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommentDialog dialog = new CommentDialog(activity);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.imgBtnSendComment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        final String newGroupName = dialog.etCommentMessage.getText().toString();
                        if(newGroupName != null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(groupWebService == null)
                                        groupWebService = new GroupWebService();

//                                    sceneryWebService.comment(momentId,comment,comments.get(i).getCommentIndex());
//                                    ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
//                                    moments.clear();
//                                    moments.addAll(momentsTemp);
//                                    activity.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    });
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    }
                });
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
            }
        });
        //群主修改群组名字
        llFounderUpdateGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommentDialog dialog = new CommentDialog(activity);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.imgBtnSendComment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        final String newGroupName = dialog.etCommentMessage.getText().toString();
                        if(newGroupName != null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(groupWebService == null)
                                        groupWebService = new GroupWebService();
                                    groupWebService.updateName(group.getGroupId(),newGroupName);

//                                    sceneryWebService.comment(momentId,comment,comments.get(i).getCommentIndex());
//                                    ArrayList<Moment> momentsTemp = sceneryWebService.getWorldScenery(1+"",2);
//                                    moments.clear();
//                                    moments.addAll(momentsTemp);
//                                    activity.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    });
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    }
                });
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
            }
        });
        //邀请好友
        ivGroupInvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}