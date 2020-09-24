package com.feiyueve.snsdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.list.AccountList;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<AccountList> {
    private int recourceId;

    public AccountAdapter(@NonNull Context context, int resource, @NonNull List<AccountList> objects) {
        super(context, resource, objects);
        recourceId = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccountList accountList = getItem(position); //得到集合中指定位置的一组数据，并且实例化
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_account_list,parent,false); //用布局裁剪器(又叫布局膨胀器)，将导入的布局裁剪并且放入到当前布局中
        ImageView imageView = (ImageView)view.findViewById(R.id.headListImage);//从裁剪好的布局里获取ImageView布局ID
        TextView textView = (TextView)view.findViewById(R.id.infListUserText); //从裁剪好的布局里获取TextView布局Id
        imageView.setImageBitmap(accountList.getBitmap());//将当前一组imageListArray类中的图片iamgeId导入到ImageView布局中
        textView.setText(accountList.getUserName());//将当前一组imageListArray类中的TextView内容导入到TextView布局中
        return view;
    }
}
