package com.example.myapplication.others;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.checkinf.GroupInfActivity;
import com.example.myapplication.friendlist.FriendListActivity;
import com.example.myapplication.inf.FriendDetailedInf;

import com.example.myapplication.inf.Group;
import com.example.myapplication.moment.MyMomentActivity;
import com.example.myapplication.webService.OkHttp;

public class AddFriendOrGroupActivity extends AppCompatActivity {

    ImageButton imgBtnSearchFriend;
    ImageButton imgBtnSearchGroup;
    OkHttp okHttp;
    EditText etGroupName;
    EditText etFriendName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_or_group);
        initView();
    }
    public void initView(){
        imgBtnSearchFriend = findViewById(R.id.img_btn_search_friend);
        imgBtnSearchGroup = findViewById(R.id.img_btn_search_group);
        etGroupName = findViewById(R.id.et_search_group_name);
        etFriendName = findViewById(R.id.et_search_friend_name);

        okHttp = new OkHttp();
        imgBtnSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String searchFriend = etFriendName.getText().toString();
                        FriendDetailedInf friendDetailedInf = okHttp.searchFriend(searchFriend);
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

                        }
                    }
                }).start();

            }
        });


        imgBtnSearchGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchGroup = etGroupName.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Group group = okHttp.search_group(searchGroup,1);
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
        });
    }
}