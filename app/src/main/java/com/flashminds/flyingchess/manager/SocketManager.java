package com.flashminds.flyingchess.manager;

import android.support.v7.app.AppCompatActivity;

import com.flashminds.flyingchess.dataPack.DataPack;
import com.flashminds.flyingchess.entity.Game;
import com.flashminds.flyingchess.entity.MsgHandler;
import com.flashminds.flyingchess.R;
import com.flashminds.flyingchess.entity.SocketReader;
import com.flashminds.flyingchess.entity.SocketWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.util.LinkedList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by karthur on 2016/4/26.
 */
public class SocketManager extends MsgHandler {
    private Socket sock = null;
    private AppCompatActivity activity = null;
    private boolean connected;

    private SocketWriter sw;
    private SocketReader sr;

    public SocketManager(AppCompatActivity activity) {
        //super();
        this.activity = activity;
        connected = false;
    }

    public void connectToLocalServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    sock = new Socket(InetAddress.getLocalHost(), 6666);
                    sock.setSoTimeout(2000);
                    sock.setTcpNoDelay(true);
                    sw = new SocketWriter(sock.getOutputStream());
                    sr = new SocketReader(sock.getInputStream());
                    new Thread(sw).start();
                    new Thread(sr).start();
                    connected = true;
                    sock.setSoTimeout(0);//cancle time out
                } catch (Exception e) {
                    e.printStackTrace();
                    connected = false;
                }
            }
        }).start();
    }

    public void connectLanServer(final String ip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sock = new Socket(InetAddress.getByName(ip), 6666);
                    sock.setSoTimeout(2000);
                    sock.setTcpNoDelay(true);
                    sw = new SocketWriter(sock.getOutputStream());
                    sr = new SocketReader(sock.getInputStream());
                    new Thread(sw).start();
                    new Thread(sr).start();
                    connected = true;
                    sock.setSoTimeout(0);//cancle time out
                } catch (Exception e) {
                    e.printStackTrace();
                    connected = false;
                }
            }
        }).start();
    }

    public void connectToRemoteServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SSLContext sslContext = SSLContext.getInstance("SSLv3");
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");

                    KeyStore trustKeyStore = KeyStore.getInstance("BKS");
                    trustKeyStore.load(activity.getBaseContext().getResources().openRawResource(R.raw.flyingchess), "hustcs1307".toCharArray());
                    trustManagerFactory.init(trustKeyStore);
                    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                    sock = (SSLSocket) sslContext.getSocketFactory().createSocket(Game.dataManager.data.ip, 6666);
                    sock.setSoTimeout(2000);
                    sock.setTcpNoDelay(true);
                    sw = new SocketWriter(sock.getOutputStream());
                    sr = new SocketReader(sock.getInputStream());
                    new Thread(sw).start();
                    new Thread(sr).start();
                    connected = true;
                    sock.setSoTimeout(0);//cancle time out
                } catch (Exception e) {
                    e.printStackTrace();
                    connected = false;
                }
                DataPack dataPack = new DataPack(DataPack.CONNECTED, null);
                dataPack.setSuccessful(connected);
                processDataPack(dataPack);
            }
        }).start();
    }

    public boolean send(DataPack dataPack) {
        return sw.send(dataPack);
    }

    public boolean send(int command, Object... argv) {
        LinkedList<String> msgs = new LinkedList<>();
        for (int i = 0; i < argv.length; i++) {
            msgs.addLast(String.valueOf(argv[i]));
        }
        DataPack dataPack = new DataPack(command, msgs);
        return sw.send(dataPack);
    }

}
