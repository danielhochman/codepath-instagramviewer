package com.runops.instagramviewer.model;

import java.util.List;

public class Comments {
    private Long count;
    private List<Comment> data;

    public Long getCount() {
        return count;
    }

    public List<Comment> getCommentList() {
        return data;
    }
}
