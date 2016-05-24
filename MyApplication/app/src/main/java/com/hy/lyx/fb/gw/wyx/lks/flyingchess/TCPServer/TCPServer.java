package com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer;


import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Server.LocalServer;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Player;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.dataPack.DataPack;


import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TCPServer {
    private ServerSocket server = null;
    private ExecutorService socketExecutor = null;
    private LocalServer parent = null;
    private Room myRoom = null;


    public TCPServer(LocalServer parent){
        this.socketExecutor = Executors.newCachedThreadPool();
        this.parent = parent;
    }

    public void start() {
        try {
            if(server == null || !server.isBound() || server.isClosed())
                this.server = new ServerSocket(6666);

            this.myRoom = new Room("Myroom",this);
            this.roomChanged(myRoom);


            while(true){
                Socket sock = server.accept();
                Runnable socketRunnable = new DataPackSocketRunnable(new DataPackTcpSocket(sock),this);
                this.socketExecutor.submit(socketRunnable);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    Room getSelfRoom(){
        return this.myRoom;
    }

    public void roomChanged(Room room){
        this.myRoom = room;
        parent.setRoomInfoForBroadCast(room);
    }


    public void shutdown(){
        try{
            // send shutdown datapack to ever online users
            // and close the socket.
            for(Player player : this.myRoom.getAllPlayers()){
                player.getSocket().send(new DataPack(DataPack.TERMINATE));
                player.getSocket().close();
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
