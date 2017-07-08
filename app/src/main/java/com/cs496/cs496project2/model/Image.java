package com.cs496.cs496project2.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Image implements Serializable {
    //사진은 서버에서 mobile/id 형식으로 지정될것
    private String owner, title, description; //owner: designated by phoneNumberView
    private int id;
    private Date date;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy '@' hh:mm aaa", Locale.ENGLISH);

    public Image(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public Long getTime() {
        return date.getTime();
    }

    public void setTime(Long time) {
        this.date = new Date(time);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
