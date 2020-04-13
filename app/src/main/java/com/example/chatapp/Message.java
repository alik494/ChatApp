package com.example.chatapp;

import com.firebase.client.ServerValue;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {
    private String author;
    private String message;
    private @ServerTimestamp Date timestamp;
    private String messsage_id;
    private String author_id;

    private String imageURl;

    public Message(String author, String message,Date timestamp,String imageURl, String messsage_id, String author_id) {
        this.author = author;
        this.message = message;
        this.timestamp=timestamp;
        this.imageURl=imageURl;
        this.messsage_id=messsage_id;
        this.author_id=author_id;
    }
   public Message(String author, String message,Date timestamp,String imageURl) {
        this.author = author;
        this.message = message;
        this.timestamp=timestamp;
        this.imageURl=imageURl;

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
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
