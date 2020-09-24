package com.feiyueve.snsdemo.inf;

public class Comment {
    private int commentIndex;
    private int commentToIndex;
    private int commentUserId;
    private String commentText;
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



    public Comment(int commentIndex, int commentToIndex, int commentUserId, String commentText) {
        this.commentIndex = commentIndex;
        this.commentToIndex = commentToIndex;
        this.commentUserId = commentUserId;
        this.commentText = commentText;
    }

}
