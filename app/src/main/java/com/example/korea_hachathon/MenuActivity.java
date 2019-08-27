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
    String country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        translate = (Button)findViewById(R.id.translate);
        chatting = (Button)findViewById(R.id.chatting);
        community = (Button)findViewById(R.id.community);

        Intent intent = new Intent(this.getIntent());
        country = intent.getStringExtra("country");

        System.out.println("country:"+country);
        if(country.equals("1"))
        {
            translate.setText("translate");
            chatting.setText("catting");
            community.setText("community");
        }else if(country.equals(2)){

        }else if(country.equals(3)){

        }

        chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(),)
            }
        });
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommunityActivity.class);
                intent.putExtra("number",country);
                startActivity(intent);
            }
        });
    }
}
