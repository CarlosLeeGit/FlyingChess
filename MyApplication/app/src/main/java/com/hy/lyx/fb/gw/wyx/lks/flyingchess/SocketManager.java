package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.KeyStore;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by karthur on 2016/4/26.
 */
public class SocketManager extends MsgHandler{
    private SSLSocket sock = null;
    private AppCompatActivity activity = null;
    private boolean connected;

    private SocketWriter sw;
    private SocketReader sr;

    public SocketManager(AppCompatActivity activity){
        //super();
        this.activity=activity;
        connected=false;
    }

    public void connectToServer(){
        // create the socket
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SSLContext sslContext = SSLContext.getInstance("SSLv3");
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");

                    KeyStore trustKeyStore = KeyStore.getInstance("BKS");
                    trustKeyStore.load(activity.getBaseContext().getResources().openRawResource(R.raw.flyingchess), "hustcs1307".toCharArray());
                    trustManagerFactory.init(trustKeyStore);
                    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                    sock = (SSLSocket) sslContext.getSocketFactory().createSocket("182.254.136.46", 6666);
                    sw=new SocketWriter(sock.getOutputStream());
                    sr=new SocketReader(sock.getInputStream());
                    new Thread(sw).start();
                    new Thread(sr).start();
                    connected=true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    connected = false;
                }
                DataPack dataPack=new DataPack(DataPack.CONNECTED,null);
                dataPack.setSuccessful(connected);
                processDataPack(dataPack);
            }
        }).start();
    }

    public boolean send(DataPack dataPack){
        return sw.send(dataPack);
    }

}
