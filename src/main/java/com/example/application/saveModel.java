package com.example.application;

public class saveModel {
    String userName;
    String postContent;
    String readTime;
    String publishTime;

    public saveModel(String userName, String postContent, String readTime, String publishTime) {
        this.userName = userName;
        this.postContent = postContent;
        this.readTime = readTime;
        this.publishTime = publishTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getReadTime() {
        return readTime;
    }
}
