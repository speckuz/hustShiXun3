package com.feiyueve.snsdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import webservice.WebService;


public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonLogin;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private LoginData loginData;
    final int count[] = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                /*try {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("userName",loginData.getUserName());
                    userJSON.put("password",loginData.getPassword());

                    String content = String.valueOf(userJSON);

                    String result = new WebService().uploadData(content);

                    if(result.contains("yes")) {
                        intent = new Intent();
                        intent.setClass(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder (MainActivity.this);
                        dialog.setTitle ("提示").setMessage ("帐号或密码错误");
                        dialog.setNegativeButton ("确定",null);
                        dialog.show ();
                    }
                }catch (Exception e)
                {

                }*/

            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        editTextPassword = (EditText) findViewById(R.id.textPassword);
        editTextUserName = (EditText)findViewById(R.id.textUserName);
        loginData = new LoginData(editTextUserName.getText().toString(),editTextPassword.getText().toString());
    }
}