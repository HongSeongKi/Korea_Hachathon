package com.example.korea_hachathon;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;


public class MakeCommunity extends AppCompatActivity {

    EditText id,title,content;
    Uri photoUri;
    ImageView imageView;
    Button camera,complete;
    File tempFile = null;
    private static final int PICK_FROM_ALBUM = 1;
    private Spinner spinner2;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_community);
        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        camera= (Button)findViewById(R.id.camera);
        complete = (Button)findViewById(R.id.complete);
        imageView = (ImageView)findViewById(R.id.image);

        tedPermission();

        arrayList = new ArrayList<>();
        arrayList.add("Seoul");
        arrayList.add("Jeonju");
        arrayList.add("Jeju");
        arrayList.add("Busan");
        arrayList.add("Gyeongju");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,arrayList);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setAdapter(arrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView)adapterView.getChildAt(0)).setTextSize(25);
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
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

                String s_local = spinner2.getSelectedItem().toString();
                String s_title = title.getText().toString();
                String s_content = content.getText().toString();
                title = (EditText)findViewById(R.id.title);

                String file_path = "none";

                if(tempFile != null) {
                    file_path = tempFile.toString();
                }

                ArrayList<String> contents_array = new ArrayList<String>();
                contents_array.add(s_local);
                contents_array.add(s_title);
                contents_array.add(s_content);
                contents_array.add(file_path);

                NetworkTask networkTask = new NetworkTask(MyGlobals.getInstance().getDB_Url(), file_path, contents_array, 1);

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
                    networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    networkTask.execute();
                }

                finish();
            }
        });


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

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                System.out.println(tempFile.toString());


                Uri uri = Uri.parse("file:///" + tempFile.toString());
                imageView.setImageURI(uri);
            }

        }
    }
}
