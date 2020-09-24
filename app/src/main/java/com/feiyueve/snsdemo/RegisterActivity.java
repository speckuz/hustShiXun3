package com.feiyueve.snsdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.feiyueve.snsdemo.dao.RegisterData;
import com.feiyueve.snsdemo.webservice.WebService;


public class RegisterActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonGetVerCode;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private EditText editTextRePassword;
    private EditText editTextEmail;
    private EditText editTextVerCode;
    private TextView userNameStatus;
    private RegisterData registerData;
    private static final int COMPLETED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final int[] count = {0};
        init();

        final Handler myHandler=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what==1) {
                    String string=(String)msg.obj;
                    if (string.contains("false")) {
                        userNameStatus.setText("帐号可用");
                        count[0] = 1;
                    } else {
                        userNameStatus.setText("帐号已被使用");
                        count[0] = 2;
                    }
                }
                if(msg.what==2) {
                    String string=(String)msg.obj;
                    if (string.contains("200")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder (RegisterActivity.this);
                        dialog.setTitle ("提示").setMessage ("注册成功");
                        dialog.setNegativeButton ("确定",null);
                        dialog.show ();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder (RegisterActivity.this);
                        dialog.setTitle ("提示").setMessage (string);
                        dialog.setNegativeButton ("确定",null);
                        dialog.show ();
                    }
                }
            }
        };

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerData = new RegisterData(editTextUserName.getText().toString(),
                        editTextPassword.getText().toString(),
                        editTextEmail.getText().toString());
                String string = checkAllInf(registerData,editTextRePassword.getText().toString());
                if(string.equals("ok") && count[0] == 1){
                    try {
                        JSONObject userJSON = new JSONObject();
                        userJSON.put("userName",registerData.getUserName());
                        userJSON.put("passwordHash",encryptToSHA(registerData.getPassword()));
                        //userJSON.put("email",registerData.getEmail());

                        final String content = String.valueOf(userJSON);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = null;
                                try {
                                    result = new WebService().uploadData("http://218.244.151.221:8080/user/create", null,content);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Message msg = myHandler.obtainMessage();
                                msg.obj = result;
                                msg.what = 2;
                                myHandler.sendMessage(msg);
                            }
                        }).start();
                    }catch (Exception e)
                    {

                    }
                }
            }
        });

        editTextUserName.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    final Map data = new HashMap<String,String>();
                    data.put("userName",editTextUserName.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String p = null;
                                p = WebService.getByURL("http://218.244.151.221:8080/user/exists", data,null);
                                Message msg = myHandler.obtainMessage();
                                msg.obj = p;//发送context上下文
                                msg.what = 1;
                                myHandler.sendMessage(msg);
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    public static  String encryptToSHA(String info) {
        String digesta = null;
        DigestUtils.sha1Hex(info);
        digesta = DigestUtils.sha1Hex(info);
        return digesta;
    }

    private void init(){
        buttonRegister = (Button)findViewById(R.id.button3);
        buttonGetVerCode = (Button)findViewById(R.id.button2);
        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailAddress);
        editTextUserName = (EditText)findViewById(R.id.editTextTextPersonName);
        editTextPassword = (EditText)findViewById(R.id.editTextTextPersonName2);
        editTextRePassword = (EditText)findViewById(R.id.editTextTextPersonName3);
        editTextVerCode = (EditText)findViewById(R.id.editTextNumber);
        userNameStatus = (TextView)findViewById(R.id.textView7);
    }

    private String checkAllInf(RegisterData registerData,String rePassword){
        String p = registerData.checkData();
        if(!registerData.getPassword().equals(rePassword)){
            p="两次密码不相同";
        }
        return p;
    }
}
