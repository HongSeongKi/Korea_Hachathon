package com.example.korea_hachathon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

public class CommunityItemView extends CardView {
    TextView title;

    public CommunityItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommunityItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.community_item,this,true);
        title = (TextView)findViewById(R.id.title);
    }

    public void setTitle(String text){
        title.setText(text);
    }
}
