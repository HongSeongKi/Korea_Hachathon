package com.example.korea_hachathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChattingActivity extends AppCompatActivity {
    Button[] btn ;
    public static String local=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        btn = new Button[5];
        btn[0] = (Button)findViewById(R.id.seoul);
        btn[1] = (Button)findViewById(R.id.kyeongju);
        btn[2] = (Button)findViewById(R.id.busan);
        btn[3] = (Button)findViewById(R.id.jeonju);
        btn[4] = (Button)findViewById(R.id.jeju);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.seoul:
                Intent intent = new Intent(getApplicationContext(),RealChattingActivity.class);
                local = "seoul";
                startActivity(intent);
                break;
            case R.id.kyeongju:
                Intent intent2 = new Intent(getApplicationContext(),RealChattingActivity.class);
                local = "kyeongju";
                startActivity(intent2);
                break;
            case R.id.busan:
                Intent intent3 = new Intent(getApplicationContext(),RealChattingActivity.class);
                local = "busan";
                startActivity(intent3);
                break;
            case R.id.jeonju:
                Intent intent4 = new Intent(getApplicationContext(),RealChattingActivity.class);
                local = "jeonju";
                startActivity(intent4);
                break;
            case R.id.jeju:
                Intent intent5 = new Intent(getApplicationContext(),RealChattingActivity.class);
                local = "jeju";
                startActivity(intent5);
                break;

        }
    }




}
