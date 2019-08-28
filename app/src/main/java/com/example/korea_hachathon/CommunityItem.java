package com.example.korea_hachathon;

public class CommunityItem {
    String title;
    String content;
    String commentlength;

    public CommunityItem(){

    }

    public CommunityItem(String title, String content,String commentlength) {
        this.title = title;
        this.content = content;
        this.commentlength = commentlength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentlength() {
        return commentlength;
    }

    public void setCommentlength(String commentlength) {
        this.commentlength = commentlength;
    }
}
