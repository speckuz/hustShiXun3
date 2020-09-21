package com.example.myapplication.chat;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.inf.ChatRecord;

import java.util.ArrayList;

public class CustomRecordImagineButton extends androidx.appcompat.widget.AppCompatImageButton {

    private Context context;
    private Activity activity;
    private ArrayList<ChatRecord> chatRecords;
    private ListView lvChatRecord;
    private BaseAdapter adapter;
    private RecordVoiceService recordService;

    private boolean isRecordering = false;   // 是否已经开始录音

    public CustomRecordImagineButton(Context context) {
        super(context);
    }



    public void init(ArrayList<ChatRecord> chatRecords,Activity activity,BaseAdapter adapter) {
        this.chatRecords = chatRecords;
        this.activity = activity;
        this.lvChatRecord = (ListView) activity.findViewById(R.id.tv_chat_record);
        this.adapter = adapter;
        recordService = new RecordVoiceService();

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
                    chatRecords.add(new ChatRecord(mTime/1000+"'",ChatRecord.SEND,ChatRecord.VOICE));
                    adapter.notifyDataSetChanged();
                    lvChatRecord.smoothScrollToPosition(chatRecords.size());


                }
                reset();
                break;
        }


        return super.onTouchEvent(event);
    }

}



