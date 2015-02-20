package com.runops.instagramviewer.model;

public class Media {

    private String id;
    private User user;
    private Caption caption;
    private Images images;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Caption getCaption() {
        return caption;
    }

    public Images getImages() {
        return images;
    }
}
