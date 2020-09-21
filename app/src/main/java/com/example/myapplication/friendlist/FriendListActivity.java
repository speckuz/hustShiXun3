package com.example.myapplication.friendlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.myapplication.chatlist.ChatListActivity;
import com.example.myapplication.inf.Friend;

import com.example.myapplication.inf.Group;
import com.example.myapplication.others.AddFriendOrGroupActivity;
import com.example.myapplication.checkinf.MineActivity;
import com.example.myapplication.R;
import com.example.myapplication.moment.MyMomentActivity;

public class FriendListActivity extends AppCompatActivity {

    private ImageButton imgBtnProfilePicture;
    private ImageButton imgBtnToChatList;
    private ImageButton imgBtnToMoment;
    private ImageButton imgBtnAddFriendOrGroup;
    private TextView tvFriendByGroupShow;
    private TextView tvFriendShow;
    private TextView tvGroupShow;
    private ArrayList<String> groupName;
    private ArrayList<String> groupName1;
    private ArrayList<ArrayList<Friend>> friendLists;
    private ArrayList<ArrayList<Group>> groupLists;
    private CustomFriendExpandableListView lvFriendByGroupList;
    private ListView lvFriendList;
    private CustomGroupExpandableListView lvGroupList;
    private BaseExpandableListAdapter friendBaseExpandableListAdapter;
    private BaseExpandableListAdapter groupBaseExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        initView();

    }
    public void initView(){
        imgBtnProfilePicture = (ImageButton) findViewById(R.id.img_btn_friend_profile_picture);
        imgBtnToChatList = (ImageButton) findViewById(R.id.img_btn_friend_to_chat_list);
        imgBtnToMoment = (ImageButton) findViewById(R.id.img_btn_friend_to_moment);
        tvFriendByGroupShow = (TextView) findViewById(R.id.tv_friend_by_group_show);
        tvFriendShow = (TextView)findViewById(R.id.tv_friend_show);
        tvGroupShow = (TextView)findViewById(R.id.tv_group_show);
        lvFriendByGroupList = (CustomFriendExpandableListView) findViewById(R.id.lv_friend_by_group_list);
        lvFriendList = (ListView) findViewById(R.id.lv_friend_list);
        lvGroupList = (CustomGroupExpandableListView) findViewById(R.id.lv_group_list);
        imgBtnAddFriendOrGroup = (ImageButton) findViewById(R.id.img_btn_friend_add_friend_or_group);
        imgBtnProfilePicture = (ImageButton) findViewById(R.id.img_btn_friend_profile_picture);

        friendLists = new ArrayList<ArrayList<Friend>>();

        groupName = new ArrayList<String>();
        groupName.add("leo的好友");
        ArrayList<Friend> friendList1 = new ArrayList<Friend>();
        friendList1.add(new Friend("123","11","22"));
        friendList1.add(new Friend("456","1122","232"));

        groupName.add("leo的同学");
        ArrayList<Friend> friendList2 = new ArrayList<Friend>();
        friendList2.add(new Friend("123","11","22"));
        friendList2.add(new Friend("456","1122","232"));

        friendLists.add(friendList1);
        friendLists.add(friendList2);
        friendBaseExpandableListAdapter = new FriendByGroupListAdapter(friendLists,this,groupName);
        lvFriendByGroupList.setAdapter(friendBaseExpandableListAdapter);

        groupLists =  new ArrayList<ArrayList<Group>>();
        groupName1 = new ArrayList<String>();
        groupName1.add("leo男朋友交流群");
//        ArrayList<Group> groupList1 = new ArrayList<Group>();
//        groupList1.add(new Group(1,"111",1,));
//        groupList1.add(new Group(2,"121"));
//
//        groupName1.add("leo女朋友交流群");
//        ArrayList<Group> groupList2 = new ArrayList<Group>();
//        groupList2.add(new Group(3,"131"));
//        groupList2.add(new Group(4,"141"));
//        groupLists.add(groupList1);
//        groupLists.add(groupList2);
        groupBaseExpandableListAdapter = new GroupListAdapter(groupLists,this,groupName1);
        lvGroupList.setAdapter(groupBaseExpandableListAdapter);

//        dadelvlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
//
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                Intent intent = new Intent(FriendList.this, FriendInf.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("id", friendLists.get(i).get(i1).getId());
//                intent.putExtras(bundle);
//                startActivity(intent);
//                return true;
//            }
//        });
        initListener();
        initListener1();

        tvFriendShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvFriendByGroupList.setVisibility(View.GONE);
                lvGroupList.setVisibility(View.GONE);
                lvFriendList.setVisibility(View.VISIBLE);
                tvFriendByGroupShow.setBackground(getResources().getDrawable(R.color.colorAlpha));
                tvGroupShow.setBackground(getResources().getDrawable(R.color.colorAlpha));
                tvFriendShow.setBackground(getResources().getDrawable(R.drawable.shape_friend_list_showing));
                tvFriendShow.setTextColor(getResources().getColor(R.color.black));
                tvGroupShow.setTextColor(getResources().getColor(R.color.gray));
                tvFriendByGroupShow.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        tvGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvFriendByGroupList.setVisibility(View.GONE);
                lvGroupList.setVisibility(View.VISIBLE);
                lvFriendList.setVisibility(View.GONE);
                tvFriendByGroupShow.setBackground(getResources().getDrawable(R.color.colorAlpha));
                tvGroupShow.setBackground(getResources().getDrawable(R.drawable.shape_friend_list_showing));
                tvFriendShow.setBackground(getResources().getDrawable(R.color.colorAlpha));
                tvFriendShow.setTextColor(getResources().getColor(R.color.gray));
                tvGroupShow.setTextColor(getResources().getColor(R.color.black));
                tvFriendByGroupShow.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        tvFriendByGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvFriendByGroupList.setVisibility(View.VISIBLE);
                lvGroupList.setVisibility(View.GONE);
                lvFriendList.setVisibility(View.GONE);
                tvFriendByGroupShow.setBackground(getResources().getDrawable(R.drawable.shape_friend_list_showing));
                tvGroupShow.setBackground(getResources().getDrawable(R.color.colorAlpha));
                tvFriendShow.setBackground(getResources().getDrawable(R.color.colorAlpha));
                tvFriendShow.setTextColor(getResources().getColor(R.color.gray));
                tvGroupShow.setTextColor(getResources().getColor(R.color.gray));
                tvFriendByGroupShow.setTextColor(getResources().getColor(R.color.black));
            }
        });

        imgBtnProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendListActivity.this, MineActivity.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });


        imgBtnAddFriendOrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendListActivity.this, AddFriendOrGroupActivity.class));
            }
        });

        imgBtnToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( FriendListActivity.this, ChatListActivity.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
            }
        });

        imgBtnToMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendListActivity.this, MyMomentActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", "mine");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        });

    }



    public void initListener() {
        lvFriendByGroupList.setFastScrollEnabled(true);
        lvFriendByGroupList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //ExpandableListView 的回调函数
        lvFriendByGroupList.setFriendLists(friendLists);
        lvFriendByGroupList.setDragNDropListener(new CustomDragAndDropListener() {
            public void onStartDrag(View itemView) {
            }
            public void onDrag(int x, int y, ListView listView) {
            }

            public void onStopDrag(View itemView) {
            }

            public void onDrop(int flatPosFrom, int flatPosTo) {
                final boolean fromIsGroup = ExpandableListView.getPackedPositionType(lvFriendByGroupList.getExpandableListPosition(flatPosFrom)) == ExpandableListView.PACKED_POSITION_TYPE_GROUP;// 判断移动的是不是组
                final boolean toIsGroup = ExpandableListView.getPackedPositionType(lvFriendByGroupList.getExpandableListPosition(flatPosTo)) == ExpandableListView.PACKED_POSITION_TYPE_GROUP;// 判断是否移动到分组上

                final long packedPosTo = lvFriendByGroupList.getExpandableListPosition(flatPosTo);
                final long packedPosFrom = lvFriendByGroupList.getExpandableListPosition(flatPosFrom);
                final int packedGroupPosTo = ExpandableListView.getPackedPositionGroup(packedPosTo);
                final int packedGroupPosForm = ExpandableListView.getPackedPositionGroup(packedPosFrom);

                if ((!fromIsGroup) && toIsGroup) {// 如果移动的不是组，并且是移动到组上
                    long itemId = lvFriendByGroupList.getItemIdAtPosition(flatPosFrom);
                    long position = lvFriendByGroupList.getItemIdAtPosition(flatPosTo);

                    Friend friend = (Friend) friendLists.get(((int) packedGroupPosForm)).get((int) itemId);
                    ArrayList<Friend> friendArrayList1 = (ArrayList<Friend>) friendLists.get((int) packedGroupPosForm);
                    friendLists.get((int) packedGroupPosForm).remove(friend);

                    ArrayList<Friend> friendArrayList2 = (ArrayList<Friend>) friendLists.get((int) packedGroupPosForm);
                    (friendLists.get(((int) packedGroupPosTo))).add(friend);
                    friendBaseExpandableListAdapter.notifyDataSetChanged();
                    lvFriendByGroupList.setFriendLists(friendLists);

//                    View toView = dadelvlist.getChildAt(flatPosTo - dadelvlist.getFirstVisiblePosition());
//                    TextView groupCodeView = (TextView) toView.findViewById(R.id.tvGroupCode);
//                    re_group_groupCode = groupCodeView.getText().toString(); // 移动Item时，移动到组的编码
//                    View fromView = dadelvlist.getChildAt(flatPosFrom - dadelvlist.getFirstVisiblePosition());  // 要移动的设备View
//                    TextView tvMachineView = (TextView) fromView.findViewById(R.id.child_text3);
//                    re_group_machineCode = tvMachineView.getText().toString();  // 移动Item时，该条Item的编码

//                    HashMap<String, String> tmp = (HashMap<String, String>) childs.get(((int) packedGroupPosForm)).get((int) itemId);// 移动的Item项
//                    List<Map<String, String>> tmpList = (List<Map<String, String>>) childs.get((int) packedGroupPosForm);
//                    childs.get((int) packedGroupPosForm).remove(tmp); //  把该 条数据从当前组中进行删除
//
//                    List<Map<String, String>> tmp1 = (List<Map<String, String>>) childs.get(((int) packedGroupPosTo));  // 要移动到分组中的列表
//                    (childs.get(((int) packedGroupPosTo))).add(tmp); //把该 条数据添加到移动之后的组中
//                    adapter.notifyDataSetChanged();
//
//                    View toView = dadelvlist.getChildAt(flatPosTo - dadelvlist.getFirstVisiblePosition()); //获取移动到分组的View
//                    TextView groupCodeView = (TextView) toView.findViewById(R.id.tvGroupCode);
//                    re_group_groupCode = groupCodeView.getText().toString(); // 移动Item时，移动到组的编码
//                    View fromView = dadelvlist.getChildAt(flatPosFrom - dadelvlist.getFirstVisiblePosition());  // 要移动的设备View
//                    TextView tvMachineView = (TextView) fromView.findViewById(R.id.child_text3);
//                    re_group_machineCode = tvMachineView.getText().toString();  // 移动Item时，该条Item的编码

                }
            }
        });
    }


    public void initListener1() {
        lvGroupList.setFastScrollEnabled(true);
        lvGroupList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //ExpandableListView 的回调函数
        lvGroupList.setGroupLists(groupLists);
        lvGroupList.setDragNDropListener(new CustomDragAndDropListener() {
            public void onStartDrag(View itemView) {
            }
            public void onDrag(int x, int y, ListView listView) {
            }

            public void onStopDrag(View itemView) {
            }

            public void onDrop(int flatPosFrom, int flatPosTo) {
                final boolean fromIsGroup = ExpandableListView.getPackedPositionType(lvGroupList.getExpandableListPosition(flatPosFrom)) == ExpandableListView.PACKED_POSITION_TYPE_GROUP;// 判断移动的是不是组
                final boolean toIsGroup = ExpandableListView.getPackedPositionType(lvGroupList.getExpandableListPosition(flatPosTo)) == ExpandableListView.PACKED_POSITION_TYPE_GROUP;// 判断是否移动到分组上

                final long packedPosTo = lvGroupList.getExpandableListPosition(flatPosTo);
                final long packedPosFrom = lvGroupList.getExpandableListPosition(flatPosFrom);
                final int packedGroupPosTo = ExpandableListView.getPackedPositionGroup(packedPosTo);
                final int packedGroupPosForm = ExpandableListView.getPackedPositionGroup(packedPosFrom);

                if ((!fromIsGroup) && toIsGroup) {// 如果移动的不是组，并且是移动到组上
                    long itemId = lvGroupList.getItemIdAtPosition(flatPosFrom);
                    long position = lvGroupList.getItemIdAtPosition(flatPosTo);

                    Group group = (Group) groupLists.get(((int) packedGroupPosForm)).get((int) itemId);
                    ArrayList<Group> groupArrayList1 = (ArrayList<Group>) groupLists.get((int) packedGroupPosForm);
                    groupLists.get((int) packedGroupPosForm).remove(group);

                    ArrayList<Group> groupArrayList2 = (ArrayList<Group>) groupLists.get((int) packedGroupPosForm);
                    (groupLists.get(((int) packedGroupPosTo))).add(group);
                    groupBaseExpandableListAdapter.notifyDataSetChanged();
                    lvGroupList.setGroupLists(groupLists);

//                    View toView = dadelvlist.getChildAt(flatPosTo - dadelvlist.getFirstVisiblePosition());
//                    TextView groupCodeView = (TextView) toView.findViewById(R.id.tvGroupCode);
//                    re_group_groupCode = groupCodeView.getText().toString(); // 移动Item时，移动到组的编码
//                    View fromView = dadelvlist.getChildAt(flatPosFrom - dadelvlist.getFirstVisiblePosition());  // 要移动的设备View
//                    TextView tvMachineView = (TextView) fromView.findViewById(R.id.child_text3);
//                    re_group_machineCode = tvMachineView.getText().toString();  // 移动Item时，该条Item的编码

//                    HashMap<String, String> tmp = (HashMap<String, String>) childs.get(((int) packedGroupPosForm)).get((int) itemId);// 移动的Item项
//                    List<Map<String, String>> tmpList = (List<Map<String, String>>) childs.get((int) packedGroupPosForm);
//                    childs.get((int) packedGroupPosForm).remove(tmp); //  把该 条数据从当前组中进行删除
//
//                    List<Map<String, String>> tmp1 = (List<Map<String, String>>) childs.get(((int) packedGroupPosTo));  // 要移动到分组中的列表
//                    (childs.get(((int) packedGroupPosTo))).add(tmp); //把该 条数据添加到移动之后的组中
//                    adapter.notifyDataSetChanged();
//
//                    View toView = dadelvlist.getChildAt(flatPosTo - dadelvlist.getFirstVisiblePosition()); //获取移动到分组的View
//                    TextView groupCodeView = (TextView) toView.findViewById(R.id.tvGroupCode);
//                    re_group_groupCode = groupCodeView.getText().toString(); // 移动Item时，移动到组的编码
//                    View fromView = dadelvlist.getChildAt(flatPosFrom - dadelvlist.getFirstVisiblePosition());  // 要移动的设备View
//                    TextView tvMachineView = (TextView) fromView.findViewById(R.id.child_text3);
//                    re_group_machineCode = tvMachineView.getText().toString();  // 移动Item时，该条Item的编码

                }
            }
        });
    }
}