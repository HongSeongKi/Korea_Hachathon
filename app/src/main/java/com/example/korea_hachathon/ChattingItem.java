package com.example.korea_hachathon;

import android.net.Uri;

public class ChattingItem {
    String name;
    String content;
    Uri uri;

    public ChattingItem(String name , String content, Uri uri){
        this.name = name;
        this.content = content;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
