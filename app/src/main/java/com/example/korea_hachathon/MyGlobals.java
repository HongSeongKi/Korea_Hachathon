package com.example.korea_hachathon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class MyGlobals {
    private int page;
    private static MyGlobals instance = null;
    private Socket socket=null;
    private BufferedReader networkReader=null;
    private BufferedWriter networkWriter=null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
