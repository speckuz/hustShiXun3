package com.feiyueve.snsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.cache.BitmapCache;
import com.feiyueve.snsdemo.dao.LoginData;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.webservice.WebService;


public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonLogin;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private LoginData loginData;
    private String userName;
    private String loginStatus;
    private String userKey;
    private String password;
    private String userId;
    static final String userTableName = "loginData";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    MyApplication myApplication;

    @SuppressLint("HandlerLeak")
    final Handler myHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
            if (msg.what == 2) {
                String result = (String) msg.obj;
                JSONObject jsonObject = (JSONObject) JSON.parse(result);
                JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                myApplication.getLoginData().setUserKey(jsonObjectResult.getString("token"));
                myApplication.getLoginData().setUserId(jsonObjectResult.getString("userId"));
                final PersonalInf personalInf = PersonalInf.getPersonalInf(userName, myApplication.getLoginData().getUserKey());
                while (personalInf.getUsername()==null);
                personalInf.setToken(myApplication.getLoginData().getUserKey());
                System.out.println(personalInf.getToken());
                if (personalInf.getThumbnail() == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                personalInf.setThumbnail(EncodeBase64.bitmaptoString(new BitmapCache().getBitmap(db,myApplication,personalInf.getUsername())));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                myApplication.setPersonalInf(personalInf);
                System.out.println(myApplication.getPersonalInf().getToken());
                ContentValues contentValues = new ContentValues();
                contentValues.put("userName", myApplication.getLoginData().getUserName());
                contentValues.put("loginStatus", "true");
                contentValues.put("userId", myApplication.getLoginData().getUserId());
                contentValues.put("userKey", myApplication.getLoginData().getUserKey());
                contentValues.put("thumbnail",personalInf.getThumbnail());
                dbHelper.insert(db, "loginData", contentValues);

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("loginData",loginData);
                bundle.putSerializable("personalInf",personalInf);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            if(msg.what==3) {
                String result = (String) msg.obj;
                JSONObject jsonObject = (JSONObject) JSON.parse(result);
                JSONObject jsonObjectResult = jsonObject.getJSONObject("result");

                final PersonalInf personalInf = new PersonalInf();
                personalInf.setPersonalInf(jsonObjectResult);

                LoginData loginData = new LoginData(userName, userKey, null);
                loginData.setUserId(userId);
                personalInf.setToken(loginData.getUserKey());
                if (personalInf.getThumbnail() == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                personalInf.setThumbnail(EncodeBase64.bitmaptoString(new BitmapCache().getBitmap(db, myApplication,personalInf.getUsername())));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                myApplication.setLoginData(loginData);
                myApplication.setPersonalInf(personalInf);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("loginData",loginData);
                bundle.putSerializable("personalInf",personalInf);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            if(msg.what==4){
                Toast.makeText(getApplicationContext(), "登录已过期，请重新登录", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        myApplication = (MyApplication)this.getApplication();
        dbHelper = new DBHelper(this,"sns.db",null,1);
        db = dbHelper.getWritableDatabase();
        if(myApplication.getPersonalInf()!=null&&myApplication.getPersonalInf().getNickname()==null&&myApplication.getPersonalInf().getUsername()!=null){
            editTextUserName.setText(myApplication.getPersonalInf().getUsername());
            ContentValues contentValues = new ContentValues();
            contentValues.put("loginStatus", "true");
            dbHelper.update(db, "loginData", contentValues,"userName",new String[]{userName});
        }

        autoLogin();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                loginData = new LoginData(userName,password);
                String string = loginData.checkLoginData();
                if(string.contains("ok")) {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("userName", loginData.getUserName());
                    userJSON.put("passwordHash", RegisterActivity.encryptToSHA(loginData.getPassword()));
                    final String content = String.valueOf(userJSON);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = "";
                            try {
                                result = new WebService().uploadData("http://218.244.151.221:8080/login", null, content);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (result.contains("200")) {
                                myApplication.setLoginData(loginData);
                                Message msg = myHandler.obtainMessage();
                                msg.what = 2;
                                msg.obj = result;
                                myHandler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("确定退出系统吗？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ActivityCollector.finishAll();
                                    finish();
                                }
                            }).show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void autoLogin(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor onLineUser = dbHelper.find(db,userTableName,"loginStatus",new String[]{"true"});
                    String result = "";
                    if(onLineUser.getCount()!=0) {
                        onLineUser.moveToNext();
                        userName = onLineUser.getString(onLineUser.getColumnIndex("userName"));
                        loginStatus = onLineUser.getString(onLineUser.getColumnIndex("loginStatus"));
                        userKey = onLineUser.getString(onLineUser.getColumnIndex("userKey"));
                        userId = onLineUser.getString(onLineUser.getColumnIndex("userId"));
                        onLineUser.close();
                        result = WebService.checkLoginStatus(userName, userKey);
                        if (result.contains("errMsg")) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("loginStatus", "false");
                            dbHelper.update(db, "loginData", contentValues,"userName",new String[]{userName});
                            Message msg = myHandler.obtainMessage();
                            msg.what = 4;
                            myHandler.sendMessage(msg);
                        } else {
                            Message msg = myHandler.obtainMessage();
                            msg.what = 3;
                            msg.obj = result;
                            myHandler.sendMessage(msg);
                        }
                    }
                }
            }).start();
        } catch (Exception e) {

        }
    }

    private void init(){
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        editTextPassword = (EditText) findViewById(R.id.textPassword);
        editTextUserName = (EditText)findViewById(R.id.textUserName);
        loginData = new LoginData(editTextUserName.getText().toString(),editTextPassword.getText().toString());
    }
}