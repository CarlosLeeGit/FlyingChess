package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
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
public class SocketRunnable extends MsgHandler implements Runnable{
    private SSLSocket sock = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private int blockSize = 0;
    private Gson dataPackGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    private LinkedBlockingQueue<DataPack> dataPackQueue = null;
    private AppCompatActivity activity = null;

    public SocketRunnable(AppCompatActivity activity) {
        this.activity = activity;
        this.dataPackQueue = new LinkedBlockingQueue<>(100);
    }

    @Override
    public void run(){
        try{
            // create the socket
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");

            KeyStore trustKeyStore = KeyStore.getInstance("BKS");
            trustKeyStore.load(activity.getBaseContext().getResources().openRawResource(R.raw.flyingchess), "hustcs1307".toCharArray());
            trustManagerFactory.init(trustKeyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            this.sock = (SSLSocket) sslContext.getSocketFactory().createSocket("182.254.136.46", 6666);

            /**
             * Set timeout for reading the input stream so that
             * it won't be blocked forever. thus giving it chance
             * to send out queued data packs.
             */

            this.os = new DataOutputStream(sock.getOutputStream());
            this.is = new DataInputStream(sock.getInputStream());

            this.sock.setSoTimeout(200);

            // enter receive loop
            while(true){
                try{
                    sendQueuedDataPack();
                    processDataPack(receive());
                } catch(SocketTimeoutException e) {
                    // do nothing
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private void sendQueuedDataPack(){
        List<DataPack> dataPackList = new ArrayList<>();
        this.dataPackQueue.drainTo(dataPackList);
        // send out all available datapacks.
        try{
            for(DataPack dataPack : dataPackList){
                byte[] sendBytes = dataPackGson.toJson(dataPack, DataPack.class).getBytes(Charset.forName("UTF-8"));
                int bytesSize = sendBytes.length;

                this.os.writeInt(bytesSize);
                this.os.write(sendBytes);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private DataPack receive() throws IOException{
        // get the block size integer
        if(blockSize == 0){
            this.blockSize = this.is.readInt();
        }

        byte[] bytes = new byte[blockSize];
        this.is.readFully(bytes);
        this.blockSize = 0;

        String json = new String(bytes, "UTF-8");

        // parse the datapack and return
        return dataPackGson.fromJson(json, DataPack.class);
    }

    /**
     * Queue the datapack into the messaging queue, which will be sent out afterwards.
     * @param dataPack The datapack to be queued.
     */
    public void send(DataPack dataPack) {
        try{
            this.dataPackQueue.put(dataPack);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
