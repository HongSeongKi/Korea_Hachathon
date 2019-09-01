package com.example.korea_hachathon;

import android.os.Environment;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RequestHttpURLConnection {

    public String request(String _url, String _filePath, ArrayList<String> contents_list) {

        String _local = contents_list.get(0);
        String _title = contents_list.get(1);
        String _content = contents_list.get(2);
        String _image = contents_list.get(3);

        HttpURLConnection urlConn = null;

        try {

            System.out.println(_url);
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            OutputStream os = urlConn.getOutputStream();

            String myParams = "local=" + _local +
                    "&title=" + _title +
                    "&content=" + _content +
                    "&image=";

            os.write(myParams.getBytes("UTF-8"));

            if (_image.equals("none"))
            {
                String none_message = "none";
                os.write(none_message.getBytes("UTF-8"));
            }
            else
            {
                File file = new File(_filePath);

                int file_length = Integer.parseInt(String.valueOf(file.length()));
                System.out.println("파일길이 : " + file_length);

                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);

                byte[] image_data = new byte[file_length];
                String encoded_image_string = null;

                dis.readFully(image_data);

                encoded_image_string = Base64.encodeToString(image_data, Base64.DEFAULT);
                System.out.println("문자열총길이 : " + encoded_image_string.length());

                String encoded_string = Base64.encodeToString(image_data, Base64.DEFAULT);

                encoded_string = encoded_string.replace("\n", "@");
                encoded_string = encoded_string.replace("+", "#");

                System.out.println(encoded_string);
                os.write(encoded_string.getBytes());
            }
            os.flush();
            os.close();
            System.out.println("전송완료");


            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                return "failure";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String line;
            String page = "default";

            while ((line = reader.readLine()) != null) {
                page += line;
            }

            return page;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }
}