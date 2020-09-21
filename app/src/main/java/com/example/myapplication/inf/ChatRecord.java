package com.example.myapplication.inf;

public class ChatRecord {
    private String message;
    private int type;
    private boolean receive;
    private String time;
    public static final boolean SEND = false;
    public static final boolean RECEIVE = true;
    public static final int TEXT = 0;
    public static final int VOICE = 1;
    public static final int PICTURE = 2;
    public static final int LOCATION = 3;
    public ChatRecord(String message, boolean receive,int type) {
        this.message = message;
        this.receive = receive;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isReceive() {
        return receive;
    }

    public int getType(){return type;}
}
