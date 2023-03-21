package com.example.myapplication;

public class Message {

    private String text;
    private boolean sentByMe;

    public Message(String text, boolean sentByMe) {
        this.text = text;
        this.sentByMe = sentByMe;
    }

    public String getText() {
        return text;
    }


    public boolean isSentByMe() {
        return sentByMe;
    }
}
