package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketWriter implements Runnable{

    private Gson dataPackGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    private LinkedBlockingQueue<DataPack> dataPackQueue = null;
    private DataOutputStream os = null;
    private boolean connected;

    public SocketWriter(OutputStream os){
        dataPackQueue=new LinkedBlockingQueue<>();
        this.os = new DataOutputStream(os);
    }

    @Override
    public void run() {
        connected = true;
        while (true){
            try{
                sendQueuedDataPack();
                Thread.sleep(100);
            }
            catch (Exception e){
                e.printStackTrace();
                connected=false;
                break;
            }
        }
    }


    private void sendQueuedDataPack() throws IOException {
        List<DataPack> dataPackList = new ArrayList<>();
        this.dataPackQueue.drainTo(dataPackList);

        for(DataPack dataPack : dataPackList){
            byte[] sendBytes = dataPackGson.toJson(dataPack, DataPack.class).getBytes(Charset.forName("UTF-8"));
            int bytesSize = sendBytes.length;
            this.os.writeInt(bytesSize);
            this.os.write(sendBytes);
            this.os.flush();
        }
    }

    /**
     * Queue the datapack into the messaging queue, which will be sent out afterwards.
     * @param dataPack The datapack to be queued.
     */
    public boolean send(DataPack dataPack) {
        if(connected){
            try{
                this.dataPackQueue.put(dataPack);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return connected;
    }
}
