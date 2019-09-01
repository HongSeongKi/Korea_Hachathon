package com.example.korea_hachathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button translate;
    private Button chatting;
    private Button community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        translate = (Button)findViewById(R.id.translate);
        chatting = (Button)findViewById(R.id.chatting);
        community = (Button)findViewById(R.id.community);



        System.out.println("country:"+ MainActivity.country);

            translate.setText("translate");
            chatting.setText("chatting");
            community.setText("community");

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),TranslateAcitivy.class);
               startActivity(intent);
            }
        });

        chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChattingActivity.class);
                startActivity(intent);
            }
        });
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommunityActivity.class);
                startActivity(intent);
            }
        });
    }
}
