package com.example.korea_hachathon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WriteActivity extends AppCompatActivity {

    private Button pencil;
    private Button erase;
    private Button send;
    private Button translate;
    private DrawView drawView;
    private TextView[] text = new TextView[5];
    ArrayList<String> array = new ArrayList<>();
    Socket socket;
    BufferedWriter networkWriter;
    BufferedReader networkReader;
    String []hangeul_list = {"가" ,"각" ,"간" ,"갈" ,"감" ,"갑" ,"강" ,"갖" ,"같" ,"개" ,"객" ,"거" ,"걱" ,"건" ,"걷" ,"걸" ,"검" ,"겁" ,"것" ,"게" ,"겨" ,"격" ,"견" ,"결" ,"경" ,"계" ,"고" ,"곡" ,"곤" ,"곧" ,"골" ,"곳" ,"공" ,"과" ,"관" ,"광" ,"괴" ,"교" ,"구" ,"국" ,"군" ,"굳" ,"궁" ,"권" ,"귀" ,"규" ,"그" ,"극" ,"근" ,"글" ,"금" ,"급" ,"기" ,"긴" ,"길" ,"김" ,"깊" ,"까" ,"깔" ,"깨" ,"꺼" ,"껏" ,"께" ,"꼭" ,"꽃" ,"꾸" ,"끄" ,"끊" ,"끌" ,"끗" ,"끝" ,"끼" ,"나" ,"난" ,"날" ,"남" ,"낮" ,"내" ,"냉" ,"너" ,"널" ,"넓" ,"넘" ,"네" ,"넷" ,"녀" ,"녁" ,"년" ,"념" ,"녕" ,"노" ,"녹" ,"논" ,"놀" ,"농" ,"높" ,"놓" ,"누" ,"눈" ,"뉴" ,"느" ,"늘" ,"능" ,"늦" ,"니" ,"님" ,"다" ,"닥" ,"단" ,"달" ,"닭" ,"담" ,"답" ,"닷" ,"당" ,"대" ,"더" ,"덕" ,"데" ,"도" ,"독" ,"돌" ,"동" ,"되" ,"두" ,"드" ,"득" ,"들" ,"듯" ,"등" ,"디" ,"따" ,"딸" ,"때" ,"뜨" ,"라" ,"락" ,"란" ,"람" ,"랑" ,"랗" ,"래" ,"램" ,"량" ,"러" ,"런" ,"럽" ,"렇" ,"레" ,"려" ,"력" ,"련" ,"령" ,"례" ,"로" ,"록" ,"론" ,"롭" ,"료" ,"루" ,"류" ,"르" ,"른" ,"름" ,"릇" ,"리" ,"린" ,"림" ,"립" ,"마" ,"막" ,"만" ,"많" ,"말" ,"맛" ,"망" ,"맞" ,"맡" ,"매" ,"머" ,"먹" ,"멀" ,"멋" ,"멍" ,"메" ,"면" ,"명" ,"몇" ,"모" ,"목" ,"몰" ,"몸" ,"못" ,"무" ,"문" ,"묻" ,"물" ,"미" ,"민" ,"밀" ,"바" ,"박" ,"반" ,"받" ,"발" ,"밝" ,"밤" ,"밥" ,"방" ,"배" ,"백" ,"버" ,"번" ,"벌" ,"범" ,"법" ,"베" ,"벽" ,"변" ,"별" ,"병" ,"보" ,"복" ,"본" ,"볼" ,"봉" ,"부" ,"북" ,"분" ,"불" ,"붙" ,"비" ,"빌" ,"빗" ,"빛" ,"빠" ,"빨" ,"빼" ,"쁘" ,"사" ,"산" ,"살" ,"삼" ,"상" ,"새" ,"색" ,"생" ,"서" ,"석" ,"선" ,"설" ,"섭" ,"섯" ,"성" ,"세" ,"센" ,"소" ,"속" ,"손" ,"송" ,"쇠" ,"수" ,"숙" ,"순" ,"술" ,"숨" ,"쉬" ,"쉽" ,"스" ,"슬" ,"습" ,"승" ,"시" ,"식" ,"신" ,"실" ,"싫" ,"심" ,"십" ,"싱" ,"싸" ,"쌍" ,"쓰" ,"쓸" ,"씨" ,"씩" ,"아" ,"악" ,"안" ,"앉" ,"알" ,"암" ,"앞" ,"애" ,"액" ,"야" ,"약" ,"양" ,"얘" ,"어" ,"억" ,"언" ,"얼" ,"엄" ,"업" ,"없" ,"엉" ,"에" ,"여" ,"역" ,"연" ,"열" ,"염" ,"영" ,"옆" ,"예" ,"오" ,"옥" ,"온" ,"올" ,"옷" ,"와" ,"완" ,"왕" ,"외" ,"왼" ,"요" ,"욕" ,"용" ,"우" ,"운" ,"울" ,"움" ,"웃" ,"워" ,"원" ,"월" ,"웨" ,"위" ,"유" ,"육" ,"율" ,"으" ,"은" ,"을" ,"음" ,"응" ,"의" ,"이" ,"익" ,"인" ,"일" ,"임" ,"입" ,"있" ,"잊" ,"잎" ,"자" ,"작" ,"잔" ,"잘" ,"잠" ,"잡" ,"장" ,"재" ,"쟁" ,"저" ,"적" ,"전" ,"절" ,"점" ,"접" ,"정" ,"제" ,"져" ,"조" ,"족" ,"존" ,"졸" ,"종" ,"좋" ,"좌" ,"죄" ,"주" ,"죽" ,"준" ,"줄" ,"중" ,"즈" ,"즐" ,"증" ,"지" ,"직" ,"진" ,"질" ,"짐" ,"집" ,"짓" ,"징" ,"짜" ,"짝" ,"째" ,"쩌" ,"쪽" ,"찌" ,"찍" ,"차" ,"착" ,"찬" ,"찰" ,"참" ,"창" ,"찾" ,"채" ,"책" ,"처" ,"척" ,"천" ,"철" ,"첫" ,"청" ,"체" ,"초" ,"촌" ,"총" ,"최" ,"추" ,"축" ,"출" ,"충" ,"취" ,"층" ,"치" ,"친" ,"칠" ,"침" ,"카" ,"커" ,"컵" ,"코" ,"크" ,"큰" ,"키" ,"킬" ,"타" ,"탁" ,"탕" ,"태" ,"택" ,"터" ,"테" ,"토" ,"통" ,"퇴" ,"투" ,"튀" ,"트" ,"특" ,"튼" ,"틀" ,"티" ,"파" ,"판" ,"팔" ,"패" ,"팩" ,"팬" ,"퍼" ,"페" ,"편" ,"평" ,"포" ,"폭" ,"표" ,"풀" ,"품" ,"풍" ,"프" ,"피" ,"필" ,"하" ,"학" ,"한" ,"할" ,"함" ,"합" ,"항" ,"해" ,"행" ,"향" ,"허" ,"험" ,"혀" ,"현" ,"형" ,"호" ,"혼" ,"홍" ,"화" ,"확" ,"환" ,"활" ,"회" ,"효" ,"후" ,"휴" ,"흐" ,"흔" ,"흘" ,"흥" ,"희" ,"히" ,"힘"};
    String ip="172.20.200.121";
    int port = 9100;
    LinearLayout linear;
    public static TextView result;
    public static TextView result_2;


//    textView12



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        pencil = (Button) findViewById(R.id.pencil);
        erase = (Button) findViewById(R.id.erase);
        send = (Button) findViewById(R.id.send);
        drawView = (DrawView) findViewById(R.id.drawView);
        linear = (LinearLayout)findViewById(R.id.linear);
        translate = (Button)findViewById(R.id.translate);
        result = (TextView)findViewById(R.id.textView11);
        result_2 = (TextView)findViewById(R.id.textView12);
        text[0] = (TextView)findViewById(R.id.text1);
        text[1] = (TextView)findViewById(R.id.text2);
        text[2] = (TextView)findViewById(R.id.text3);
        text[3] = (TextView)findViewById(R.id.text4);
        text[4] = (TextView)findViewById(R.id.text5);

        text[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    result.setText(result.getText().toString()+text[0].getText().toString());
            }
        });
        text[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(result.getText().toString()+text[1].getText().toString());
            }
        });
        text[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(result.getText().toString()+text[2].getText().toString());
            }
        });
        text[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(result.getText().toString()+text[3].getText().toString());
            }
        });
        text[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(result.getText().toString()+text[4].getText().toString());
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaverTranslateTask asyncTask = new NaverTranslateTask();
                String sText = result.getText().toString();
                if(!sText.equals("")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        try {
                            result_2.setText(asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sText, "ko", MainActivity.country, "1").get());
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
        });

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.flag = 1;
            }
        });
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.flag = 0;
                drawView.reset();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawView.getPoints().size()!=0) { //그림이 그려저 있다면
                    System.out.println("그림그려저있음");
                    //capturesave(drawView);
                    //captureView(drawView);

                    drawView.send(getApplicationContext());
                    drawView.getPoints().clear();
                    drawView.invalidate();
                    Thread connect = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PrintWriter out=null;
                            try {
                                if (MyGlobals.getInstance().getSocket() == null) {
                                    socket = new Socket(ip,port);
                                    networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                    networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                    MyGlobals.getInstance().setSocket(socket);
                                    MyGlobals.getInstance().setNetworkReader(networkReader);
                                    MyGlobals.getInstance().setNetworkWriter(networkWriter);
                                    out = new PrintWriter(networkWriter,true);
                                    out.println("System#System");
                                    out.flush();
                                }
                                else {
                                    socket = MyGlobals.getInstance().getSocket();
                                    networkWriter = MyGlobals.getInstance().getNetworkWriter();
                                    networkReader = MyGlobals.getInstance().getNetworkReader();
                                }
                                String line = networkReader.readLine();
                                System.out.println("line : "+line);
                                File file = new File(Environment.getExternalStorageDirectory() + "/ctos_image.png");
                                long file_length = file.length();
                                String str2 = "System@image_send_client_to_server_hangeul@ctos_image.png@" +  String.valueOf(file_length);
                                str2 = new String(str2.getBytes("UTF-8"),"UTF-8");
                                out.println(str2);

                                try{
                                    FileInputStream fis = new FileInputStream(file);
                                    DataInputStream dis = new DataInputStream(fis);
                                    DataOutputStream output = new DataOutputStream(MyGlobals.getInstance().getSocket().getOutputStream());

                                    byte[] buf = new byte[1024];
                                    int len;
                                    int total_len = 0;

                                    while((len = dis.read(buf)) != -1)
                                    {
                                        System.out.println("len : "+len);
                                        total_len += len;
                                        output.write(buf,0,len);
                                        output.flush();
                                        if(total_len>=file_length) {
                                            break;
                                        }
                                    }
                                    System.out.println("total_len : "+total_len);

                                    BufferedReader reader = MyGlobals.getInstance().getNetworkReader();
                                    String str = reader.readLine();
                                    System.out.println("str + "+str);
                                    String s = str.split("@")[2];
                                    array.clear();
                                    array.add(hangeul_list[Integer.parseInt(s.split("#")[0])]);
                                    array.add(hangeul_list[Integer.parseInt(s.split("#")[1])]);
                                    array.add(hangeul_list[Integer.parseInt(s.split("#")[2])]);
                                    array.add(hangeul_list[Integer.parseInt(s.split("#")[3])]);
                                    array.add(hangeul_list[Integer.parseInt(s.split("#")[4])]);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for(int i =0;i<5;i++){
                                                text[i].setText(array.get(i));
                                            }
                                        }
                                    });
                                    MyGlobals.getInstance().setSocket(null);
                                    output.close();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }catch(IOException e){
                                System.out.println("getSocket : "+MyGlobals.getInstance().getSocket());
                                e.printStackTrace();
                            }
                        }
                    });
                    connect.start();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onStop 호출됨");
        MyGlobals.getInstance().setSocket(null);
            try{
                if(socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


}

