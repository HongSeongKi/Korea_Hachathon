package com.example.korea_hachathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WriteActivity extends AppCompatActivity {

    private Button pencil;
    private Button erase;
    private Button send;
    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        pencil = (Button) findViewById(R.id.pencil);
        erase = (Button) findViewById(R.id.erase);
        send = (Button) findViewById(R.id.send);
        drawView = (DrawView) findViewById(R.id.drawView);

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.flag = 1;
            }
        });
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.flag = 0;
                drawView.reset();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawView.getPoints().size()!=0) {
                    drawView.send(WriteActivity.this);
                    drawView.getPoints().clear();
                    drawView.invalidate();
                }
            }
        });
    }
}

