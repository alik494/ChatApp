package com.example.chatapp;

public class Message {
    private String author;
    private String message;
    private long date;
    private String imageURl;

    public Message(String author, String message,long date,String imageURl) {
        this.author = author;
        this.message = message;
        this.date=date;
        this.imageURl=imageURl;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
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
