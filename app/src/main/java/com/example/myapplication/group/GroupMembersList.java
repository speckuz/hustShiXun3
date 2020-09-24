package com.example.myapplication.group;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.group.GroupMembersAdapter;
import com.example.myapplication.inf.Group;

public class GroupMembersList extends AppCompatActivity {


    ImageView ivGroupOwnerHead;
    TextView tvGroupOwnerName;
    ListView lvGroupMembersList;
    GroupMembersAdapter groupMembersAdapter;
    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_user_list);
        group = (Group)this.getIntent().getSerializableExtra("group");
        ivGroupOwnerHead = findViewById(R.id.iv_group_owner_head);
        tvGroupOwnerName = findViewById(R.id.tv_group_owner_name);
        lvGroupMembersList = findViewById(R.id.lv_group_members_list);
        for(int i = 0;i < group.getMembersId().size();i++){
            System.out.println(group.getMembersId().get(i));
        }
        groupMembersAdapter = new GroupMembersAdapter(group,this);
        lvGroupMembersList.setAdapter(groupMembersAdapter);

    }
}
