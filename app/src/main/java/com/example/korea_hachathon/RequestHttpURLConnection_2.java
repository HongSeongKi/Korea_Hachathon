package com.example.korea_hachathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RequestHttpURLConnection_2 {

    public String request(String _url, String _filePath, ArrayList<String> contents_list) {

        HttpURLConnection urlConn = null;

        try {
            String newURL = _url; //showone 수행
            URL url = new URL(newURL);
            urlConn = (HttpURLConnection) url.openConnection();

            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            OutputStream os = urlConn.getOutputStream();
            os.flush();
            os.close();

            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return "failure";

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String jsonData = "";
            String total_data = "";
            while ((jsonData = br.readLine()) != null) {
                total_data += jsonData;
            }

            return total_data;

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