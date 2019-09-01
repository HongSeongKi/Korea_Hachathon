package com.example.korea_hachathon;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private String filePath;
    private ArrayList<String> contents_list;
    private int mode;

    public NetworkTask(String url, String filePath, ArrayList<String> contents_list, int mode) {

        this.url = url;
        this.filePath = filePath;
        this.contents_list = contents_list;
        this.mode = mode;
    }

    @Override
    protected String doInBackground(Void... params) {

        String result =null; // 요청 결과를 저장할 변수.
        if(mode==1) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, filePath, contents_list); // 해당 URL로 부터 결과물을 얻어온다.
        }
        else if(mode==2){
            RequestHttpURLConnection_2 requestHttpURLConnection_2 = new RequestHttpURLConnection_2();
            result = requestHttpURLConnection_2.request(url, filePath, contents_list); // 해당 URL로 부터 결과물을 얻어온다.
        }
        else if(mode==3){
            RequestHttpURLConnection_2 requestHttpURLConnection_2 = new RequestHttpURLConnection_2();
            result = requestHttpURLConnection_2.request(url, filePath, contents_list); // 해당 URL로 부터 결과물을 얻어온다.
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println("onPostExcute에서 받은 내용 " + s);
        if(this.mode == 2)
        {
            Gson gson = new Gson();
            Elements[] elements= gson.fromJson(s, Elements[].class);
            CommunityActivity.adapter.clean();
            CommunityActivity.items_2.clear();
            for(int i=0; i<elements.length; i++)
            {
                System.out.println("@@@idid" + elements[i]._id);

                CommunityActivity.items_2.add(new CommunityItem("["+elements[i].local.toUpperCase()+"] " + elements[i].title, elements[i].content, elements[i].local ,elements[i]._id));
                CommunityActivity.adapter.addItem(new CommunityItem("["+elements[i].local.toUpperCase()+"] " + elements[i].title, elements[i].content, elements[i].local ,elements[i]._id));
            }
            CommunityActivity.listView.setAdapter(CommunityActivity.adapter);


        }else if(this.mode == 3)
        {

            JSONObject jsonObject = null;
            String raw_data = null;
            try {
                jsonObject = new JSONObject(s);
                raw_data = (String) jsonObject.get("image");
                DetailActivity.title.setText(jsonObject.get("title").toString());
                DetailActivity.content.setText(jsonObject.get("content").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if((raw_data == null) || (raw_data.equals("none")))
                return;

            raw_data = raw_data.replace("@", "\n");
            raw_data = raw_data.replace("#", "+");
            byte [] decoded_byte = Base64.decode(raw_data, Base64.DEFAULT);
            System.out.println("변환한바이트배열길이" + decoded_byte.length);

            String file_path = Environment.getExternalStorageDirectory() + "/test.png";

            File file = new File(file_path);

            if( file.exists() ) {
                if (file.delete()) {
                    System.out.println("파일삭제 성공");
                    file = new File(file_path);
                } else {
                    System.out.println("파일삭제 실패");
                }
            }

            try {
                FileOutputStream output = new FileOutputStream(file);
                output.write(decoded_byte, 0, decoded_byte.length);
                output.flush();
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.parse("file:///" + file_path );
            System.out.println(uri);

            DetailActivity.image.setImageURI(uri);
//            CommunityDetail.imageView.setImageURI(uri);

        }
    }

    class Elements{
        String _id;
        String local;
        String title;
        String image;
        String content;
        String createdAt;
        String __v;
    }

}