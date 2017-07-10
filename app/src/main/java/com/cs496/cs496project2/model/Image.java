package com.cs496.cs496project2.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//이미지 정보 저장
public class Image implements Serializable {
    //사진은 서버에서 mobile/id 형식으로 지정될것
    private String owner, title, description; //owner: designated by phoneNumber
    private String id, imageUri, imageUrl;
    private Date date;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy '@' hh:mm aaa", Locale.ENGLISH);

    public Image() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    //TODO: 이미지 파일을 서버에서 가져오는 함수 구현해야


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
