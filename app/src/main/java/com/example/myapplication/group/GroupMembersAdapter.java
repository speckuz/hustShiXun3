package com.example.myapplication.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.inf.Group;

public class GroupMembersAdapter extends BaseAdapter {
    Group group;
    private LayoutInflater layoutInflater;
    public GroupMembersAdapter(Group group, Context context){
        this.group = group;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return group.getMembersId().size();
    }

    @Override
    public Object getItem(int i) {
        return group.getMembersId().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        view = layoutInflater.inflate(R.layout.adapter_item_group_user_list,null);
        holder.ivGroupMembersHead = view.findViewById(R.id.iv_group_members_head);
        holder.tvGroupMembersName = view.findViewById(R.id.tv_members_name);
        holder.imgBtnDeleteMember = view.findViewById(R.id.img_btn_delete_member);
        holder.tvGroupMembersName.setText(group.getMembersId().get(i)+"");
        System.out.println(group.getMembersId().get(i) + group.getIdentity());
        if(! group.getIdentity().equals("FOUNDER")){
            holder.imgBtnDeleteMember.setVisibility(View.GONE);
        }else{
            holder.imgBtnDeleteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    group.getMembersId().remove(i);
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    private class Holder{
        private ImageView ivGroupMembersHead;
        private TextView tvGroupMembersName;
        private ImageButton imgBtnDeleteMember;
    }
}
