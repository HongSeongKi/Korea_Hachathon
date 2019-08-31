package com.example.korea_hachathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TranslateAcitivy extends AppCompatActivity {

    Button speak,write;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_acitivy);

        Intent intent = new Intent(this.getIntent());
        String data = intent.getStringExtra("country");
        System.out.println("data : "+data);
        speak = (Button)findViewById(R.id.speak);
        write = (Button)findViewById(R.id.write);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                intent.putExtra("country",MainActivity.country);
                startActivity(intent);
            }
        });
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SpeakActivity.class);
                startActivity(intent);
            }
        });
    }
}
