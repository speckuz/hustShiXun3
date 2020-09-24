package com.feiyueve.snsdemo.ui.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.feiyueve.snsdemo.AddFriendOrGroupActivity;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.HomeActivity;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.adapter.FriendByGroupListAdapter;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.friendlist.CustomDragAndDropListener;
import com.feiyueve.snsdemo.friendlist.CustomFriendExpandableListView;
import com.feiyueve.snsdemo.friendlist.CustomGroupExpandableListView;
import com.feiyueve.snsdemo.inf.Friend;
import com.feiyueve.snsdemo.inf.Group;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;


public class FriendFragment extends Fragment {
    private ImageButton imgBtnProfilePicture;
    private ImageButton imgBtnAddFriendOrGroup;
    private TextView tvFriendByGroupShow;
    private TextView tvFriendShow;
    private TextView tvGroupShow;
    private ArrayList<String> groupName;
    private ArrayList<Friend> friends;
    private HashMap<String,ArrayList<Friend>> friendLists;
    private ArrayList<ArrayList<Group>> groupLists;
    private CustomFriendExpandableListView lvFriendByGroupList;
    private ListView lvFriendList;
    private CustomGroupExpandableListView lvGroupList;
    private BaseExpandableListAdapter friendBaseExpandableListAdapter;
    private BaseExpandableListAdapter groupBaseExpandableListAdapter;
    View root;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    MyApplication myApplication;
    PersonalInf personalInf;

    @SuppressLint("HandlerLeak")
    final Handler myHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                friendBaseExpandableListAdapter = new FriendByGroupListAdapter(friendLists,getContext(),groupName);
                lvFriendByGroupList.setAdapter(friendBaseExpandableListAdapter);
            }
            if (msg.what == 2) {
            }
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_friend_list, container, false);
        dbHelper = new DBHelper(getContext(), "sns.db", null, 1);
        db = dbHelper.getWritableDatabase();
        myApplication = (MyApplication) getActivity().getApplication();
        while(myApplication.getFriendLists()==null){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        HomeActivity homeActivity = (HomeActivity)getActivity();
        personalInf = homeActivity.getPersonInf();
        friendLists = myApplication.getFriendLists();
        groupName = myApplication.getGroupName();
        navigationView = getActivity().findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        initView();
        return root;
    }

    public void initView(){
        imgBtnProfilePicture = (ImageButton) root.findViewById(R.id.img_btn_friend_profile_picture);
        tvFriendByGroupShow = (TextView) root.findViewById(R.id.tv_friend_by_group_show);
        tvFriendShow = (TextView)root.findViewById(R.id.tv_friend_show);
        tvGroupShow = (TextView)root.findViewById(R.id.tv_group_show);
        lvFriendByGroupList = (CustomFriendExpandableListView) root.findViewById(R.id.lv_friend_by_group_list);
        lvFriendList = (ListView) root.findViewById(R.id.lv_friend_list);
        lvGroupList = (CustomGroupExpandableListView) root.findViewById(R.id.lv_group_list);
        imgBtnAddFriendOrGroup = (ImageButton) root.findViewById(R.id.img_btn_friend_add_friend_or_group);
        imgBtnProfilePicture = (ImageButton) root.findViewById(R.id.img_btn_friend_profile_picture);



        friendBaseExpandableListAdapter = new FriendByGroupListAdapter(friendLists,getContext(),groupName);
        lvFriendByGroupList.setAdapter(friendBaseExpandableListAdapter);

        //groupBaseExpandableListAdapter = new GroupListAdapter(groupLists,getContext(),groupName);
        //lvGroupList.setAdapter(groupBaseExpandableListAdapter);

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

        imgBtnProfilePicture.setImageBitmap(EncodeBase64.stringtoBitmap(personalInf.getThumbnail()));
        imgBtnProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawer(navigationView);
                }else{
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });


        imgBtnAddFriendOrGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), AddFriendOrGroupActivity.class));
            }
        });

    }



    public void initListener() {
        lvFriendByGroupList.setFastScrollEnabled(true);
        lvFriendByGroupList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //ExpandableListView 的回调函数
        lvFriendByGroupList.setFriendLists(friendLists);
        lvFriendByGroupList.setGroupName(groupName);
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

                    ArrayList<Friend> friendArrayList2 = (ArrayList<Friend>) friendLists.get(groupName.get((int) packedGroupPosForm));
                    (friendLists.get(((int) packedGroupPosTo))).add(friend);
                    friendBaseExpandableListAdapter.notifyDataSetChanged();
                    lvFriendByGroupList.setFriendLists(friendLists);
                    lvFriendByGroupList.setGroupName(groupName);

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