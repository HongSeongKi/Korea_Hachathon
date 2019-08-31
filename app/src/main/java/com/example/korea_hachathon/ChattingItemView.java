package com.example.korea_hachathon;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChattingItemView extends CardView {
    TextView name;
    TextView content;
    ImageView imageView;
    ImageView proImage;

    public TextView getContent() {
        return content;
    }

    public void setContent(TextView content) {
        this.content = content;
    }

    public ChattingItemView(Context context) {
        super(context);
        init(context);
    }

    public ChattingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatting_item,this,true);
        name = (TextView)findViewById(R.id.name);
        content = (TextView)findViewById(R.id.content);
        imageView = (ImageView)findViewById(R.id.imageView);
        proImage = (ImageView)findViewById(R.id.proImage);
    }

    public void setName(String name1){
        name.setText(name1);
    }

    public void setContent(String content1){
        content.setText(content1);
    }

    public void setImageView(Uri uri){
        if(uri == null){
            imageView.setImageBitmap(null);
        }
        else{
            imageView.setImageURI(uri);
        }
    }
}


