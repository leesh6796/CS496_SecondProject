package com.cs496.cs496project2.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Friend implements Serializable {

    private String phoneNumber, name, email;
    private String profileImageUrl;
    private ArrayList<Image> images;

    public Friend(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() { return profileImageUrl; }

    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> imageIDs) {
        this.images = imageIDs;
    }

}
