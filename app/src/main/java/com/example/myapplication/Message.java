package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Message {

    private String text;
    private boolean sentByMe;
    private boolean imgflg;
    private Drawable drawable;

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
    public void setDrawable(Drawable drawable){this.drawable = drawable;}
    public Drawable getDrawable(){return drawable;}
    public boolean isSentByMe() {
        return sentByMe;
    }
}
