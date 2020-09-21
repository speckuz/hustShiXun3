package sse.hust.vini.scenery;

public class SceneryComment {
    private Integer commentIndex;
    private Integer commentToIndex;
    private Integer commentUserId;
    private String commentText;

    public Integer getCommentToIndex() {
        return commentToIndex;
    }

    public void setCommentToIndex(Integer commentToIndex) {
        this.commentToIndex = commentToIndex;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Integer getCommentIndex() {
        return commentIndex;
    }

    public void setCommentIndex(Integer commentIndex) {
        this.commentIndex = commentIndex;
    }
}
