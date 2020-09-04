package com.feiyueve.snsdemo.ui.vinispace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.feiyueve.snsdemo.R;

public class ViniSpaceFragment extends Fragment {

    private ViniSpaceViewModel viniSpaceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viniSpaceViewModel =
                ViewModelProviders.of(this).get(ViniSpaceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vinispace, container, false);
        final TextView textView = root.findViewById(R.id.text_vinispace);
        viniSpaceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}