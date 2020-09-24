package com.feiyueve.snsdemo.chat;

import android.media.MediaPlayer;
import android.os.Environment;

public class PlayVoiceService {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private boolean isCompleted;
    final String audioSaveDir = RecordVoiceService.audioSaveDir;
    public void playVoice(String voiceName){
        isCompleted = false;
        isPlaying = true;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try{
            mediaPlayer.setDataSource(audioSaveDir+voiceName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isPlaying = false;
                    isCompleted = true;
                }
            });
        }catch (Exception e){
            System.out.println(e.toString());
        }


    }


    public void stopVoice(){
        isPlaying = false;
        isCompleted = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }


    public boolean isPlaying(){
        return isPlaying;
    }
    public boolean isCompleted(){
        return  isCompleted;
    }

}
