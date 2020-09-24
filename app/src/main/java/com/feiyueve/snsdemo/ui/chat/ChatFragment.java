package com.feiyueve.snsdemo.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.adapter.MyFragmentPageAdapter;
import com.feiyueve.snsdemo.chatlist.SwipeListLayout;
import com.feiyueve.snsdemo.ui.fragment.FriendFragment;
import com.feiyueve.snsdemo.ui.fragment.MessageFragment;
import com.feiyueve.snsdemo.ui.fragment.MyMomentFragment;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private List<Fragment> fragmentList;

    private ImageButton btChatFragment;
    private ImageButton btFriendFragment;
    private ImageButton btMomentFragment;
    private ViewPager main_body;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        fragmentList = new ArrayList<>();
        fragmentList.add(new MessageFragment());
        fragmentList.add(new FriendFragment());
        fragmentList.add(new MyMomentFragment());

        btChatFragment = root.findViewById(R.id.btChatFragment);
        btFriendFragment = root.findViewById(R.id.btFriendFragment);
        btMomentFragment = root.findViewById(R.id.btMomentFragment);

        List<ImageButton> buttonList = new ArrayList<>();
        buttonList.add(btChatFragment);
        buttonList.add(btFriendFragment);
        buttonList.add(btMomentFragment);

        main_body = (ViewPager) root.findViewById(R.id.main_body);
        FragmentManager childFragmentManager = getChildFragmentManager();
        main_body.setAdapter(new MyFragmentPageAdapter(childFragmentManager, fragmentList));
        main_body.setCurrentItem(0);

        btChatFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btChatFragment.setImageResource(R.drawable.ic_recent_chat_list_true);
                btFriendFragment.setImageResource(R.drawable.ic_friend_list_false);
                btMomentFragment.setImageResource(R.drawable.ic_moment_false);
                main_body.setCurrentItem(0);

            }
        });

        btFriendFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btChatFragment.setImageResource(R.drawable.ic_recent_chat_list_false);
                btFriendFragment.setImageResource(R.drawable.ic_friend_list_true);
                btMomentFragment.setImageResource(R.drawable.ic_moment_false);
                main_body.setCurrentItem(1);
            }
        });

        btMomentFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btChatFragment.setImageResource(R.drawable.ic_recent_chat_list_false);
                btFriendFragment.setImageResource(R.drawable.ic_friend_list_false);
                btMomentFragment.setImageResource(R.drawable.ic_moment_true);
                main_body.setCurrentItem(2);
            }
        });

        main_body.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            //这个方法就是页面滑动之后底部导航栏的一个状态切换
            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        btChatFragment.setImageResource(R.drawable.ic_recent_chat_list_true);
                        btFriendFragment.setImageResource(R.drawable.ic_friend_list_false);
                        btMomentFragment.setImageResource(R.drawable.ic_moment_false);
                        break;
                    case 1:
                        btChatFragment.setImageResource(R.drawable.ic_recent_chat_list_false);
                        btFriendFragment.setImageResource(R.drawable.ic_friend_list_true);
                        btMomentFragment.setImageResource(R.drawable.ic_moment_false);
                        break;
                    case 2:
                        btChatFragment.setImageResource(R.drawable.ic_recent_chat_list_false);
                        btFriendFragment.setImageResource(R.drawable.ic_friend_list_false);
                        btMomentFragment.setImageResource(R.drawable.ic_moment_true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return root;
    }
}
