package com.cs496.cs496project2.model;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.cs496.cs496project2.helper.ServerRequest;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friend implements Serializable {

    private String phoneNumber, name, email;
    private String profileImageUrl;
    private ArrayList<Image> images;

    public Friend(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Friend(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }
    public Friend(String phoneNumber, String name, String profileImageUrl) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
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

    public String getProfileImageUrl() { return profileImageUrl; }

    public ArrayList<Image> fetchImages(final Context context) {
        images = new ArrayList<Image>();
        (new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<String> urls = (new ServerRequest(context)).getGallery(phoneNumber);
                for (String url : urls) {
                    Image image = new Image();
                    image.setImageUrl(url);
                    images.add(image);
                }
                return null;
            }
        }).execute();
        return images;
    }

    public void setImages(ArrayList<Image> imageIDs) {
        this.images = imageIDs;
    }


    public boolean isRegistered(final Context context) {
        boolean flag = false;
        (new AsyncTask<Void,Void,Void>() {
            @Override
            protected boolean doInBackground(Void... voids) {
                flag =  (new ServerRequest(context)).getProfileImageUrl(phoneNumber) != null);
            }
            @Override
            protected void onPostExecute(boolean temp) {
                flag = temp;
            }
        }).execute();

    }

}
