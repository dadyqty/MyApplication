package com.example.myapplication;

public class Message {

    private String text;
    private boolean sentByMe;
    private boolean imgflg;

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

    public boolean isSentByMe() {
        return sentByMe;
    }
}
