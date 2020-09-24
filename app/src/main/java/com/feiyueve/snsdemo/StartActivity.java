package com.feiyueve.snsdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(){
                    @Override
                    public void run() {
                        startActivity(new Intent( StartActivity.this, MainActivity.class));
                        finish();
                    }
                }.start();
            }
        },2000);
    }
}