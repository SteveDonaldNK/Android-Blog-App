package com.example.application;
public class DataModel {
    String userName;
    String postContent;
    String readTime;
    String publishTime;
    String postid;

    public DataModel(String userName, String postContent, String readTime, String publishTime, String postid) {
        this.userName = userName;
        this.postContent = postContent;
        this.readTime = readTime;
        this.publishTime = publishTime;
        this.postid = postid;
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

    public String getPostid() {
        return postid;
    }
}
