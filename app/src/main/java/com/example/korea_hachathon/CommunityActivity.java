package com.example.korea_hachathon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    public static ListView listView;
    ArrayList<CommunityItem> items;
    public static ArrayList<CommunityItem> items_2;
    public static CommunityAdapter adapter;
    public static CommunityAdapter_2 adapter_2;
    Button write;
    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        arrayList = new ArrayList<String>();
        arrayList.add("TOTAL");
        arrayList.add("SEOUL");
        arrayList.add("JEONJU");
        arrayList.add("JEJU");
        arrayList.add("BUSAN");
        arrayList.add("GYEONGJU");


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CommunityAdapter();
        adapter_2 = new CommunityAdapter_2();
        items = new ArrayList<CommunityItem>();
        items_2 = new ArrayList<CommunityItem>();
        write = (Button) findViewById(R.id.write);
        spinner = (Spinner) findViewById(R.id.spinner);

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, arrayList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter.addItem(new CommunityItem("여행후기~", "너모 재밋죵", "id"));
//        adapter.addItem(new CommunityItem("앙~기모찌~", "너모 재밋죵", "id"));
//        adapter.addItem(new CommunityItem("내가짱이야~", "너모 재밋죵", "id"));
//        adapter.addItem(new CommunityItem("헿~", "너모 재밋죵", "id"));

        spinner.setAdapter(arrayAdapter);
        listView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                System.out.println(arrayList.get(i));

                if(arrayList.get(i).toLowerCase().equals("total")) {
                    items.clear();
                    items_2.addAll(items);
                    listView.setAdapter(adapter);
                }
                else {
                    items.clear();
                    for(int j=0; j<items_2.size();j++) {
                        if (arrayList.get(i).toLowerCase().equals(items_2.get(j).local.toLowerCase())) {

                            CommunityItem temp_item = new CommunityItem(items_2.get(j).title, items_2.get(j).content, items_2.get(j).local, items_2.get(j).id);
                            items.add(temp_item);
                        }
                    }
                    listView.setAdapter(adapter);
                }

                if (adapterView.getItemAtPosition(i).equals(arrayList.get(4))) {
                    ((TextView) adapterView.getChildAt(0)).setTextSize(12);
                } else {
                    ((TextView) adapterView.getChildAt(0)).setTextSize(17);
                }
                ((TextView) adapterView.getChildAt(0)).setTypeface(((TextView) adapterView.getChildAt(0)).getTypeface(), Typeface.BOLD);
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String target_id = adapter.getItem(position).getId();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                System.out.println("targetid : " + target_id);
                intent.putExtra("target_id", target_id);
                startActivity(intent);

            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeCommunity.class);
                startActivity(intent);
            }
        });


        String url = MyGlobals.getDB_Url() + "/showlist";

        NetworkTask networkTask = new NetworkTask(url, null, null, 2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            networkTask.execute();
        }
    }


    class CommunityAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CommunityItem getItem(int position) {
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
            if (view != null) {
                view.setBackgroundColor(Color.parseColor("#AAD0DC"));
            }
            view.setTitle(item.getTitle());
            view.setContent(item.getContent());
            return view;
        }

        public void addItem(CommunityItem item) {
            items.add(item);
        }


        public void clean() {
            items.clear();
        }
    }

    class CommunityAdapter_2 extends BaseAdapter {
        @Override
        public int getCount() {
            return items_2.size();
        }

        @Override
        public CommunityItem getItem(int position) {
            return items_2.get(position);
        }

        @Override
        public long getItemId(int position2) {
            return position2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommunityItemView_2 view2 = new CommunityItemView_2(getApplicationContext());
            CommunityItem item_2 = items.get(position);
            if (view2 != null) {
                view2.setBackgroundColor(Color.parseColor("#AAD0DC"));
            }
            view2.setTitle(item_2.getTitle());
            view2.setContent(item_2.getContent());
            return view2;
        }

        public void addItem(CommunityItem item) {
            items_2.add(item);
        }


        public void clean() {
            items_2.clear();
        }
    }


//    protected void onResume() {
//        super.onResume();
//        String url = MyGlobals.getDB_Url() + "/showlist";
////        adapter.clean();
//
//        NetworkTask networkTask = new NetworkTask(url, null, null, 2);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            networkTask.execute();
//        }
//    }
}
