package com.feiyueve.snsdemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.ui.photo.PhotoViewModel;

public class MyMomentFragment extends Fragment {

    private PhotoViewModel photoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_my_moment, container, false);

        return root;
    }
}