package com.example.korea_hachathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static String country = null;
    private Button English,China,Japan, Korea;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        English = (Button)findViewById(R.id.English);
        China = (Button)findViewById(R.id.China);
        Japan = (Button)findViewById(R.id.Japan);
        Korea = (Button)findViewById(R.id.Korea);


        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                country = "en";
                startActivity(intent);
            }
        });
        China.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                country = "zh-CN";
                startActivity(intent);
            }
        });
        Japan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                country = "ja";
                startActivity(intent);
            }
        });
        Korea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                country = "ko";
                startActivity(intent);
            }
        });
    }
}
