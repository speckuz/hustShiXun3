package com.feiyueve.snsdemo.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    //首次创建数据库的时候调用，一般可以执行建库，建表的操作
    //Sqlite没有单独的布尔存储类型，它使用INTEGER作为存储类型，0为false，1为true
    public void onCreate(SQLiteDatabase db) {
        //user table
        db.execSQL("create table if not exists loginData(userName text primary key," +
                "userId text," +
                "loginStatus text," +
                "userKey text," +
                "thumbnail text)");
    }

    @Override//当数据库的版本发生变化时，会自动执行
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createFriendTable(SQLiteDatabase db,String userName){
        db.execSQL("create table if not exists "+ userName+"Friend(friendName text," +
                "isTop text," +
                "grouping text," +
                "friendHead text," +
                "aliasName text," +
                "friendNickName text," +
                "friendId text primary key," +
                "gender text," +
                "birthday text," +
                "location text," +
                "fileName text," +
                "chatText text," +
                "receiver text,"+
                "chatTextType text," +
                "fileLength INTEGER," +
                "isRead text," +
                "chatTimeText text)");
    }

    public void createChatMessageTable(SQLiteDatabase db,String userName,String chatObject){
        String tableName = userName+"_"+chatObject;
        db.execSQL("create table if not exists "+ tableName+"(chatTextType text," +
                "friendId text,"+
                "receiver text,"+
                "friendNickName text," +
                "chatText text,"+
                "fileName text," +
                "isRead text," +
                "fileLength INTEGER," +
                "chatTimeText text)");
    }

    public void insert(SQLiteDatabase db, String tableName, ContentValues values) {
        db.replace(tableName, null, values);
    }

    public void delete(SQLiteDatabase db, String tableName, String where, String[] args){
        db.delete(tableName,where+"=?",args);
    }

    public void update(SQLiteDatabase db, String tableName, ContentValues values, String where, String[] args){
        db.update(tableName,values,where+"=?",args);
    }

    public Cursor find(SQLiteDatabase db, String tableName,String where,String[] args){
        Cursor cursor = db.query(tableName, null, where +" = ?", args, null, null, null);
        return cursor;
    }
}
