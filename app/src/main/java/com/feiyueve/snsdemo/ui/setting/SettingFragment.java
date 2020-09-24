package com.feiyueve.snsdemo.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.feiyueve.snsdemo.AccountActivity;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.UpdateUserActivity;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private ImageButton imageButtonUser;
    private ImageButton imageButtonMessage;
    private ImageButton imageButtonAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        imageButtonAccount = (ImageButton)root.findViewById(R.id.imageButtonAccount);
        imageButtonMessage = (ImageButton)root.findViewById(R.id.imageButtonMessage);
        imageButtonUser = (ImageButton)root.findViewById(R.id.imageButtonUser);

        imageButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateUserActivity.class));
            }
        });

        imageButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }
        });


        return root;
    }
}