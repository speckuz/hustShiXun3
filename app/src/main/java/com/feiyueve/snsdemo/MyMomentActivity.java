package com.feiyueve.snsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.feiyueve.snsdemo.inf.Comment;
import com.feiyueve.snsdemo.inf.Moment;
import com.feiyueve.snsdemo.moment.MomentAdapter;

import java.util.ArrayList;

public class MyMomentActivity extends AppCompatActivity {

    private String id;
    private ArrayList<Moment> moments;
    private ListView lvMomentList;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moment);
        initView();

    }
    public void initView(){

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println(id);
        lvMomentList = (ListView) findViewById(R.id.lv_moment_list);
        moments = new ArrayList<Moment>();

        ArrayList<Comment> comments1 = new ArrayList<Comment>();
        ArrayList<Comment> comments2 = new ArrayList<Comment>();
        ArrayList<Comment> comments3 = new ArrayList<Comment>();

        comments1.add(new Comment(1,0,1,"我爱澳哥哥"));
        comments2.add(new Comment(1,0,1,"我爱澳哥哥"));
        comments3.add(new Comment(1,0,1,"我爱澳哥哥"));
        comments1.add(new Comment(2,0,2,"我爱澳哥哥"));
        comments2.add(new Comment(2,0,2,"我爱澳哥哥"));
        comments3.add(new Comment(2,0,2,"我爱澳哥哥"));
        comments1.add(new Comment(3,1,3,"我爱澳哥哥"));
        comments2.add(new Comment(3,2,3,"我爱澳哥哥"));
        comments3.add(new Comment(3,1,3,"我爱澳哥哥"));


        ArrayList<String> likeUserName = new ArrayList<String>();
        likeUserName.add("迷弟1");
        likeUserName.add("迷弟12");
        likeUserName.add("迷弟122");
        likeUserName.add("迷弟1222");

        ArrayList<String> likeUserName1 = new ArrayList<String>();
        likeUserName1.add("迷弟1");
        likeUserName1.add("迷弟12");
        likeUserName1.add("迷弟122");
        likeUserName1.add("迷弟1222");

        ArrayList<String> likeUserName2 = new ArrayList<String>();
        likeUserName2.add("迷弟1");
        likeUserName2.add("迷弟12");
        likeUserName2.add("迷弟122");
        likeUserName2.add("迷弟1222");

        moments.add(new Moment("leo","1","今天好开心","2019.01.01 12：30",false,likeUserName,comments1));
        moments.add(new Moment("leo","1","我爱夏天","2019.01.01 12：30",true,likeUserName1,comments2));
        moments.add(new Moment("leo","1","我不喜欢吃柑橘","2019.01.01 12：30",false,likeUserName2,comments3));

        adapter = new MomentAdapter(moments,this,this);
        lvMomentList.setAdapter(adapter);
    }
}