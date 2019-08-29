package com.example.korea_hachathon;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.security.Permission;
import java.util.ArrayList;


public class MakeCommunity extends AppCompatActivity {

    EditText id,title,content;
    Button image,complete;
    File tempFile;
    private static final int PICK_FROM_ALBUM = 1;
    String s_id;
    String s_title;
    String s_content;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_community);
        id = (EditText)findViewById(R.id.id);
        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        image = (Button)findViewById(R.id.image);
        complete = (Button)findViewById(R.id.complete);
        imageView = (ImageView)findViewById(R.id.imageView);
        tedPermission();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_id = id.getText().toString();
                s_title = title.getText().toString();
                s_content = title.getText().toString();
               // new JSONTask2().execute("http://172.20.10.4:3000/add");
                new JSONTask3().execute("http://192.168.123.186:3000/add");
            }
        });
    }


    public class JSONTask3 extends AsyncTask<String,String,String> {

         HttpURLConnection con = null;
         BufferedReader reader = null;

         private  String toBinary(String s){
             String temp = s;
             byte[] bytes = s.getBytes();
            StringBuilder binary = new StringBuilder();

            for(byte b : bytes)
            {
                int val =  b;
                for(int i=0;i<8;i++)
                {
                    binary.append((val&128) == 0 ? 0:1);
                    val <<= 1;
                }
                binary.append(' ');
            }
            return binary.toString();
         }

         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);
             System.out.println("onPostExecute : "+s);
             JSONObject jsonObject = null;
             String id=null,title=null,content=null,image=null;
             try {
                 jsonObject = new JSONObject(s);
                 id = (String)jsonObject.get("id");
                 title = (String)jsonObject.get("title");
                 content = (String)jsonObject.get("content");
                 image = (String)jsonObject.get("image");
             }catch (Exception e){

             }
            System.out.println("image length : "+image.length());
            File root = Environment.getExternalStorageDirectory();
            try {
                File file = new File(root.getAbsolutePath()+"/DCIM/Camera/img10.jpg");
                System.out.println("file first : "+file.length());
                byte[] buffer = image.getBytes("utf-8" + "");
                System.out.println("onPostExe : "+buffer.length);
                FileOutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                DataOutputStream output = new DataOutputStream(outputStream);
                output.writeBytes(image);

                System.out.println("file second : "+file.length());
                if(file.exists())
                {
                    System.out.println("파일 존재합니다. " + file.length()+" 경로 : "+file.getAbsolutePath());

                }
                else{
                    System.out.println("파일 존재 하지 않습니다");
                }
                getApplication().sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)) );

            }catch (Exception e){
                  e.printStackTrace();
            }
         }

         @Override
         protected String doInBackground(String... strings) {
             try {
                 JSONObject jsonObject = new JSONObject();
                 jsonObject.accumulate("id", s_id);
                 jsonObject.accumulate("title", s_title);
                 jsonObject.accumulate("content", s_content);
                 //String str = fileToBinary(tempFile);
                 //jsonObject.accumulate("image",str);
                 //System.out.println("str : "+str);
                 try {
                     String str ="";
                     FileInputStream inputStream = new FileInputStream(tempFile.getAbsoluteFile());
                     DataInputStream dis = new DataInputStream(inputStream);
                     byte[] buffer = new byte[1024];
                     System.out.println((int)tempFile.length());
                     int len ;
                     int tot_len=0;

                     while((len = dis.read(buffer)) !=-1)
                     {
                         tot_len += len;
                         System.out.println("buffer : "+buffer.toString());
                         System.out.println("buffer_length : " + buffer.length);
                         str = str + new String(buffer);
                     }

                     System.out.println("tot_len : " + tot_len);
                     System.out.println("strlength : "+str.length());
                     //System.out.println("str : "+str);
                     jsonObject.accumulate("image",str);
                 } catch (Exception e) {

                 }

                 try {
                     URL url = new URL(strings[0]);
                     con = (HttpURLConnection) url.openConnection();
                     con.setRequestMethod("POST");
                     con.setRequestProperty("Cache-Control", "no-cache");
                     con.setRequestProperty("Content-Type", "application/json");
                     con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                     con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                     con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                     con.connect();
                     OutputStream outputStream = con.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                     writer.write(jsonObject.toString());
                     writer.flush();
                     writer.close();

                     //서버로 부터 데이터를 받음
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
    public class JSONTask2 extends AsyncTask<String,String,String>{

        JSONObject jsonObject = new JSONObject();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String[] data={"id","title","content"};
            String[] dataName = {s_id,s_title,s_content};

            String boundary = "^-----^";
            String LINE_FEED = "\r\n";
            String charset = "UTF-8";
            OutputStream outputStream;
            PrintWriter writer;

            JSONObject result = null;

            try{
                result = new JSONObject();
                result.accumulate("id",s_id);
                result.accumulate("title",s_title);
                result.accumulate("content",s_content);
            }catch(Exception e){

            }
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setConnectTimeout(15000);

                outputStream = con.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(outputStream,charset),true);


                /*writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name="+ data[0]).append(LINE_FEED);
                writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.append(dataName[0]).append(LINE_FEED);
                writer.flush();*/

                writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + tempFile.getName() + "\"").append(LINE_FEED);
                writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(tempFile.getName())).append(LINE_FEED);
                writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.flush();

                JSONObject jsonObject = new JSONObject();
                FileInputStream inputStream = new FileInputStream(tempFile);
                DataInputStream dis = new DataInputStream(inputStream);
                byte[] buffer = new byte[(int)tempFile.length()];
                dis.readFully(buffer);
                dis.close();
                String str = new String(buffer);
                System.out.println("str : "+str);
                int bytesRead = -1;
                /*while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }*/
                jsonObject.accumulate("id",s_id);
                jsonObject.accumulate("title",s_title);
                jsonObject.accumulate("content",s_content);
                jsonObject.accumulate("image",str);
                writer.write(jsonObject.toString());
                outputStream.flush();
                inputStream.close();
                writer.append(LINE_FEED);
                writer.flush();

                writer.append("--" + boundary + "--").append(LINE_FEED);
                writer.close();
                System.out.println("쓰기 완료!!");

                int responseCode = con.getResponseCode();
                System.out.println("responseCode : "+responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println(response);
                    in.close();

                    return response.toString();

                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    System.out.println("response : "+response.toString());
                   return response.toString();
                }

            } catch (ConnectException e) {
                e.printStackTrace();

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }


    }



    private void tedPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri photoUri;
        if(requestCode == PICK_FROM_ALBUM){
            try {
                photoUri = data.getData();
            }
            catch (Exception e){
                photoUri = null;
            }
            System.out.println("Uri : "+photoUri);
            Cursor cursor = null;
            if(photoUri != null) {
                try {

                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();
                    System.out.println("cursor : "+cursor);
                    System.out.println("Uri : "+photoUri);
                    tempFile = new File(cursor.getString(column_index));
                    System.out.println("tempFile : "+tempFile);
                    System.out.println(tempFile.getAbsoluteFile());
                    Bitmap myBitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                System.out.println(tempFile.toString());
            }

        }
    }
}
