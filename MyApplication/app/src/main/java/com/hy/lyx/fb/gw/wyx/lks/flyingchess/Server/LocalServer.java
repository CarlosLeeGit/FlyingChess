package com.hy.lyx.fb.gw.wyx.lks.flyingchess.Server;

import android.support.v7.app.AppCompatActivity;

import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.TCPServer;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer.UDPServer;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.dataPack.DataPack;

import java.util.UUID;


/**
 * Created by BingF on 2016/5/15.
 */
public class LocalServer {
    private UDPServer udpServer = null;
    private TCPServer tcpServer = null;

    public LocalServer(AppCompatActivity activity){
        udpServer = new UDPServer(this,activity);
        tcpServer = new TCPServer(this);
    }

    /**
     * Message notification from tcp server.
     * @param room
     */
    public void setRoomInfoForBroadCast(Room room){
        udpServer.setRoomInfo(room);
    }

    public void onDataPackReceived(DataPack dataPack){

    }


    public String startListen(){
        udpServer.startListen();
        return UUID.randomUUID().toString();
    }

    public void startHost(){
        udpServer.startBroadCast();
        new Thread(new Runnable() {
            @Override
            public void run() {
                tcpServer.start();
            }
        }).start();
    }

    public void stop(){
        udpServer.stop();
        tcpServer.shutdown();
    }



}
