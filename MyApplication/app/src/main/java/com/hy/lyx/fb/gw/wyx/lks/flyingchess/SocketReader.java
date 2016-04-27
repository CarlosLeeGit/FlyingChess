package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Ryan on 16/4/22.
 */
public class SocketReader implements Runnable{

    private Gson dataPackGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    private DataInputStream is = null;
    private boolean connected;

    public SocketReader(InputStream is) {
        this.is=new DataInputStream(is);
    }

    @Override
    public void run(){
        connected=true;
        while(true){
                try{
                    Game.getSocketManager().processDataPack(receive());
                } catch(Exception e){
                    e.printStackTrace();
                    connected=false;
                    break;
                }
        }
    }

    private DataPack receive() throws IOException{
        // get the block size integer
        int blockSize;
        blockSize  = this.is.readInt();
        byte[] bytes = new byte[blockSize];
        this.is.readFully(bytes);

        String json = new String(bytes, "UTF-8");

        // parse the datapack and return
        return dataPackGson.fromJson(json, DataPack.class);
    }
}

