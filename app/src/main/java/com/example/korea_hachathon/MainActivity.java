package com.example.korea_hachathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static String country = null;
    private Button English,China,Japan;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        English = (Button)findViewById(R.id.English);
        China = (Button)findViewById(R.id.China);
        Japan = (Button)findViewById(R.id.Japan);

        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                intent.putExtra("country","English");
                country = "English";
                startActivity(intent);
            }
        });
        China.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                intent.putExtra("country","China");
                country = "China";
                startActivity(intent);
            }
        });
        Japan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MenuActivity.class);
                intent.putExtra("country","Japan");
                country = "Japan";
                startActivity(intent);
            }
        });
    }
}
