package com.feiyueve.snsdemo.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.feiyueve.snsdemo.dao.LoginData;

import java.util.List;

public class UserSettingAdapter extends ArrayAdapter<LoginData> {

    private int resourceId;

    public UserSettingAdapter(Context context, int textViewResourceId, List<LoginData> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
}
