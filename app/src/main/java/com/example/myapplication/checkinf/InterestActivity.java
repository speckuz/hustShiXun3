package com.example.myapplication.checkinf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    TextView interest1;
    TextView interest2;
    TextView interest3;
    TextView interest4;
    TextView interest5;
    TextView interest6;
    TextView interest7;
    TextView interest8;
    TextView interest9;
    TextView interest10;
    TextView interest11;
    TextView interest12;
    TextView interest13;
    TextView interest14;
    TextView interest15;
    TextView interest16;
    TextView interest17;
    TextView interest18;
    ArrayList<TextView> interests;
    ArrayList<Boolean> like;
    Button btnConfirmInterests;
    ArrayList<String> interest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        btnConfirmInterests = findViewById(R.id.btn_confirm_interests);
        interest1 = findViewById(R.id.interest1);
        interest2 = findViewById(R.id.interest2);
        interest3 = findViewById(R.id.interest3);
        interest4 = findViewById(R.id.interest4);
        interest5 = findViewById(R.id.interest5);
        interest6 = findViewById(R.id.interest6);
        interest7 = findViewById(R.id.interest7);
        interest8 = findViewById(R.id.interest8);
        interest9 = findViewById(R.id.interest9);
        interest10 = findViewById(R.id.interest10);
        interest11 = findViewById(R.id.interest11);
        interest12 = findViewById(R.id.interest12);
        interest13 = findViewById(R.id.interest13);
        interest14 = findViewById(R.id.interest14);
        interest15 = findViewById(R.id.interest15);
        interest16 = findViewById(R.id.interest16);
        interest17 = findViewById(R.id.interest17);
        interest18 = findViewById(R.id.interest18);
        interests = new ArrayList<TextView>();
        like = new ArrayList<Boolean>();
        interest = new ArrayList<String>();
        interests.add(interest1);
        interests.add(interest2);
        interests.add(interest3);
        interests.add(interest4);
        interests.add(interest5);
        interests.add(interest6);
        interests.add(interest7);
        interests.add(interest8);
        interests.add(interest9);
        interests.add(interest10);
        interests.add(interest11);
        interests.add(interest12);
        interests.add(interest13);
        interests.add(interest14);
        interests.add(interest15);
        interests.add(interest16);
        interests.add(interest17);
        interests.add(interest18);

        for(int i = 0;i < interests.size();i++){
            like.add(false);
            final int finalI = i;
            interests.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(like.get(finalI)){
                        like.set(finalI,false);
                        interests.get(finalI).setBackground(getResources().getDrawable(R.drawable.shape_chat_message));
                    }else{
                        like.set(finalI,true);
                        interests.get(finalI).setBackground(getResources().getDrawable(R.drawable.shape_chat_voice_showing));
                    }
                }
            });
        }
        btnConfirmInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interest.clear();
                for(int i = 0;i < interests.size();i++){
                    if(like.get(i)){
                        interest.add(interests.get(i).getText().toString());
                    }
                }
                for(int i = 0;i < interest.size();i++){
                    System.out.println(interest.get(i));
                }
            }
        });
    }
}