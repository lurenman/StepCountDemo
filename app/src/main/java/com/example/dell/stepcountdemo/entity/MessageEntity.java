package com.example.dell.stepcountdemo.entity;


public class MessageEntity {
    public int what;//标识
    public Object obj;//消息

    private MessageEntity() {

    }

    private static class Holder {
        private static final MessageEntity INSTANCE = new MessageEntity();
    }

    public static MessageEntity obtianMessage() {
        return Holder.INSTANCE;
    }
}
