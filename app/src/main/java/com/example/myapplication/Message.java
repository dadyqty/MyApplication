package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Message {

    private String text;
    private boolean sentByMe;
    private boolean imgflg;
    private Bitmap bitmap;

    public Message(String text, boolean sentByMe,boolean imgflg) {
        this.text = text;
        this.sentByMe = sentByMe;
        this.imgflg = imgflg;
    }

    public String getText() {
        return text;
    }
    public boolean getimgflg() {
        return imgflg;
    }
    public void setBitmap(Bitmap bitmap){this.bitmap = bitmap;}
    public Bitmap getBitmap(){return bitmap;}
    public boolean isSentByMe() {
        return sentByMe;
    }
}
