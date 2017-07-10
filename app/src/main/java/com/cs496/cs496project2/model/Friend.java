package com.cs496.cs496project2.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Friend implements Serializable {

    private String phoneNumber, name, email;
    private String profileImageUrl;
    private Uri profileImageUri;
    private ArrayList<Image> images;
    public boolean isRegistered = false;

    public Friend(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Friend(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
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

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        isRegistered = true;
    }

    public Uri getProfileImageUri() { return profileImageUri; }

    public void setProfileImageUri(Uri profileImageUri) { this.profileImageUri = profileImageUri; }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> imageIDs) {
        this.images = imageIDs;
    }

}
