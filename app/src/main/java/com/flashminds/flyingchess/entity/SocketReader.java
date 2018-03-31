package com.flashminds.flyingchess.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.flashminds.flyingchess.dataPack.DataPack;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ryan on 16/4/22.
 */
public class SocketReader implements Runnable {

    private Gson dataPackGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    private DataInputStream is = null;
    private boolean connected;

    public SocketReader(InputStream is) {
        this.is = new DataInputStream(is);
    }

    @Override
    public void run() {
        connected = true;
        while (true) {
            try {
                Game.socketManager.processDataPack(receive());
            } catch (Exception e) {
                e.printStackTrace();
                connected = false;
                Game.offlineTip();
                break;
            }
        }
    }

    private DataPack receive() throws IOException {
        // get the block size integer
        int blockSize;
        blockSize = this.is.readInt();
        byte[] bytes = new byte[blockSize];
        this.is.readFully(bytes);

        String json = new String(bytes, "UTF-8");

        // parse the datapack and return
        return dataPackGson.fromJson(json, DataPack.class);
    }
}

