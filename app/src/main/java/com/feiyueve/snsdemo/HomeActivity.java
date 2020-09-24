package com.feiyueve.snsdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.cache.BitmapCache;
import com.feiyueve.snsdemo.dao.LoginData;
import com.feiyueve.snsdemo.dao.PersonalInf;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.inf.ChatMessage;
import com.feiyueve.snsdemo.inf.Friend;
import com.feiyueve.snsdemo.permission.NotificationsUtils;
import com.feiyueve.snsdemo.permission.PermissionsUtilX;
import com.feiyueve.snsdemo.webservice.JWebSocketClient;
import com.feiyueve.snsdemo.webservice.JWebSocketClientService;
import com.feiyueve.snsdemo.webservice.WebService;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class HomeActivity extends AppCompatActivity implements PermissionsUtilX.IPermissionsCallback{

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView imageViewThumbnail;
    private ImageView iv_photo;
    private Bitmap head;// 头像Bitmap
    private static String path = "/storage/emulated/0/com.feiyueve.snsdemo/";
    private Uri uritempFile;
    private MyApplication myApplication;
    private TextView nicknameText;
    private TextView signatureText;
    private PermissionsUtilX permissionsUtilX;
    private AlertDialog dialog;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private List<ChatMessage> chatChatMessageList = new ArrayList<>();//消息列表
    private ChatMessageReceiver chatMessageReceiver;
    public ArrayList<Friend> friends;
    public ArrayList<String> groupName;
    public HashMap<String,ArrayList<Friend>> friendLists;
    public PersonalInf personalInf;

    public PersonalInf getPersonInf(){
        return personalInf;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;

            myApplication.setClient(client);
            myApplication.setBinder(binder);
            myApplication.setjWebSClientService(jWebSClientService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    private class ChatMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            JSONObject jsonObject = (JSONObject) JSON.parse(message);
            ChatMessage chatMessage = new ChatMessage();
            String msg = jsonObject.getString("msgPayload");
            if(msg!=null){
                String strings[]  = msg.split("_");
                chatMessage.setMessage(jsonObject.getString("msgPayload"));
                chatMessage.setReceive(true);
                chatMessage.setMsgSource(jsonObject.getInteger("msgSource"));
                chatMessage.setType(jsonObject.getInteger("msgType"));
                chatMessage.setTime(jsonObject.getString("sendTime"));
                chatMessage.setSendId(jsonObject.getString("sendId"));
                chatMessage.setSceneryId(jsonObject.getString("sceneryId"));
                chatMessage.setMessage(strings[0]);
                if (chatMessage.getType()==3) {
                    chatMessage.setFileName(strings[1]);
                    chatMessage.setFileLength(strings[2]);
                }
                if(chatMessage.getType()==1){
                    chatMessage.setFileName(strings[1]);
                }
                chatMessage.setRead(false);

                if (chatMessage.getMsgSource() == 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("friendId", jsonObject.getString("sendId"));
                    contentValues.put("chatTextType", jsonObject.getInteger("msgType"));
                    contentValues.put("receiver", true);
                    contentValues.put("chatText", strings[0]);
                    if (chatMessage.getType()==3) {
                        contentValues.put("fileName", strings[1]);
                        contentValues.put("fileLength",strings[2]);
                    }
                    if(chatMessage.getType()==1){
                        contentValues.put("fileName", strings[1]);
                    }
                    contentValues.put("isRead", false);
                    contentValues.put("chatTimeText", jsonObject.getString("sendTime"));
                    dbHelper.createChatMessageTable(db, personalInf.getUsername(), jsonObject.getString("sendId"));
                    dbHelper.createFriendTable(db, personalInf.getUsername());
                    dbHelper.update(db, personalInf.getUsername() + "Friend", contentValues, "friendId", new String[]{jsonObject.getString("sendId")});
                    dbHelper.insert(db, personalInf.getUsername() + "_" + jsonObject.getString("sendId"), contentValues);
                }
            }
            broadcastIntent(chatMessage);
        }
    }

    public void broadcastIntent(ChatMessage chatMessage){
        if(chatMessage.getSendId()!=null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("chatMessage", chatMessage);
            bundle.putSerializable("personalInf", personalInf);
            intent.setAction("cn.programmer.CUSTOM_INTENT");
            intent.putExtra("bundle", bundle);
            sendBroadcast(intent);
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler myHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {

            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(personalInf!=null) {
            myApplication.setPersonalInf(personalInf);
            if (personalInf.getSignature() != null)
                signatureText.setText(personalInf.getSignature());
            else
                signatureText.setText("点击编辑个性签名");

            if (!personalInf.getNickname().isEmpty())
                nicknameText.setText(personalInf.getNickname());
            else
                nicknameText.setText(personalInf.getUsername());
        }
    }

    private void getFriendList(){
        friends = new ArrayList<>();
        friendLists = new HashMap<>();
        groupName = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = db.rawQuery("select * from " + personalInf.getUsername() + "Friend", null);
                while (cursor != null && cursor.moveToNext()) {
                    String friendHead = cursor.getString(cursor.getColumnIndex("friendHead"));
                    String friendName = cursor.getString(cursor.getColumnIndex("friendName"));
                    String recentChatText = cursor.getString(cursor.getColumnIndex("chatText"));
                    String chatTimeText = cursor.getString(cursor.getColumnIndex("chatTimeText"));
                    String isTop = cursor.getString(cursor.getColumnIndex("isTop"));
                    Friend friend = new Friend(friendName, recentChatText, friendHead, chatTimeText, isTop);
                    friend.setId(cursor.getString(cursor.getColumnIndex("friendId")));
                    friend.setAliasName(cursor.getString(cursor.getColumnIndex("aliasName")));
                    friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                    friend.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                    friend.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    friend.setRecentChatLength(cursor.getString(cursor.getColumnIndex("fileLength")));
                    friend.setRecentChatType(cursor.getString(cursor.getColumnIndex("chatTextType")));
                    if (isTop != null && isTop.equals("true"))
                        friends.add(0, friend);
                    else
                        friends.add(friend);
                }
                cursor.close();

                String result = WebService.getFriendList(personalInf.getToken());
                System.out.println(result);
                JSONObject jsonObject = (JSONObject) JSON.parse(result);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (!jsonArray.isEmpty()) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Friend friend = new Friend(object.getString("userId"), object.getString("alias"), object.getString("groupName"));
                            String inf = WebService.getFriendInf(new String[]{friend.getId()}, personalInf.getToken());
                            JSONObject infJson = (JSONObject) JSON.parse(inf);
                            JSONArray infArray = infJson.getJSONArray("result");
                            JSONObject infOne = infArray.getJSONObject(0);
                            friend.setFriendHead(infOne.getString("thumbnail"));
                            friend.setFriendName(infOne.getString("userName"));
                            friend.setLocation(infOne.getString("location"));
                            friend.setBirthday(infOne.getString("birthday"));

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("friendName", friend.getFriendName());
                            contentValues.put("grouping", friend.getGrouping());
                            contentValues.put("friendHead", friend.getFriendHead());
                            contentValues.put("friendNickName", friend.getFriendNickName());
                            contentValues.put("friendId", friend.getId());
                            contentValues.put("gender", friend.getGender());
                            contentValues.put("birthday", friend.getBirthday());
                            contentValues.put("location", friend.getLocation());
                            dbHelper.createChatMessageTable(db, personalInf.getUsername(), friend.getId());
                            dbHelper.createFriendTable(db, personalInf.getUsername());
                            if (!friends.contains(friend)) {
                                friends.add(friend);
                                dbHelper.insert(db, personalInf.getUsername() + "Friend", contentValues);
                            } else {
                                friends.remove(new Friend(friend.getId()));
                                friends.add(friend);
                                dbHelper.update(db, personalInf.getUsername() + "Friend", contentValues, "friendId", new String[]{friend.getId()});
                            }
                        }
                    }
                    for (Friend friend : friends) {
                        ArrayList<Friend> friendArrayList = new ArrayList<>();
                        if (!groupName.contains(friend.getGrouping())) {
                            friendArrayList.add(friend);
                            groupName.add(friend.getGrouping());
                            friendLists.put(friend.getGrouping(), friendArrayList);
                        } else {
                            friendLists.get(friend.getGrouping()).add(friend);
                        }
                    }
                    myApplication.setFriendLists(friendLists);
                    myApplication.setGroupName(groupName);
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        dbHelper = new DBHelper(this, "sns.db", null, 1);
        db = dbHelper.getWritableDatabase();
        SQLiteStudioService.instance().start(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_chat, R.id.nav_photo, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        initPhotoError();
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imageViewThumbnail = (ImageView) headerLayout.findViewById(R.id.imageViewThumbnail);
        nicknameText = (TextView) headerLayout.findViewById(R.id.nicknameText);
        signatureText = (TextView) headerLayout.findViewById(R.id.signatureText);
        myApplication = MyApplication.getInstance();
        personalInf = myApplication.getPersonalInf();
        dbHelper.createFriendTable(db,personalInf.getUsername());
        nicknameText.setText(personalInf.getNickname());
        if(personalInf.getSignature()!=null)
            signatureText.setText(personalInf.getSignature());

        uritempFile = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/com.feiyueve.snsdemo/" + "small.jpg");
        init();
        getPermission();

        getFriendList();
        //启动服务
        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(this);

        Bitmap bitmap = EncodeBase64.stringtoBitmap(personalInf.getThumbnail());
        if(bitmap!=null)
            imageViewThumbnail.setImageBitmap(bitmap);
        else
            imageViewThumbnail.setImageResource(R.drawable.user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void getPermission() {
        String permissions[] = {PermissionsUtilX.Permission.Camera.CAMERA,PermissionsUtilX.Permission.Storage.READ_EXTERNAL_STORAGE
                ,PermissionsUtilX.Permission.Storage.WRITE_EXTERNAL_STORAGE,PermissionsUtilX.Permission.Microphone.RECORD_AUDIO};
        permissionsUtilX = PermissionsUtilX.with(this)
                .requestCode(0)
                .isDebug(true)
                .permissions(permissions)
                .request();
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.imageViewThumbnail:
                getPermission();
                showTypeDialog();
                break;
            case R.id.nicknameText:
                editNicknameDialog();
                break;
            case R.id.signatureText:
                editSignatureDialog();
                break;
        }
    }

    private void editNicknameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        LayoutInflater factorys = LayoutInflater.from(HomeActivity.this);
        View view2 = factorys.inflate(R.layout.layout_nickname_dialog,null);
        final EditText editText = (EditText) view2.findViewById(R.id.dialogEditNickname);
        Button button = (Button) view2.findViewById(R.id.dialogNicknameBt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                String string = editText.getText().toString();
                if(string.isEmpty()||string.contains(" ")){
                    Toast.makeText(getApplicationContext(), "不能为空或带空格", Toast.LENGTH_LONG).show();
                }
                else
                    personalInf.setNickname(string);
                jsonObject.put("nickname", personalInf.getNickname());
                jsonObject.put("userName",personalInf.getUsername());
                nicknameText.setText(personalInf.getNickname());
                PersonalInf.updatePersonalInf(jsonObject,personalInf.getToken());
                dialog.dismiss();
            }
        });

        dialog.setView(view2);
        dialog.show();
    }

    private void editSignatureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        LayoutInflater factorys = LayoutInflater.from(HomeActivity.this);
        View view2 = factorys.inflate(R.layout.layout_signature_dialog,null);
        final EditText editText = (EditText) view2.findViewById(R.id.dialogEditSignature);
        Button button = (Button) view2.findViewById(R.id.dialogSignatureBt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map;
                String string = editText.getText().toString();
                personalInf.setSignature(string);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("signature", personalInf.getSignature());
                jsonObject.put("userName",personalInf.getUsername());
                signatureText.setText(personalInf.getSignature());
                PersonalInf.updatePersonalInf(jsonObject,personalInf.getToken());
                dialog.dismiss();
            }
        });

        dialog.setView(view2);
        dialog.show();
    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        LayoutInflater factorys = LayoutInflater.from(HomeActivity.this);
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
                        head = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (head != null) {
                        String headString = EncodeBase64.bitmaptoString(head);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userName", personalInf.getUsername());
                        jsonObject.put("thumbnail", headString);
                        PersonalInf.updatePersonalInf(jsonObject,personalInf.getToken());

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userName", personalInf.getUsername());
                        contentValues.put("loginStatus", "true");
                        contentValues.put("userKey",  personalInf.getToken());
                        contentValues.put("thumbnail",headString);
                        dbHelper.insert(db, "loginData", contentValues);

                        setPicToView(head);// 保存在SD卡中

                        new BitmapCache().addBitmapToMemoryCache(personalInf.getUsername(),head);
                        imageViewThumbnail.setImageBitmap(head);
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

        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //uritempFile为Uri类变量，实例化uritempFile
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
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
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

    private void init(){
        LayoutInflater factorys = LayoutInflater.from(HomeActivity.this);
        View view = factorys.inflate(R.layout.layout_photo_dialog,null,true);
        onClick(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //需要调用onRequestPermissionsResult
        permissionsUtilX.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, String... permission) {
        //权限获取回调
        Log.e("555", "456" );
    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {
        //权限被拒绝回调
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

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(this, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(this, JWebSocketClientService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.feiyueve.snsdemo.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    private void checkNotification(final Context context) {
        if (!NotificationsUtils.isNotificationEnabled(context)) {
            System.out.println("你还未开启系统通知，将影响消息的接收，要去开启吗？");
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationsUtils.requestNotify(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }
}