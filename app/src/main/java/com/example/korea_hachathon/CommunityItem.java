package com.example.korea_hachathon;

public class CommunityItem {
    String title;
    String content;

    public CommunityItem(){

    }

    public CommunityItem(String title, String content) {
        this.title = title;
        this.content = content;
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

}
