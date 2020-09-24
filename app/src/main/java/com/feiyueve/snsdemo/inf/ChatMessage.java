package com.feiyueve.snsdemo.inf;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String message;
    private int type;
    private boolean isRead;
    private int msgSource;
    private boolean receive;
    private String time;
    private String fileName;
    private String fileLength;
    private String sendId;
    private String sceneryId;
    public static final boolean SEND = false;
    public static final boolean RECEIVE = true;
    public static final int TEXT = 0;
    public static final int VOICE = 3;
    public static final int PICTURE = 1;
    public static final int LOCATION = 2;
    public ChatMessage(String message, boolean receive, int type, String time) {
        this.message = message;
        this.receive = receive;
        this.type = type;
        this.time = time;
    }

    public ChatMessage(){}

    public String getMessage() {
        return message;
    }

    public boolean isReceive() {
        return receive;
    }

    public int getType(){return type;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLength() {
        return fileLength;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public void setFileLength(String fileLength) {
        this.fileLength = fileLength;
    }

    public int getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(int msgSource) {
        this.msgSource = msgSource;
    }

    public String getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(String sceneryId) {
        this.sceneryId = sceneryId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setType(Integer msgType) {
        this.type = msgType;
    }
}
