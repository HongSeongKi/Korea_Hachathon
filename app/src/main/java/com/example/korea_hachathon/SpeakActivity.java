package com.example.korea_hachathon;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.korea_hachathon.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SpeakActivity extends Activity {

    private static final String TAG = SpeakActivity.class.getSimpleName();
    private static final String CLIENT_ID = "dngwnvzb6i";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    public static TextView txtResilt_2;
    private Button btnStart;
    private String mResult;
//    textView
    private AudioWriterPCM writer;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
                for(String result : results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                    break;
                }
                mResult = strBuf.toString();
                txtResult.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart = (Button) findViewById(R.id.btn_start);
        txtResilt_2 = (TextView) findViewById(R.id.textView);
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    //btnStart.setText(R.string.str_stop);
                    btnStart.setBackgroundResource(R.drawable.recording);
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    btnStart.setEnabled(false);
                    btnStart.setBackgroundResource(R.drawable.record);
                    naverRecognizer.getSpeechRecognizer().stop();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    NaverTranslateTask asyncTask = new NaverTranslateTask();
                    String sText = txtResult.getText().toString();
                    if(!sText.equals("")) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            try {
                                txtResilt_2.setText(asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sText, "ko",MainActivity.country, "2").get());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            asyncTask.execute(sText);
                        }
                    }


                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mResult = "";
        txtResult.setText("");
        //btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // NOTE : release() must be called on stop time.
        naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<SpeakActivity> mActivity;

        RecognitionHandler(SpeakActivity activity) {
            mActivity = new WeakReference<SpeakActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SpeakActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}