package com.example.myapplication.others;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.chatlist.ChatListActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(){
                    @Override
                    public void run() {
                        startActivity(new Intent( Splash.this, ChatListActivity.class));
                        finish();
                    }
                }.start();
            }
        },2000);
    }
}