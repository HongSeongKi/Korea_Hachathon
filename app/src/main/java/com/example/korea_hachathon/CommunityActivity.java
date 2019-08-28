package com.example.korea_hachathon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<CommunityItem> items;
    CommunityAdapter adapter;
    TextView textView;
    Button btn,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CommunityAdapter();
        items = new ArrayList<CommunityItem>();
        ;
        textView = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);

        adapter.addItem(new CommunityItem("여행후기~", null,null));
        adapter.addItem(new CommunityItem("재밌었습니다.", null,null));
        adapter.addItem(new CommunityItem("추억여행", null,null));
        adapter.addItem(new CommunityItem("하하하~", null,null));
        adapter.addItem(new CommunityItem("히히히~", null,null));

        listView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"버튼 클릭",Toast.LENGTH_SHORT).show();
                new JSONTask().execute("http://192.168.130.1:3000/users"); //전체 리스트 가저오기
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MakeCommunity.class);
                startActivity(intent);
            }
        });
    }
    class CommunityAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommunityItemView view = new CommunityItemView(getApplicationContext());
            CommunityItem item = items.get(position);
            view.setTitle(item.getTitle());
            return view;
        }

        public void addItem(CommunityItem item){
            items.add(item);
        }
    }

    class JSONTask extends AsyncTask<String,String,String> {

        HttpURLConnection con;
        BufferedReader reader;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"넘어온 데이터 : "+s,Toast.LENGTH_SHORT).show();
            //JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
            }catch(Exception e){
                System.out.println("Json 객체 만들기 실패");
            }
            String id=null,name=null;
            try{
               id =  (String)jsonObject.get("id");
               name = (String)jsonObject.get("name");
            }catch (Exception e)
            {
                System.out.println("parsing 못함");
            }

            Toast.makeText(getApplicationContext(),"id : "+id+" , name : "+name,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                System.out.println("doInBackground실행");
                //JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("id", "androidTest");
                //jsonObject.accumulate("name", "Hong");
                con = null;
                reader = null;

                try {
                    //strings[0]=strings[0]+"?id=androidTest&name=HONG";
                    URL url = new URL(strings[0]);
                    System.out.println(url);
                    System.out.println("string url : "+strings[0]);
                    con = (HttpURLConnection) url.openConnection();
                    System.out.println("con : "+con);
                    System.out.println("conHTTP");
                    con.connect(); //연결 수행

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

