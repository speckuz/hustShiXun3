package com.feiyueve.snsdemo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyueve.snsdemo.EncodeBase64.EncodeBase64;
import com.feiyueve.snsdemo.R;
import com.feiyueve.snsdemo.chat.PlayVoiceService;
import com.feiyueve.snsdemo.inf.ChatMessage;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> chatMessages;
    private LayoutInflater layoutInflater;
    private Context context;
    private FrameLayout llBroadcasting;
    private PlayVoiceService playVoiceService;

    public ChatAdapter(ArrayList<ChatMessage> chatMessages, Context context){
        this.chatMessages = chatMessages;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }
    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return chatMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        if(chatMessages.get(i).getType() == ChatMessage.TEXT){
            if(chatMessages.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_text,null);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_text,null);
            }
            holder.tvChatContent = (TextView) view.findViewById(R.id.text);
            holder.tvChatContent.setText(chatMessages.get(i).getMessage());
        }

        if(chatMessages.get(i).getType() == ChatMessage.VOICE){
            if(chatMessages.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_voice,null);
                holder.llVoice = (FrameLayout)view.findViewById(R.id.ll_chat_voice_receive);
                holder.tvVoice = (TextView)view.findViewById(R.id.tv_chat_voice_time_receive);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_voice,null);
                holder.llVoice = (FrameLayout)view.findViewById(R.id.ll_chat_voice_send);
                holder.tvVoice = (TextView)view.findViewById(R.id.tv_chat_voice_time_send);
            }
            holder.tvVoice.setText(chatMessages.get(i).getFileLength());
            holder.playVoiceService = new PlayVoiceService();
            holder.llVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(llBroadcasting == null){
                        llBroadcasting = holder.llVoice;
                        playVoiceService = holder.playVoiceService;
                        holder.llVoice.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_voice_showing));
                        String fileName = chatMessages.get(i).getFileName();
                        holder.playVoiceService.playVoice(fileName);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true){
                                    if(!holder.playVoiceService.isPlaying()){
                                        if(holder.playVoiceService.isCompleted()){
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
                            String fileName = chatMessages.get(i).getFileName();
                            holder.playVoiceService.playVoice(fileName);
                            llBroadcasting.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_message));
                            llBroadcasting = holder.llVoice;
                            holder.llVoice.setBackground(context.getResources().getDrawable(R.drawable.shape_chat_voice_showing));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(true){
                                        if(!holder.playVoiceService.isPlaying()){
                                            if(holder.playVoiceService.isCompleted()){
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
        }
        if(chatMessages.get(i).getType() == ChatMessage.PICTURE){
            if(chatMessages.get(i).isReceive()){
                view = layoutInflater.inflate(R.layout.adapter_item_chat_receive_picture,null);
                holder.imageView = (ImageView) view.findViewById(R.id.iv_chat_receive_picture);
            }else{
                view = layoutInflater.inflate(R.layout.adapter_item_chat_send_picture,null);
                holder.imageView = (ImageView)view.findViewById(R.id.iv_chat_send_picture);
            }
            holder.imageView.setImageBitmap(EncodeBase64.stringtoBitmap(chatMessages.get(i).getMessage()));
        }

        if(chatMessages.get(i).getType() == ChatMessage.LOCATION){
            if(chatMessages.get(i).isReceive()){
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
            holder.tvLocation.setText(chatMessages.get(i).getMessage());
        }

        return view;
    }

    private class Holder {
        private TextView tvChatContent;
        private ImageView imageView;
        private FrameLayout llVoice;
        private TextView tvVoice;
        private FrameLayout flLocation;
        private TextView tvLocation;
        private PlayVoiceService playVoiceService;
    }
}
