package com.feiyueve.snsdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.alibaba.fastjson.JSONObject;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.adapter.ChatAdapter;
import com.feiyueve.snsdemo.adapter.ChatListAdapter;
import com.feiyueve.snsdemo.cache.BitmapCache;
import com.feiyueve.snsdemo.chat.CustomRecordImagineButton;
import com.feiyueve.snsdemo.chat.RecordVoiceService;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.inf.ChatMessage;
import com.feiyueve.snsdemo.inf.Friend;
import com.feiyueve.snsdemo.webservice.JWebSocketClient;
import com.feiyueve.snsdemo.webservice.JWebSocketClientService;
import com.feiyueve.snsdemo.webservice.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity{

    private EditText etTextMessage;
    private ArrayList<ChatMessage> chatMessages;
    private ListView lvChatRecord;
    private TextView tvFriendName;
    private BaseAdapter adapter;
    private ImageButton imgBtnSendText;
    private ImageButton imgBtnSendPicture;
    private ImageButton imgBtnSendLocation;
    private ImageButton imgBtnBackToChatList;
    private ImageButton imgBtnVoiceMessage;
    private ImageButton imgBtnShowOthers;
    private ImageButton imgBtnMore;
    private LinearLayout llShowVoice;
    private LinearLayout llShowPicture;
    private CustomRecordImagineButton imgBtnSoundRecording;
    private boolean isVoiceOpened;
    private boolean isPictureOpened;
    private String message;
    private String friendName;
    private String friendHead;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    MyApplication myApplication;
    private String userName;
    private String userId;
    private Friend friend;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private LocatiopnBroadcast actionBroadcast;
    private LocatiopnBroadcast2 actionBroadcast2;
    private Activity activity;
    private static String path = "/storage/emulated/0/com.feiyueve.snsdemo/";
    private Uri uritempFile;
    private AlertDialog dialog;
    private Bitmap pictureMsg;
    private String fileName;
    private Context context;
    private ChatMessage chatMessage;
    private PersonalInf personalInf;

    @Override
    public void onResume() {
        super.onResume();
        if (actionBroadcast != null) {
            try {
                unregisterReceiver(actionBroadcast);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Receiver not registered")) {
                    // Ignore this exception. This is exactly what is desired
                } else {
                    // unexpected, re-throw
                    throw e;
                }
            }
        }

        if (actionBroadcast2 != null) {
            try {
                unregisterReceiver(actionBroadcast2);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Receiver not registered")) {
                    // Ignore this exception. This is exactly what is desired
                } else {
                    // unexpected, re-throw
                    throw e;
                }
            }
        }
        actionBroadcast = new LocatiopnBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.programmer.CUSTOM_INTENT");
        registerReceiver(actionBroadcast, intentFilter);

        actionBroadcast2 = new LocatiopnBroadcast2();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("cn.programmer.CUSTOM_INTENT2");
        registerReceiver(actionBroadcast2, intentFilter2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dbHelper = new DBHelper(this, "sns.db", null, 1);
        db = dbHelper.getWritableDatabase();
        myApplication = (MyApplication)getApplication();
        client = myApplication.getClient();
        jWebSClientService = myApplication.getjWebSClientService();
        binder = myApplication.getBinder();
        userName = myApplication.getPersonalInf().getUsername();
        userId = myApplication.getPersonalInf().getUserId();
        activity = this;
        context = this;
        Bundle bundle = this.getIntent().getExtras();
        friend = (Friend) bundle.getSerializable("friend");
        initPhotoError();

        actionBroadcast = new LocatiopnBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.programmer.CUSTOM_INTENT");
        registerReceiver(actionBroadcast, intentFilter);

        actionBroadcast2 = new LocatiopnBroadcast2();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("cn.programmer.CUSTOM_INTENT2");
        registerReceiver(actionBroadcast2, intentFilter2);

        try {
            initView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        this.getExternalFilesDir(null);
    }

    public class LocatiopnBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra("bundle");
            chatMessage = (ChatMessage)bundle.getSerializable("chatMessage");
            personalInf = (PersonalInf)bundle.getSerializable("personalInf");
            myApplication.setPersonalInf(personalInf);
            try {
                initView(chatMessage.getSendId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter = new ChatAdapter(chatMessages,context);
            lvChatRecord.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            lvChatRecord.setSelection(chatMessages.size());
            imgBtnSoundRecording.init(chatMessages,activity,adapter);
        }
    }

    public class LocatiopnBroadcast2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fileLength = intent.getStringExtra("fileLength");
            String fileName = intent.getStringExtra("fileName");
            friend.setId(intent.getStringExtra("friendId"));
            sendMessage(fileName,fileLength,3);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //销毁在onResume()方法中的广播
        if (actionBroadcast != null) {
            try {
                unregisterReceiver(actionBroadcast);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Receiver not registered")) {
                    // Ignore this exception. This is exactly what is desired
                } else {
                    // unexpected, re-throw
                    throw e;
                }
            }
        }

        if (actionBroadcast2 != null) {
            try {
                unregisterReceiver(actionBroadcast2);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Receiver not registered")) {
                    // Ignore this exception. This is exactly what is desired
                } else {
                    // unexpected, re-throw
                    throw e;
                }
            }
        }

    }

    public void initView(String sendId) throws Exception {
        //聊天记录
        myApplication.setCurrentFriend(friend);
        Cursor cursor;
        if(sendId!=null)
            cursor = db.rawQuery("select * from "+myApplication.getPersonalInf().getUsername()+"_"+sendId, null);
        else
            cursor = db.rawQuery("select * from "+myApplication.getPersonalInf().getUsername()+"_"+friend.getId(), null);
        chatMessages = new ArrayList<>();
        String receiver = null;
        String chatTimeText= null;
        String chatText= null;
        String fileName = null;
        String fileLength = null;
        ChatMessage chatMessage = null;
        while (cursor!=null && cursor.moveToNext()) {
            switch (cursor.getInt(cursor.getColumnIndex("chatTextType"))){
                case ChatMessage.TEXT:
                    receiver = cursor.getString(cursor.getColumnIndex("receiver"));
                    chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                    chatText = cursor.getString(cursor.getColumnIndex("chatText"));
                    chatMessages.add(new ChatMessage(chatText,receiver.equals("1"), ChatMessage.TEXT,chatTimeText));
                    break;
                case ChatMessage.VOICE:
                    receiver = cursor.getString(cursor.getColumnIndex("receiver"));
                    chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                    chatText = cursor.getString(cursor.getColumnIndex("chatText"));
                    fileName = cursor.getString(cursor.getColumnIndex("fileName"));
                    fileLength = cursor.getString(cursor.getColumnIndex("fileLength"));
                    EncodeBase64.stringToFile(chatText, RecordVoiceService.audioSaveDir+fileName);
                    chatMessage = new ChatMessage(chatText,receiver.equals("1"), ChatMessage.VOICE,chatTimeText);
                    chatMessage.setFileName(fileName);
                    chatMessage.setFileLength(fileLength);
                    chatMessages.add(chatMessage);
                    break;
                case ChatMessage.PICTURE:
                    receiver = cursor.getString(cursor.getColumnIndex("receiver"));
                    chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                    chatText = cursor.getString(cursor.getColumnIndex("chatText"));
                    fileName = cursor.getString(cursor.getColumnIndex("fileName"));;
                    chatMessage = new ChatMessage(chatText,receiver.equals("1"), ChatMessage.PICTURE,chatTimeText);
                    chatMessage.setFileName(fileName);
                    chatMessages.add(chatMessage);
                    break;
                case ChatMessage.LOCATION:
                    break;
            }
        }
    }

    private void init(){
        isVoiceOpened = false;
        isPictureOpened = false;

        etTextMessage = (EditText) findViewById(R.id.et_text_message);
        lvChatRecord = (ListView) findViewById(R.id.tv_chat_record);
        imgBtnBackToChatList = (ImageButton) findViewById(R.id.img_btn_back_to_chatList);
        imgBtnVoiceMessage = (ImageButton) findViewById(R.id.img_btn_voice_message);
        imgBtnMore = (ImageButton) findViewById(R.id.img_btn_more);
        imgBtnSendText = (ImageButton) findViewById(R.id.img_btn_send_text);
        llShowVoice = (LinearLayout)findViewById(R.id.ll_show_voice);
        llShowPicture = (LinearLayout)findViewById(R.id.ll_others);
        imgBtnShowOthers = (ImageButton)findViewById(R.id.img_btn_show_others);
        tvFriendName = (TextView)findViewById(R.id.tv_friend_name);
        imgBtnSoundRecording = (CustomRecordImagineButton)findViewById(R.id.img_btn_sound_recording);
        imgBtnSendPicture = findViewById(R.id.img_btn_picture);
        imgBtnSendLocation = findViewById(R.id.img_btn_location);
        adapter = new ChatAdapter(chatMessages,this);
        lvChatRecord.setAdapter(adapter);
        lvChatRecord.setSelection(chatMessages.size());

        imgBtnSoundRecording.init(chatMessages,this,adapter);

        imgBtnBackToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ChatActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        imgBtnSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });

        //发送语音
        imgBtnVoiceMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPictureOpened){
                    imgBtnShowOthers.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_show_picture_false));
                    llShowPicture.setVisibility(View.GONE);
                    isPictureOpened = false;
                }
                if(isVoiceOpened){
                    imgBtnVoiceMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_voice_show_false));
                    llShowVoice.setVisibility(View.GONE);
                    isVoiceOpened = false;
                }else{
                    imgBtnVoiceMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_voice_show_true));
                    llShowVoice.setVisibility(View.VISIBLE);
                    isVoiceOpened = true;
                }
            }
        });

        imgBtnShowOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVoiceOpened){
                    imgBtnVoiceMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_voice_show_false));
                    llShowVoice.setVisibility(View.GONE);
                    isVoiceOpened = false;
                }
                if(isPictureOpened){
                    imgBtnShowOthers.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_show_picture_false));
                    llShowPicture.setVisibility(View.GONE);
                    isPictureOpened = false;
                }else{
                    imgBtnShowOthers.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_show_picture_true));
                    llShowPicture.setVisibility(View.VISIBLE);
                    isPictureOpened = true;
                }

            }
        });

        //获取更多
        imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFriendInfDialog();
            }
        });

        //发送文字消息
        imgBtnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = etTextMessage.getText().toString();
                if (message.length() <= 0) {
                    return;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                String t=format.format(new Date());
                ContentValues contentValues = new ContentValues();
                contentValues.put("chatText",message);
                contentValues.put("friendId",friend.getId());
                contentValues.put("chatTextType","0");
                contentValues.put("receiver",false);
                contentValues.put("chatTimeText",t);
                dbHelper.insert(db,userName+"_"+friend.getId(),contentValues);
                contentValues.remove("friendId");
                dbHelper.update(db,userName+"Friend",contentValues,"friendId",new String[]{friend.getId()});
                chatMessages.add(new ChatMessage(message,false, ChatMessage.TEXT,t));

                sendMessage(null,null,0);
            }
        });
    }

    private void showFriendInfDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        LayoutInflater factorys = LayoutInflater.from(ChatActivity.this);
        View view2 = factorys.inflate(R.layout.layout_friendinf_dialog,null);
        ImageView head = view2.findViewById(R.id.touxiang);
        head.setImageBitmap(EncodeBase64.stringtoBitmap(friend.getFriendHead()));
        dialog.setView(view2);
        dialog.show();
    }

    private void showTypeDialog() {
        fileName = UUID.randomUUID().toString().replace("-","") + ".jpg";
        uritempFile = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/com.feiyueve.snsdemo/" + fileName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        LayoutInflater factorys = LayoutInflater.from(ChatActivity.this);
        View view2 = factorys.inflate(R.layout.layout_photo_dialog,null);
        TextView tv_select_gallery = (TextView) view2.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view2.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory()+ "/com.feiyueve.snsdemo/", "temp.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view2);
        dialog.show();
    }

    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/com.feiyueve.snsdemo/"+"temp.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    try {
                        pictureMsg = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (pictureMsg != null) {
                        String headString = EncodeBase64.bitmaptoString(pictureMsg);
                        String friendId = myApplication.getCurrentFriend().getId();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                        String t=format.format(new Date());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("chatText",headString);
                        contentValues.put("chatTextType", "1");
                        contentValues.put("receiver",false);
                        contentValues.put("chatTimeText",t);
                        contentValues.put("fileName",fileName);

                        dbHelper.insert(db,userName+"_"+friendId,contentValues);
                        dbHelper.update(db,userName+"Friend",contentValues,"friendId",new String[]{friendId});
                        ChatMessage chatMessage = new ChatMessage(headString,false, ChatMessage.PICTURE,t);
                        chatMessage.setFileName(fileName);
                        chatMessage.setMsgSource(1);
                        chatMessages.add(chatMessage);
                        adapter.notifyDataSetChanged();
                        lvChatRecord.setSelection(chatMessages.size());

                        sendMessage(fileName,null,1);

                        setPicToView(pictureMsg);// 保存在SD卡中

                        new BitmapCache().addBitmapToMemoryCache(myApplication.getLoginData().getUserName(),pictureMsg);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 170);
        intent.putExtra("outputY", 170);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String name = path + fileName;// 图片名字
        try {
            b = new FileOutputStream(name);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
// 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(final String fileName, final String fileLength, final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(type==0) {
                    while(client == null || !client.isOpen()) {
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("msgPayload",message);
                    jsonObject.put("msgSource",1);
                    jsonObject.put("msgType",0);
                    jsonObject.put("receiveId",friend.getId());
                    String content = jsonObject.toJSONString();
                    jWebSClientService.sendMsg(content);
                }else if(type==3){
                    for(ChatMessage ch:chatMessages){
                        if(ch.getFileName()!=null && ch.getFileName().equals(fileName)) {
                            while (client == null || !client.isOpen()) {
                            }
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("msgPayload", ch.getMessage() + "_" + ch.getFileName() + "_" + fileLength);
                            jsonObject.put("msgSource", 1);
                            jsonObject.put("msgType", 3);
                            jsonObject.put("fileName", ch.getFileName());
                            jsonObject.put("receiveId", friend.getId());
                            jsonObject.put("fileLength", fileLength);
                            String content = jsonObject.toJSONString();
                            jWebSClientService.sendMsg(content);
                        }
                    }
                }else if(type==1){
                    for(ChatMessage ch:chatMessages){
                        if(ch.getFileName()!=null && ch.getFileName().equals(fileName)) {
                            while (client == null || !client.isOpen()) {
                            }
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("msgPayload", ch.getMessage() + "_" + ch.getFileName());
                            jsonObject.put("msgSource", 1);
                            jsonObject.put("msgType", 1);
                            jsonObject.put("fileName", ch.getFileName());
                            jsonObject.put("receiveId", friend.getId());
                            jsonObject.put("fileLength", fileLength);
                            String content = jsonObject.toJSONString();
                            jWebSClientService.sendMsg(content);
                        }
                    }
                }

            }
        }).start();
        etTextMessage.setText("");
        adapter.notifyDataSetChanged();
        lvChatRecord.setSelection(chatMessages.size());
    }
}