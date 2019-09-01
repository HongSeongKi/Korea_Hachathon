package com.example.korea_hachathon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static TextView title, content;
    public static ImageView image;

    ListView listView;
    ScrollView sv;
    View view;
    public  ArrayList<String> list=new ArrayList<String>();
    public static ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final View header = getLayoutInflater().inflate(R.layout.header, null, false) ;

        title = (TextView) header.findViewById(R.id.header_title);
        content = (TextView)header.findViewById(R.id.header_content);
        image = (ImageView)header.findViewById(R.id.header_imgae);

        list.add("좋아연");
        list.add("굿");

        listView = (ListView)findViewById(R.id.listview);
//
//        final View header = getLayoutInflater().inflate(R.layout.header, null, false);

        listView.addHeaderView(header);

        adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,list);

        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);

        final EditText edt = (EditText)findViewById(R.id.chat_EditText);
        Button btnadd = (Button)findViewById(R.id.btncoment);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edt.getText().toString();
                list.add(str);
                adapter.notifyDataSetChanged();
                edt.setText("");
            }
        });


        Intent intent = new Intent(this.getIntent());
        String id = intent.getStringExtra("target_id");
        System.out.println("@@@id : " + id);

        NetworkTask networkTask = new NetworkTask(MyGlobals.getInstance().getDB_Url() + "/showone/" + id , null, null, 3);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            networkTask.execute();
        }
    }
}



