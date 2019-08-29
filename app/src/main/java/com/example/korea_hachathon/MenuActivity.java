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

        Intent intent = new Intent(this.getIntent());
        MainActivity.country = intent.getStringExtra("country");

        System.out.println("country:"+ MainActivity.country);
        if(MainActivity.country.equals("English"))
        {
            translate.setText("translate");
            chatting.setText("chatting");
            community.setText("community");
        }else if(MainActivity.country.equals("China")){

        }else if(MainActivity.country.equals("Japan")){

        }
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),TranslateAcitivy.class);
               intent.putExtra("country",MainActivity.country);
               startActivity(intent);
            }
        });

        chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TranslateAcitivy.class);
                intent.putExtra("country",MainActivity.country);
                startActivity(intent);
            }
        });
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommunityActivity.class);
                intent.putExtra("country",MainActivity.country);
                startActivity(intent);
            }
        });
    }
}
