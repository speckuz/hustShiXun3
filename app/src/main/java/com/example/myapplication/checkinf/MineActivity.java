package com.example.myapplication.checkinf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.R;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        //修改我的个人信息modifyData
//        ImageButton modifyData = (ImageButton) findViewById(R.id.modifyData);
//        modifyData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent( Mine.this,ModifyData.class));
//            }
//        });
//        //进入我的Moment
//        ImageButton myMoment = (ImageButton) findViewById(R.id.myMoment);
//        myMoment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent( Mine.this,MyMoment.class));
//            }
//        });
    }
}