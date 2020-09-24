package com.example.myapplication.inf;

public class Comment {
    private int commentIndex;
    private int commentToIndex;
    private int commentUserId;
    private String commentText;
    private String commentUserName;
    private String commentToUserName;

    public String getCommentToUserName() {
        return commentToUserName;
    }

    public void setCommentToUserName(String commentToUserName) {
        this.commentToUserName = commentToUserName;
    }



    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public int getCommentToIndex() {
        return commentToIndex;
    }

    public int getCommentUserId() {
        return commentUserId;
    }

    public String getCommentText() {
        return commentText;
    }



    public Comment(int commentIndex,String commentUserName,String commentToUserName, int commentToIndex, int commentUserId, String commentText) {
        this.commentIndex = commentIndex;
        this.commentUserName = commentUserName;
        this.commentToUserName = commentToUserName;
        this.commentToIndex = commentToIndex;
        this.commentUserId = commentUserId;
        this.commentText = commentText;
    }

}
