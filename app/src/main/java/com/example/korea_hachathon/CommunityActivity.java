package com.example.korea_hachathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<CommunityItem> items;
    CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new CommunityAdapter();
        items = new ArrayList<CommunityItem>();;

        adapter.addItem(new CommunityItem("여행후기~",null));
        adapter.addItem(new CommunityItem("재밌었습니다.",null));
        adapter.addItem(new CommunityItem("추억여행",null));
        adapter.addItem(new CommunityItem("하하하~",null));
        adapter.addItem(new CommunityItem("히히히~",null));

        listView.setAdapter(adapter);
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
}
