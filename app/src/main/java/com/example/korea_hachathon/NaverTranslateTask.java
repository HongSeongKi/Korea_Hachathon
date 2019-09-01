package com.example.korea_hachathon;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverTranslateTask extends AsyncTask<String, Void, String> {

    public String resultText;

    String clientId = "AoWZ4WdQuGGqUvul6ZYU";
    String clientSecret = "sGqd7pnXkp";

    String sourceLang = "en";
    String targetLang = "ko";


    String mode;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //AsyncTask 메인처리
    @Override
    protected String doInBackground(String... strings) {

        System.out.println("start : ");
        String sourceText = strings[0];

        String sourceLang = strings[1];
        String targetLang = strings[2];
        mode = strings[3];
        System.out.println("MainString : "+MainActivity.country);
        try {

            String text = URLEncoder.encode(sourceText, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/language/translate";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println("doinbacground리턴값" + response);
            return response.toString().split("\"translatedText\":\"")[1].split("\"")[0];

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("result : ", s);

//            if(mode=="1")
//            {
//                WriteActivity.result_2.setText(s.toString().split("\"translatedText\":\"")[1].split("\"")[0]);
//            }else if(mode=="2")
//            {
//                SpeakActivity.txtResilt_2.setText(s.toString().split("\"translatedText\":\"")[1].split("\"")[0]);
//            }

        }


    public void execute(String sText) {
    }


    //자바용 그릇
    private class TranslatedItem {
        String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }
    }
}