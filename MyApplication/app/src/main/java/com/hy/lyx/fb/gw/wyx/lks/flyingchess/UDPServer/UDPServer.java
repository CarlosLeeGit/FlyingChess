package com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer;


import android.support.v7.app.AppCompatActivity;

import com.hy.lyx.fb.gw.wyx.lks.flyingchess.ActivityManager;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Server.LocalServer;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.dataPack.DataPack;

/**
 * Created by BingF on 2016/5/15.
 */
public class UDPServer {
    private BroadcastSender sender = null;
    private BroadcastReceiver receiver = null;
    private LocalServer parent = null;

    public UDPServer(LocalServer parent, AppCompatActivity activity){
        sender = new BroadcastSender(this,activity);
        receiver = new BroadcastReceiver(this);
        this.parent = parent;
    }

    public void setRoomInfo(Room room){
        this.sender.roomChanged(room);
    }

    void dataPackReceived(DataPack dataPack){
        parent.onDataPackReceived(dataPack);
    }

    public void startListen(){
        Thread threadReceive = new Thread(receiver);
        threadReceive.start();
    }

    public void startBroadCast(){
        Thread threadSender = new Thread(sender);
        threadSender.start();
    }

    public void stop(){
        sender.close();
        receiver.close();
    }

    public LocalServer getParent(){
        return parent;
    }
}
