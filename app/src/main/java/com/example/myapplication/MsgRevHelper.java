package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MsgRevHelper extends SQLiteOpenHelper {

    public MsgRevHelper(@Nullable Context context) {
        super(context, "Chat_info.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("create table Chat_info(name varchar ,sex varchar,msg varchar,position integer,chara_photo integer,is_send_by_me varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}