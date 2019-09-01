package com.example.korea_hachathon;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class RealChattingActivity extends AppCompatActivity {
    private Handler mHandler;
    private Socket socket;
    private BufferedReader networkReader;
    private BufferedWriter networkWriter;
    private static final int PICK_FROM_ALBUM = 1;
    public String globalLine;
    public String global;
    private String ip = "172.20.200.121";
    private int port = 9100;
    private ListView listView;
    AlertDialog.Builder ad;
    String myNickName = MyGlobals.getInstance().getNickName();
    Button send, camera;
    EditText chat_edit,editText;
    ChattingAdapter adapter ;
    String check = null;
    File tempFile;
    Uri photoUri ;
    String imageString;
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            try {
                photoUri = data.getData();
            } catch (Exception e) {
                photoUri = null;
            }
            if (photoUri != null) {
                Cursor cursor = null;
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

                    tempFile = new File(cursor.getString(column_index));
                    System.out.println("tempFile : "+tempFile.toString());

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                Thread connect3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String str = tempFile.toString();
                        System.out.println(str);
                        imageString = str.split("/0/")[1];
                        System.out.println(imageString);

                        PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(),true);
                        File file = new File(Environment.getExternalStorageDirectory()+"/"+imageString);
                        int file_length = (int)file.length();
                        out.println(myNickName+"@image_send_client_to_server@test_image.jpg@"+file_length);

                        try{
                            FileInputStream fis = new FileInputStream(file);
                            DataInputStream dis = new DataInputStream(fis);
                            DataOutputStream dos = new DataOutputStream(MyGlobals.getInstance().getSocket().getOutputStream());

                            int len;
                            int total_len = 0;
                            int size = 1024;
                            byte[] image_data = new byte[file_length];
                            //System.out.println("image_data@@@@@@@@@" + image_data.length);
                            /*dis.readFully(image_data);
                            System.out.println("image_data@@@@@@@@@" + image_data.length);

                            dos.write(image_data,0,image_data.length);
                            dos.flush();
                            dos.close();*/
                            while((len = dis.read(image_data)) != -1 )
                            {
                                System.out.println("len : "+len);
                                total_len+=len;
                                dos.write(image_data,0,len);
                                dos.flush();
                                if(total_len>=file_length)
                                    break;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }


                        out.println(myNickName+"@image_send_server_to_client@test_image.jpg@"+ChattingActivity.local); //사진 전송시 지역정보 보냄
                    }
                });
                connect3.start();
            }
        }
    }


   /* @Override
    protected void onPause() {
        super.onPause();
        MyGlobals.getInstance().setSocket(null);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect_and_check.interrupt();
    }*/


    @Override
    protected void onStop() {
        super.onStop();
        if(MyGlobals.getInstance().getFlag() == 1) {
            System.out.println("onStop 호출됨");
            MyGlobals.getInstance().setStop(1);
            connect_and_check.interrupt();
            MyGlobals.getInstance().setCurrent_connect(null);
            MyGlobals.getInstance().setSocket(null);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MyGlobals.getInstance().setFlag(1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_chatting);

        System.out.println("onCreate 호출됨@@@@@@@@@@@@@@@@@@");
        MyGlobals.getInstance().setStop(0);
        send = (Button)findViewById(R.id.send);
        camera = (Button)findViewById(R.id.camera);
        chat_edit = (EditText)findViewById(R.id.chat_EditText);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ChattingAdapter();

        if(MyGlobals.getInstance().getCurrent_connect()== null) {
            MyGlobals.getInstance().setCurrent_connect(connect_and_check);
            connect_and_check.start(); //소켓 설정 및 쓰레드 시작
        }
        else{
            MyGlobals.getInstance().getCurrent_connect().interrupt();
            connect_and_check.start();
            MyGlobals.getInstance().setCurrent_connect(connect_and_check);
        }


        tedPermission();

            ad = new AlertDialog.Builder(RealChattingActivity.this);


            ad.setTitle("NICK NAME?");
            editText = new EditText(RealChattingActivity.this);
            ad.setView(editText);
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Thread connect = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(), true);
                            myNickName = editText.getText().toString();
                            MyGlobals.getInstance().setNickName(editText.getText().toString());
                            out.println(MyGlobals.getInstance().getNickName() + "#" + ChattingActivity.local);
                        }
                    });
                    connect.start();

                }
            });

            ad.show();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread connect2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(chat_edit.getText().toString() != null && !chat_edit.getText().toString().equals(""))
                        {
                            PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(),true);
                            out.println(myNickName+"@normal_chatting@" + chat_edit.getText().toString()+ "&" + MainActivity.country + "@"+ChattingActivity.local);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chat_edit.setText("");
                                    System.out.println("종료됨!@@@@@@@@@@@@@@@@@@@@@@@@");
                                }
                            });
                            return;
                        }
                    }
                });
                connect2.start();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGlobals.getInstance().setFlag(0);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(adapter.getCount() - 1);

    }

    public void tedPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }





   public Thread connect_and_check = new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                setSocket(ip,port);
            }catch(IOException e){
                e.printStackTrace();
            }


            String line = "";
            int file_num = 0;
            while(true && (MyGlobals.getInstance().getStop() == 0)){
                try{
                    System.out.println("network Reader : "+MyGlobals.getInstance().getNetworkReader());
                    line = MyGlobals.getInstance().getNetworkReader().readLine();
                }catch(IOException e){
                    e.printStackTrace();
                }

                globalLine = line;
                System.out.println("globalLine : "+globalLine);
//                System.out.println(globalLine.split("@")[3]);
                    if(globalLine.split("@")[1].equals("image_send_server_to_client") && globalLine.split("@")[4].trim().equals(ChattingActivity.local)) //지역정보 넣어야 한다.
                    {
                    String temp_name = globalLine.split("@")[2];
                    String file_name = temp_name.split("\\.")[0] + "_" + file_num + "." + temp_name.split("\\.")[1];
                    String file_path = Environment.getExternalStorageDirectory() + "/" + file_name;
                    File file = new File(file_path);
                    System.out.println("file path : "+file.toString());
                    FileOutputStream output = null;


                        int max_size=Integer.parseInt(globalLine.split("@")[3]); // file_size;
                        System.out.println("max_size : "+max_size);
                       // byte buf[] = new byte[max_size];

                        try{
                            output = new FileOutputStream(file);
                        InputStream is = socket.getInputStream();
                        DataInputStream dis = new DataInputStream(is);
                        System.out.println("파일 입력 준비 ");
                        PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(),true);
                        out.println("ready");
                        byte[] buf = new byte[1024];
                        int len;
                        int total_len=0;
                        while((len=dis.read(buf)) != -1)
                        {
                          System.out.println("len : "+len);
                          total_len += len;
                         output.write(buf,0,len);
                         output.flush();
                         if(total_len>=max_size) {
                             System.out.println("tot@@@@@@@@@@ : "+total_len);
                             output.close();
                             break;
                         }
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                        System.out.println("이미지 줍니당~~11111@@@@@@@@@@@@@@@@@@");
                    final String inner_name = file_name;
                    file_num++;
                        System.out.println("이미지 줍니당~~22222222@@@@@@@@@@@@@@@@");
                        System.out.println("globalLin55e : "+globalLine);
                        global = globalLine.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("global : "+global);
                            System.out.println("Chatting@@@@@ : "+ChattingActivity.local);
                            System.out.println(globalLine.split("@")[4].trim().equals(ChattingActivity.local));
                            if(globalLine.split("@")[4].trim().equals(ChattingActivity.local)) {
                                System.out.println("이미지 줍니당~~3333333333@@@@@@@@@@@@@@@@@@");
                                Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/" + inner_name);
                                ChattingItem item = new ChattingItem(globalLine.split("@")[0], null, uri);//이름,내용,사진

                                adapter.addItem(item);
                                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                listView.setSelection(adapter.getCount() - 1);
                            }
                        }
                    });


                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("global : "+global);
                            System.out.println("Chatting@@@@@ : "+ChattingActivity.local);
                            System.out.println(globalLine.split("@")[4].trim().equals(ChattingActivity.local));
                            if(globalLine.split("@")[4].trim().equals(ChattingActivity.local)) {

                                System.out.println("이미지 줍니당~~3333333333@@@@@@@@@@@@@@@@@@");
                                Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/" + inner_name);
                                ChattingItem item = new ChattingItem(globalLine.split("@")[0], "", uri);//이름,내용,사진
                                adapter.addItem(item);
                                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                listView.setSelection(adapter.getCount() - 1);
                            } //지역정보가 동일 해야지만 받는다
                        }
                    });*/
                }
                else if(globalLine.split("@")[1].equals("normal_chatting"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("3 : @@@@@@@@"+globalLine);
                            System.out.println("3 : !!!!!!!!"+globalLine.split("@")[3]);
                            System.out.println(ChattingActivity.local);
                            if(globalLine.split("@")[3].trim().equals(ChattingActivity.local)) {
                                System.out.println("들어갔씁니다~");
                                System.out.println("content : "+globalLine.split("@")[2]);
                                ChattingItem item;
                                if(globalLine.split("@")[2].contains("&"))
                                {
                                    System.out.println("통과전" + globalLine.split("@")[2].split("&")[1]);
                                    item = new ChattingItem(globalLine.split("@")[0],
                                            MyGlobals.getInstance().translate(globalLine.split("@")[2].split("&")[0],"en", "ko"),null);//유저이름,내용입력,사진은 -1
//                                    item = new ChattingItem(globalLine.split("@")[0],
//                                            MyGlobals.getInstance().translate(globalLine.split("@")[2].split("&")[0],
//                                                  "ko", "ko"),null);//유저이름,내용입력,사진은 -1
                                    adapter.addItem(item);
                                    System.out.println("통과후" + globalLine.split("@")[2].split("&")[1]);
                                }else
                                {
                                    item = new ChattingItem(globalLine.split("@")[0], globalLine.split("@")[2],null);//유저이름,내용입력,사진은 -1
                                    adapter.addItem(item);
                                }
                                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                listView.setSelection(adapter.getCount() - 1);
                            }
                        }
                    });
                }
                else if(globalLine.split("@")[1].equals("already_exist")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ad.show();
                            Toast.makeText(getApplicationContext(),"already_exist",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }
    });

    public void setSocket(String ip, int port) throws IOException{

        try{
            if(MyGlobals.getInstance().getSocket() != null) {
                socket = MyGlobals.getInstance().getSocket();
                networkWriter = MyGlobals.getInstance().getNetworkWriter();
                networkReader = MyGlobals.getInstance().getNetworkReader();
            }
            else {
                socket = new Socket(ip, port);
                networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("Socket : " + socket);
                System.out.println("writer : " + networkWriter);
                System.out.println("reader : " + networkReader);
                MyGlobals.getInstance().setSocket(socket);
                MyGlobals.getInstance().setNetworkWriter(networkWriter);
                MyGlobals.getInstance().setNetworkReader(networkReader);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //adapter 정의
    class ChattingAdapter extends BaseAdapter {
        ArrayList<ChattingItem> items = new ArrayList<ChattingItem>();

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

        public void addItem(ChattingItem item){
            items.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChattingItemView view = null;
            if(convertView == null)
                view = new ChattingItemView(getApplicationContext());
            else
                view = (ChattingItemView)convertView;
            ChattingItem item = items.get(position);
            if(item.getContent()==null){
                view.getContent().setBackgroundColor(Color.parseColor("#AAD0DC"));
            }
            view.setName(item.getName());
            view.setContent(item.getContent());
            view.setImageView(item.getUri());
            return view;
        }
    }
}
