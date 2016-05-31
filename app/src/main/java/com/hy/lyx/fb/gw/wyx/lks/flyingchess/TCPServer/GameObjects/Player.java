package com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects;


import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.DataPackTcpSocket;

/**
 * Created by Ryan on 16/4/27.
 */
public class Player {
    private boolean isHost = false;
    private int id = 0;
    private DataPackTcpSocket socket = null;
    private int status = 0;
    private String userName = null;
    private Room room = null;
    private int points;
    public static final int ROOM_SELECTING = 0;
    public static final int ROOM_WAITING = 1;
    public static final int PLAYING = 2;

    private static int nextPlayerID = 1;

    private Player(int id, String userName){
        this.id = id;
        this.userName = userName;
        this.points = 0;
    }

    public static synchronized Player createPlayer(String userName){
        Player player = new Player(nextPlayerID, userName);
        nextPlayerID++;
        return player;
    }

    public static Player createRobot(int id){
        Player player = new Player(id,"Robot");
        return player;
    }

    public String getName(){
        return this.userName;
    }

    public boolean isRobot() { return this.id <= -1 && this.id >= -4; }

    public boolean isHost() { return this.isHost; }

    public void setHost(boolean isHost) { this.isHost = isHost; }

    public void setSocket(DataPackTcpSocket socket) { this.socket = socket; }

    public DataPackTcpSocket getSocket() { return this.socket; }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;

        if(obj instanceof Player){
            Player player = (Player) obj;
            return this.id == player.id;
        }

        return false;
    }

    protected void setRoom(Room room){
        this.room = room;
    }

    public Room getRoom(){
        return this.room;
    }

    public int getId(){
        return this.id;
    }

    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getStatus() { return this.status; }

    public void setStatus(int status) { this.status = status; }

    @Override
    public int hashCode(){
        return this.id;
    }

    @Override
    public String toString(){
        return userName + '(' + String.valueOf(id) + ')';
    }
}
