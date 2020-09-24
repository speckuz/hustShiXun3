package com.feiyueve.snsdemo.chat;

import android.media.MediaRecorder;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class RecordVoiceService {
    MediaRecorder mMediaRecorder;
    String fileName;
    String filePath;
    int timeCount; // 录音时长 计数
    final int TIME_COUNT = 0x101;
    boolean isRecording;

    public static String audioSaveDir = Environment.getExternalStorageDirectory()+"/com.feiyueve.snsdemo/";
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            //申请录音权限

            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            fileName = UUID.randomUUID().toString().replace("-","") + ".m4a";
            File file = new File(audioSaveDir);
            file.mkdir();
            System.out.println(file.exists());
            filePath = audioSaveDir + fileName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            System.out.println("录制失败" + e.getMessage());
            //LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            System.out.println("录制失败" + e.getMessage());
            //LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    public void stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            filePath = "";
        } catch (RuntimeException e) {
            //LogUtil.e(e.toString());
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            System.out.println("录制失败");
            System.out.println("录制失败" + e.getMessage());
            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";
        }
    }

    public void recordFailed(){
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            filePath = "";
        } catch (RuntimeException e) {
            //LogUtil.e(e.toString());
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            System.out.println("录制失败");
            System.out.println("录制失败" + e.getMessage());
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            filePath = "";
        }
    }

//    // 记录录音时长
//    private void countTime() {
//        while (isRecording) {
//            //LogUtil.d("正在录音");
//            timeCount++;
//            ChatMessage msg = ChatMessage.obtain();
//            msg.what = TIME_COUNT;
//            msg.obj = timeCount;
//            myHandler.sendMessage(msg);
//            try {
//                timeThread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        //LogUtil.d("结束录音");
//        timeCount = 0;
//        ChatMessage msg = ChatMessage.obtain();
//        msg.what = TIME_COUNT;
//        msg.obj = timeCount;
//        myHandler.sendMessage(msg);
//    }
//
//    // 格式化 录音时长为 时:分:秒
//    public static String FormatMiss(int miss) {
//        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
//        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
//        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
//        return hh + ":" + mm + ":" + ss;
//    }



}
