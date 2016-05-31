package com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer;


import android.app.Activity;
import android.app.FragmentBreadCrumbs;
import android.support.v7.app.AppCompatActivity;

import com.hy.lyx.fb.gw.wyx.lks.flyingchess.DataPack;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Server.LocalServer;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by BingF on 2016/5/15.
 */
public class UDPServer {
    private BroadcastSender sender = null;
    private BroadcastReceiver receiver = null;
    private LocalServer parent = null;
    private HashMap<UUID, DataPack> roomMap = null;
    private ExecutorService executor = null;
    private AppCompatActivity activity = null;

    public UDPServer(LocalServer parent, AppCompatActivity activity){

        this.parent = parent;
        this.roomMap = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(2);
        this.activity = activity;
    }

    public DataPack createRoomInfoListDataPack(){
        List<String> msgList = new LinkedList<>();
        for(DataPack dataPack : roomMap.values())
            msgList.addAll(dataPack.getMessageList().subList(0, 4));

        return new DataPack(DataPack.A_ROOM_LOOKUP, msgList);
    }

    public void onRoomChanged(Room room){
        this.sender.onRoomChanged(room);
    }

    void dataPackReceived(DataPack dataPack){
        UUID id = UUID.fromString(dataPack.getMessage(0));
        switch(dataPack.getCommand()){
            case DataPack.E_ROOM_REMOVE_BROADCAST:{
                roomMap.remove(id);
                parent.onDataPackReceived(createRoomInfoListDataPack());
                break;
            }
            case DataPack.E_ROOM_CREATE_BROADCAST:{
                if(roomMap.get(id) == null ||
                        Integer.valueOf(roomMap.get(id).getMessage(2)) != Integer.valueOf(dataPack.getMessage(2)) ||
                        Integer.valueOf(roomMap.get(id).getMessage(3)) != Integer.valueOf(dataPack.getMessage(3))) {
                    roomMap.put(id, dataPack);
                    parent.onDataPackReceived(createRoomInfoListDataPack());
                }
                break;
            }
            default:
                break;
        }
    }

    public Map<UUID, DataPack> getRoomMap(){
        return roomMap;
    }

    public void startBroadcast(){
        if(this.sender != null)
            this.sender.stop(null);

        this.sender = new BroadcastSender(this, activity);
        this.executor.submit(this.sender);
    }

    public void startListen(){
        this.receiver = new BroadcastReceiver(this);
        this.executor.submit(this.receiver);
    }

    public void stopBroadcast(Room room){
        this.sender.stop(room);
    }

    public void stopListen(){
        this.roomMap.clear();
        this.receiver.stop();
    }

}
