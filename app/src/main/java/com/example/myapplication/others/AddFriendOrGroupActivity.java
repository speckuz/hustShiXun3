package com.example.myapplication.others;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.checkinf.FriendInfActivity;
import com.example.myapplication.group.GroupInfActivity;
import com.example.myapplication.inf.FriendDetailedInf;

import com.example.myapplication.inf.Group;
import com.example.myapplication.moment.CommentDialog;
import com.example.myapplication.webService.FriendInfWebService;
import com.example.myapplication.webService.GroupWebService;

import java.util.ArrayList;

public class AddFriendOrGroupActivity extends AppCompatActivity {

    ImageButton imgBtnSearchFriend;
    ImageButton imgBtnSearchGroup;
    ImageButton imgBtnNewGroup;
    ImageButton imgRefreshCommend;
    GroupWebService groupWebService;
    EditText etGroupName;
    EditText etFriendName;
    EditText etNewGroupName;
    LinearLayout llClicked1;
    ImageView ivFriend1RecommendHead;
    TextView tvFriend1RecommendName;
    TextView tvFriend1RecommendSign;
    TextView tvCreateGroup;
    LinearLayout llClicked2;
    ImageView ivFriend2RecommendHead;
    TextView tvFriend2RecommendName;
    TextView tvFriend2RecommendSign;
    LinearLayout llClicked3;
    ImageView ivFriend3RecommendHead;
    TextView tvFriend3RecommendName;
    TextView tvFriend3RecommendSign;
    TextView tvNewCommendFriend;
    FriendInfWebService friendInfWebService;
    ArrayList<FriendDetailedInf> friendRecommendList;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_or_group);
        initView();
    }
    public void initView(){
        imgBtnSearchFriend = findViewById(R.id.img_btn_search_friend);
        imgBtnSearchGroup = findViewById(R.id.img_btn_search_group);
        imgBtnNewGroup = findViewById(R.id.img_btn_new_group);
        imgRefreshCommend = findViewById(R.id.img_btn_refresh_commend);
        etGroupName = findViewById(R.id.et_search_group_name);
        etFriendName = findViewById(R.id.et_search_friend_name);
        etNewGroupName = findViewById(R.id.et_new_group_name);
        tvCreateGroup = findViewById(R.id.tv_create_group);
        llClicked1 = findViewById(R.id.ll_clicked1);
        tvNewCommendFriend = findViewById(R.id.tv_recommend_new_friend);
        ivFriend1RecommendHead = findViewById(R.id.iv_friend1_recommend_head);
        tvFriend1RecommendName = findViewById(R.id.tv_friend1_recommend_name);
        tvFriend1RecommendSign = findViewById(R.id.tv_friend1_recommend_sign);
        llClicked2 = findViewById(R.id.ll_clicked2);
        ivFriend2RecommendHead = findViewById(R.id.iv_friend2_recommend_head);
        tvFriend2RecommendName = findViewById(R.id.tv_friend2_recommend_name);
        tvFriend2RecommendSign = findViewById(R.id.tv_friend2_recommend_sign);
        llClicked3 = findViewById(R.id.ll_clicked3);
        ivFriend3RecommendHead = findViewById(R.id.iv_friend3_recommend_head);
        tvFriend3RecommendName = findViewById(R.id.tv_friend3_recommend_name);
        tvFriend3RecommendSign = findViewById(R.id.tv_friend3_recommend_sign);
        llClicked1.setVisibility(View.GONE);
        llClicked2.setVisibility(View.GONE);
        llClicked3.setVisibility(View.GONE);
        this.activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                friendInfWebService = new FriendInfWebService();
                friendRecommendList = friendInfWebService.getRecommendFriendList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecommendFriendList();
                    }
                });
            }
        }).start();
        imgRefreshCommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(friendInfWebService == null)
                            friendInfWebService = new FriendInfWebService();
                        friendRecommendList = friendInfWebService.getRecommendFriendList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecommendFriendList();
                            }
                        });
                    }
                }).start();
            }
        });
        imgBtnSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchFriend = etFriendName.getText().toString();
                if(searchFriend.trim().equals("")){
                    Toast.makeText(getApplicationContext(),"请输入用户ID", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(friendInfWebService == null)
                                friendInfWebService = new FriendInfWebService();
                            FriendDetailedInf friendDetailedInf = friendInfWebService.search(searchFriend);
                            if(friendDetailedInf == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"该用户不存在", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"该用户存在", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent intent = new Intent(AddFriendOrGroupActivity.this, FriendInfActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("friendDetailedInf", friendDetailedInf);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    }).start();
                }


            }
        });

        imgBtnNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etNewGroupName.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"请输入创建新群组的名字", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(groupWebService == null)
                                groupWebService = new GroupWebService();
                            final String newGroupName = etNewGroupName.getText().toString();
                            int newGroupId = groupWebService.createGroup(newGroupName);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"创建群聊"+newGroupName+"成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Group group = groupWebService.searchGroup(newGroupId+"");
                            Intent intent = new Intent(AddFriendOrGroupActivity.this, GroupInfActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("group", group);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }).start();
                }

            }
        });

        imgBtnSearchGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchGroup = etGroupName.getText().toString();

                if(searchGroup.trim().equals("")){
                    Toast.makeText(getApplicationContext(),"请输入群组ID", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(groupWebService == null)
                                groupWebService = new GroupWebService();
                            Group group = groupWebService.searchGroup(searchGroup);
                            if(group == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"该群组不存在", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Intent intent = new Intent(AddFriendOrGroupActivity.this, GroupInfActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("group", group);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    }).start();
                }

            }
        });
    }
    public void initRecommendFriendList(){
        llClicked1.setVisibility(View.VISIBLE);
        llClicked2.setVisibility(View.VISIBLE);
        llClicked3.setVisibility(View.VISIBLE);
        final FriendDetailedInf friendDetailedInf1 = friendRecommendList.get(0);
        final FriendDetailedInf friendDetailedInf2 = friendRecommendList.get(1);
        final FriendDetailedInf friendDetailedInf3 = friendRecommendList.get(2);
        llClicked1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddFriendOrGroupActivity.this, FriendInfActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("friendDetailedInf", friendDetailedInf1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvFriend1RecommendName.setText(friendDetailedInf1.getAlias());
        tvFriend1RecommendSign.setText(friendDetailedInf1.getSign());

        llClicked2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddFriendOrGroupActivity.this, FriendInfActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("friendDetailedInf", friendDetailedInf2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvFriend2RecommendName.setText(friendDetailedInf2.getAlias());
        tvFriend2RecommendSign.setText(friendDetailedInf2.getSign());

        llClicked3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddFriendOrGroupActivity.this, FriendInfActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("friendDetailedInf", friendDetailedInf3);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvFriend3RecommendName.setText(friendDetailedInf3.getAlias());
        tvFriend3RecommendSign.setText(friendDetailedInf3.getSign());

    }
}