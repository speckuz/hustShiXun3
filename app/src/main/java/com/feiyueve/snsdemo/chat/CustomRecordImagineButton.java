package com.feiyueve.snsdemo.chat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.MyApplication;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.dataBase.DBHelper;
import com.feiyueve.snsdemo.inf.ChatMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomRecordImagineButton extends androidx.appcompat.widget.AppCompatImageButton {

    private Context context;
    private Activity activity;
    private ArrayList<ChatMessage> chatMessages;
    private ListView lvChatRecord;
    private BaseAdapter adapter;
    private RecordVoiceService recordService;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    MyApplication myApplication;
    private String userName;
    private String friendId;

    private boolean isRecordering = false;   // 是否已经开始录音

    public CustomRecordImagineButton(Context context) {
        super(context);
    }

    public void init(ArrayList<ChatMessage> chatMessages, Activity activity, BaseAdapter adapter) {
        this.chatMessages = chatMessages;
        this.activity = activity;
        this.lvChatRecord = (ListView) activity.findViewById(R.id.tv_chat_record);
        this.adapter = adapter;
        recordService = new RecordVoiceService();
        dbHelper = new DBHelper(activity, "sns.db", null, 1);
        db = dbHelper.getWritableDatabase();
        myApplication = (MyApplication)activity.getApplication();
        userName = myApplication.getPersonalInf().getUsername();
        friendId = myApplication.getCurrentFriend().getId();
    }

    public CustomRecordImagineButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        new Thread(mUpdateCurTimeRunnable).start();
    }

    private int mTime;  //开始录音计时，计时；（在reset()中置空） 单位为毫秒
    /**
     * 更新当前录音时长的runnable
     */
    private Runnable mUpdateCurTimeRunnable = new Runnable() {

        @Override
        public void run() {
            while(true){
                if(isRecordering){
                    try {
                        Thread.sleep(100);
                        mTime += 100;
                       // System.out.println(mTime);


                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void reset() {
        isRecordering = false;
        mTime = 0;
        this.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chat_record_false));
    }
    public boolean wantToCancel(int x,int y){
        return false;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();   //获取当前Action
        int x = (int) event.getX();       //获取当前的坐标
        int y = (int) event.getY();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                    this.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chat_record_true));
                    isRecordering = true;
                    recordService.startRecord();
                    //System.out.println(isRecordering);

                break;

            case MotionEvent.ACTION_MOVE:
                if (isRecordering) {
                    if (wantToCancel(x, y)) {

                    } else {
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isRecordering || mTime < 900) {
                    reset();
                    recordService.recordFailed();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"澳哥哥时间太短啦，录制失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return super.onTouchEvent(event);
                }

                if (isRecordering) { //正常录制结束
                    recordService.stopRecord();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"录制成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    String fileString = null;
                    try {
                        fileString = EncodeBase64.fileToString(RecordVoiceService.audioSaveDir+recordService.fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                    String t=format.format(new Date());
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("chatText",fileString);
                    contentValues.put("chatTextType", "3");
                    contentValues.put("receiver",false);
                    contentValues.put("chatTimeText",t);
                    contentValues.put("fileLength",mTime/1000);
                    contentValues.put("fileName",recordService.fileName);

                    dbHelper.insert(db,userName+"_"+friendId,contentValues);
                    dbHelper.update(db,userName+"Friend",contentValues,"friendId",new String[]{friendId});
                    ChatMessage chatMessage = new ChatMessage(fileString,false, ChatMessage.VOICE,t);
                    chatMessage.setFileLength(mTime/1000+"");
                    chatMessage.setFileName(recordService.fileName);
                    chatMessage.setMsgSource(1);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    lvChatRecord.setSelection(chatMessages.size());

                    Intent intent = new Intent();
                    intent.setAction("cn.programmer.CUSTOM_INTENT2");
                    intent.putExtra("fileLength",mTime/1000+"");
                    intent.putExtra("friendId",friendId);
                    intent.putExtra("chatText",recordService.filePath);
                    intent.putExtra("fileName",recordService.fileName);
                    activity.sendBroadcast(intent);
                }
                reset();
                break;
        }


        return super.onTouchEvent(event);
    }

}



