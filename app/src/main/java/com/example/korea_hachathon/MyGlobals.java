package com.example.korea_hachathon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class MyGlobals {
    private static String nickName= null;
    private int page;
    private static MyGlobals instance = null;
    private Socket socket=null;
    private BufferedReader networkReader=null;
    private BufferedWriter networkWriter=null;
    private Thread current_connect =null;
    private static int flag = 1;
    private static int stop = 0;

    public static int getStop() {
        return stop;
    }

    public static void setStop(int stop) {
        MyGlobals.stop = stop;
    }

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        MyGlobals.flag = flag;
    }

    public Thread getCurrent_connect() {
        return current_connect;
    }

    public void setCurrent_connect(Thread current_connect) {
        this.current_connect = current_connect;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public static String getNickName() {
        return nickName;
    }

    public static void setNickName(String nickName) {
        MyGlobals.nickName = nickName;
    }


    public static MyGlobals getInstance() {
        if(instance == null)
        {
            instance = new MyGlobals();
        }
        return instance;
    }

    public static void setInstance(MyGlobals instance) {
        MyGlobals.instance = instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getNetworkReader() {
        return networkReader;
    }

    public void setNetworkReader(BufferedReader networkReader) {
        this.networkReader = networkReader;
    }

    public BufferedWriter getNetworkWriter() {
        return networkWriter;
    }

    public void setNetworkWriter(BufferedWriter networkWriter) {
        this.networkWriter = networkWriter;
    }
}
