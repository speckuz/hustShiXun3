package com.example.myapplication.chat;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.myapplication.inf.ChatRecord;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;

import org.json.JSONObject;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<ChatRecord> chatRecords ;
    private LayoutInflater layoutInflater;
    private Context context;
    private LinearLayout llBroadcasting;
    private PlayVoiceService playVoiceService;

    private static final String APP_ID = "5f6403b0";
    private static final String SECRET_KEY = "0e5bffc571b01fe4545f7fd8e36bb2aa";
    private static final String audioSaveDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.example.myapplication/files/audio/";

    public ChatAdapter(ArrayList<ChatRecord> chatRecords, Context context){
        this.chatRecords = chatRecords;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }
    @Override
    public int getCount() {
        return chatRecords.size();
    }

    @Override
    public Object getItem(int i) {
        return chatRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        if(chatRecords.get(i).getType() == ChatRecord.TEXT){
            if(chatRecords.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_text,null);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_text,null);
            }
            holder.tvChatContent = (TextView) view.findViewById(R.id.text);
            holder.tvChatContent.setText(chatRecords.get(i).getMessage());
        }
        if(chatRecords.get(i).getType() == ChatRecord.VOICE){
            if(chatRecords.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_voice,null);
                holder.llVoice = (LinearLayout)view.findViewById(R.id.ll_chat_voice_receive);
                holder.tvVoice = (TextView)view.findViewById(R.id.tv_chat_voice_time_receive);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_voice,null);
                holder.llVoice = (LinearLayout)view.findViewById(R.id.ll_chat_voice_send);
                holder.tvVoice = (TextView)view.findViewById(R.id.tv_chat_voice_time_send);
            }
            holder.playVoiceService = new PlayVoiceService();
            holder.llVoice.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try{
                        LfasrClient lfasrClient = LfasrClient.getInstance(APP_ID, SECRET_KEY);
                        Message task = lfasrClient.upload(audioSaveDir+"test.m4a");
                        String taskId = task.getData();
                        System.out.println("转写任务 taskId：" + taskId);
//                        int status = 0;
//                        while (status != 9) {
//                            Message message = lfasrClient.getProgress(taskId);
//                            JSONObject object = JSON.parseObject(message.getData());
//                            status = object.getInteger("status");
//                            System.out.println(message.getData());
//                            TimeUnit.SECONDS.sleep(2);
//                        }
                        //4、获取结果
                        Message result = lfasrClient.getResult(taskId);
                        System.out.println("转写结果: \n" + result.getData());
                    }catch (Exception e){

                    }

                    return false;
                }
            });
            holder.llVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(llBroadcasting == null){
                        llBroadcasting = holder.llVoice;
                        playVoiceService = holder.playVoiceService;
                        holder.llVoice.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_voice_showing));
                        holder.playVoiceService.playVoice("test.m4a");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true){
                                    if(!holder.playVoiceService.isPlaying()){

                                        if(holder.playVoiceService.isCompleted()){
                                            System.out.println("completed");
                                            holder.playVoiceService.stopVoice();
                                            holder.llVoice.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_message));
                                            llBroadcasting = null;
                                            playVoiceService = null;
                                        }else{
                                            System.out.println("changed");
                                        }
                                        break;
                                    }
                                }
                            }
                        }).start();
                    }else{
                        if(llBroadcasting == holder.llVoice){
                            holder.playVoiceService.stopVoice();
                            llBroadcasting.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_message));
                            llBroadcasting = null;
                            playVoiceService = null;
                        }else{
                            playVoiceService.stopVoice();
                            playVoiceService = holder.playVoiceService;
                            holder.playVoiceService.playVoice("test.m4a");
                            llBroadcasting.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_message));
                            llBroadcasting = holder.llVoice;
                            holder.llVoice.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_voice_showing));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(true){
                                        if(!holder.playVoiceService.isPlaying()){

                                            if(holder.playVoiceService.isCompleted()){
                                                System.out.println("completed");
                                                holder.playVoiceService.stopVoice();
                                                holder.llVoice.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_message));
                                                llBroadcasting = null;
                                                playVoiceService = null;
                                            }else{
                                                System.out.println("changed");
                                            }
                                            break;
                                        }
                                    }
                                }
                            }).start();
                        }

                    }

                }
            });
            holder.tvVoice.setText(chatRecords.get(i).getMessage());

        }
        if(chatRecords.get(i).getType() == ChatRecord.PICTURE){
            if(chatRecords.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_picture,null);
                holder.llPicture = (LinearLayout)view.findViewById(R.id.ll_chat_picture_receive);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_picture,null);
                holder.llPicture = (LinearLayout)view.findViewById(R.id.ll_chat_picture_send);
            }
            holder.llPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        if(chatRecords.get(i).getType() == ChatRecord.LOCATION){
            if(chatRecords.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_location,null);
                holder.flLocation = (FrameLayout) view.findViewById(R.id.fl_chat_location_receive);
                holder.tvLocation = (TextView)view.findViewById(R.id.tv_chat_location_receive);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_location,null);
                holder.flLocation = (FrameLayout) view.findViewById(R.id.fl_chat_location_send);
                holder.tvLocation = (TextView)view.findViewById(R.id.tv_chat_location_send);
            }
            holder.flLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.tvLocation.setText(chatRecords.get(i).getMessage());
        }

        return view;
    }

    private class Holder {
        private TextView tvChatContent;
        private LinearLayout llPicture;
        private LinearLayout llVoice;
        private TextView tvVoice;
        private FrameLayout flLocation;
        private TextView tvLocation;
        private PlayVoiceService playVoiceService;
    }
}
