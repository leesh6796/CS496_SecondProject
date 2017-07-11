package com.cs496.cs496project2.helper;

/**
 * Created by memorial on 2017. 7. 12..
 */

public class GuestBookItem {
    private String name;
    private String profilePictureURL;
    private String content;
    private boolean isSecret;

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePictureURL(String url) {
        this.profilePictureURL = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSecret(boolean value) { this.isSecret = value; }

    public String getName() {
        return this.name;
    }

    public String getProfilePictureURL() {
        return this.profilePictureURL;
    }

    public String getContent() {
        return content;
    }

    public boolean isSecret() {
        return this.isSecret;
    }
}
