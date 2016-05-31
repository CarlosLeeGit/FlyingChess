package com.flashminds.flyingchess.LocalServer.TCPServer;


import android.os.Build;

import com.flashminds.flyingchess.LocalServer.LocalServer;
import com.flashminds.flyingchess.LocalServer.TCPServer.GameObjects.Room;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TCPServer {
    private ServerSocket serverSocket = null;
    private ExecutorService socketExecutor = null;
    private LocalServer parent = null;
    private Room selfRoom = null;
    private boolean isRunning = true;


    public TCPServer(LocalServer parent) {
        this.socketExecutor = Executors.newCachedThreadPool();
        this.parent = parent;
    }

    public void start() {
        try {
            if (serverSocket == null || !serverSocket.isBound() || serverSocket.isClosed())
                this.serverSocket = new ServerSocket(6666);

            this.selfRoom = new Room(new Build().MODEL, this);
            this.onRoomChanged(selfRoom);

            serverSocket.setSoTimeout(1000);

            while (isRunning) {
                try {
                    Socket sock = serverSocket.accept();
                    Runnable socketRunnable = new DataPackSocketRunnable(new DataPackTcpSocket(sock), this);
                    this.socketExecutor.submit(socketRunnable);
                } catch (SocketTimeoutException e) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Room getSelfRoom() {
        return this.selfRoom;
    }

    public void onRoomChanged(Room room) {
        this.selfRoom = room;
        parent.setRoomInfoForBroadCast(room);
    }

    public Room stop() {
//        try {
//            // send shutdown datapack to ever online users
//            // and stop the socket.
//            for (Player player : this.selfRoom.getAllPlayers()) {
//                player.getSocket().send(new DataPack(DataPack.TERMINATE));
//                player.getSocket().close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Room room = this.selfRoom;
        this.isRunning = false;
        this.selfRoom = null;
        return room;
    }
}
