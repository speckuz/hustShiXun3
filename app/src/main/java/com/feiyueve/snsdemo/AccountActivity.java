package com.feiyueve.snsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.adapter.AccountAdapter;
import com.feiyueve.snsdemo.cache.BitmapCache;
import com.feiyueve.snsdemo.dao.LoginData;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.list.AccountList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private ListView listView;
    private List<AccountList> accountLists = new ArrayList<>();
    private DBHelper helper;
    private SQLiteDatabase db;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        myApplication = (MyApplication)this.getApplication();
        helper = new DBHelper(this,"sns.db",null,1);
        db = helper.getWritableDatabase();
        listView = (ListView)findViewById(R.id.accountList);
        initAccountLists();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showTypeDialog(arg0,arg1,arg2,arg3);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    final Handler myHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                accountLists = (List<AccountList>)msg.obj;
                AccountAdapter accountAdapter = new AccountAdapter(AccountActivity.this,R.layout.layout_account_list,accountLists);
                listView.setAdapter(accountAdapter);
            }
        }
    };

    private void showTypeDialog(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        LayoutInflater factorys = LayoutInflater.from(AccountActivity.this);
        View view2 = factorys.inflate(R.layout.layout_listclickaccount_dialog,null);
        TextView loginThisAccount = (TextView) view2.findViewById(R.id.loginThisAccount);
        TextView deleteThisAccount = (TextView) view2.findViewById(R.id.deleteThisAccount);
        TextView quitThisAccount = (TextView) view2.findViewById(R.id.quitThisAccount);
        loginThisAccount.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                AccountList accountList = (AccountList)parent.getItemAtPosition(position);
                String userName = accountList.getUserName();
                if(userName.equals(myApplication.getPersonalInf().getUsername())){
                    Toast.makeText(getApplication(), "该账号已登录", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("loginStatus", "true");
                    helper.update(db, "loginData", contentValues1,"userName",new String[]{userName});

                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("loginStatus", "false");
                    helper.update(db, "loginData", contentValues2,"userName",new String[]{myApplication.getPersonalInf().getUsername()});

                    myApplication.setLoginData(new LoginData(userName));
                    myApplication.setPersonalInf(new PersonalInf(userName));

                    Intent intent = new Intent();
                    intent.setClass(AccountActivity.this, MainActivity.class);
                    ActivityCollector.finishAll();
                    startActivity(intent);
                    finish();
                }
                dialog.dismiss();
            }
        });
        deleteThisAccount.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                helper.delete(db,"loginData","userName",new String[]{myApplication.getPersonalInf().getUsername()});
                Intent intent = new Intent();
                intent.setClass(AccountActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        quitThisAccount.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                AccountList accountList = (AccountList)parent.getItemAtPosition(position);
                String userName = accountList.getUserName();
                if(!userName.equals(myApplication.getPersonalInf().getUsername())){
                    Toast.makeText(getApplication(), "该账号未登录", Toast.LENGTH_SHORT).show();
                }else {
                    myApplication.setLoginData(new LoginData());
                    myApplication.setPersonalInf(new PersonalInf());

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("loginStatus", "false");
                    helper.update(db, "loginData", contentValues, "userName", new String[]{userName});

                    Intent intent = new Intent();
                    intent.setClass(AccountActivity.this, MainActivity.class);
                    ActivityCollector.finishAll();
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            }
        });
        dialog.setView(view2);
        dialog.show();
    }

    public void initAccountLists() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = db.rawQuery("select * from loginData", null);
                List<AccountList> lists = new ArrayList<>();
                while (cursor.moveToNext()) {
                    final String userName = cursor.getString(cursor.getColumnIndex("userName"));
                    System.out.println("111111111111111111111111111111111" + userName);
                    Bitmap thumbnail = null;
                    try {
                        thumbnail = new BitmapCache().getBitmap(db, myApplication, userName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lists.add(new AccountList(userName, thumbnail));
                }
                Message msg = myHandler.obtainMessage();
                msg.what = 1;
                msg.obj = lists;
                myHandler.sendMessage(msg);
                cursor.close();
            }
        }).start();
    }
}