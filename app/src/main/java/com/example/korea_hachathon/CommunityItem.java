package com.example.korea_hachathon;

import android.widget.ImageView;

public class CommunityItem {
    String title;
    String content;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    String local;
    String id;

    public CommunityItem(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommunityItem(String title, String content, String local, String id) {
        this.title = title;
        this.local = local;
        this.content = content;
        this.id = id;
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
