package com.cs496.cs496project2.model;

import java.io.Serializable;

public class Friend implements Serializable {

    private String phoneNumber, name, email;
    private int[] imageIDs;

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

    public int[] getImageIDs() {
        return imageIDs;
    }

    public void setImageIDs(int[] imageIDs) {
        this.imageIDs = imageIDs;
    }

}
