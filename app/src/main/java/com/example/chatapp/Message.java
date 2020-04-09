package com.example.chatapp;

public class Message {
    private String author;
    private String message;
    private long date;

    public Message(String author, String message,long date) {
        this.author = author;
        this.message = message;
        this.date=date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    Message(){}

}
