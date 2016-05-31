package com.hy.lyx.fb.gw.wyx.lks.flyingchess.Server;

import android.support.v7.app.AppCompatActivity;

import com.hy.lyx.fb.gw.wyx.lks.flyingchess.DataPack;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.TCPServer;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Target;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer.UDPServer;

import java.util.UUID;


/**
 * Created by BingF on 2016/5/15.
 */
public class LocalServer {
    private UDPServer udpServer = null;
    private TCPServer tcpServer = null;

    private Target target;

    public LocalServer(AppCompatActivity activity){
        udpServer = new UDPServer(this,activity);
        tcpServer = new TCPServer(this);
    }

    /**
     * Message notification from tcp server.
     * @param room
     */
    public void setRoomInfoForBroadCast(Room room){
        udpServer.onRoomChanged(room);
    }

    public void onDataPackReceived(DataPack dataPack){
        target.processDataPack(dataPack);
    }

    public String getRoomIp(String roomId){
        return udpServer.getRoomMap().get(UUID.fromString(roomId)).getMessage(4);
    }

    public int getPort(String roomId){
        return Integer.valueOf(udpServer.getRoomMap().get(UUID.fromString(roomId)).getMessage(5));
    }

    public void updateRoomListImmediately(){
        onDataPackReceived(udpServer.createRoomInfoListDataPack());
    }

    public String startListen(){
        udpServer.startListen();
        return UUID.randomUUID().toString();
    }

    public void startHost(){
        udpServer.startBroadcast();
        new Thread(new Runnable() {
            @Override
            public void run() {
                tcpServer.start();
            }
        }).start();
    }

    public void stopHost(){
        Room closedRoom = tcpServer.stop();
        udpServer.stopBroadcast(closedRoom);
    }

    public void stop(){
        udpServer.stopListen();
    }

    public void registerMsg(Target target){
        this.target=target;
    }

}
