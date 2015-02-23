package com.runops.instagramviewer.model;

public class User {

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String profile_picture;

    public String getUsername() {
        return username;
    }

    public String getProfilePicture() {
        return profile_picture;
    }
}
