package com.feiyueve.snsdemo.list;

import android.graphics.Bitmap;

public class AccountList {
    private String userName;
    private Bitmap bitmap;

    public AccountList(String userName, Bitmap bitmap) {
        this.userName = userName;
        this.bitmap = bitmap;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
