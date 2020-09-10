package com.example.myapplication.Inf;

public class ChatRecord {
    private String message;
    private boolean receive;

    public ChatRecord(String message, boolean receive) {
        this.message = message;
        this.receive = receive;
    }

    public String getMessage() {
        return message;
    }

    public boolean isReceive() {
        return receive;
    }
}
