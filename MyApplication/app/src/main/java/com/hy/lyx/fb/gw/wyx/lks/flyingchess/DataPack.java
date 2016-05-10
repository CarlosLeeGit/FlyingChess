package com.hy.lyx.fb.gw.wyx.lks.flyingchess;


import java.util.Date;
import java.util.List;

/**
 * Created by Ryan on 16/4/8.
 */
public class DataPack {
    /**
     * Server does nothing upon receiving INVALID datapack.
     */
    public final static int INVALID = 0;

    /**
     * Commands in login process.
     */
    public final static int R_LOGIN = 1000;
    public final static int A_LOGIN = 1010;
    public final static int R_LOGOUT = 1002;
    public final static int A_LOGOUT = 1012;
    public final static int R_REGISTER = 1003;
    public final static int A_REGISTER = 1013;

    /**
     * Commands in room selecting process.
     */
    public final static int R_ROOM_ENTER = 2000;
    public final static int A_ROOM_ENTER = 2010;
    public final static int E_ROOM_ENTER = 2100;
    public final static int R_ROOM_CREATE = 2001;
    public final static int A_ROOM_CREATE = 2011;
    public final static int R_ROOM_LOOKUP = 2002;
    public final static int A_ROOM_LOOKUP = 2012;

    /**
     * Commands in room process.
     */
    public final static int R_ROOM_EXIT = 3000;
    public final static int A_ROOM_EXIT = 3010;
    public final static int E_ROOM_EXIT = 3100;
    public final static int R_ROOM_POSITION_SELECT = 3001;
    public final static int E_ROOM_POSITION_SELECT = 3101;
    public final static int R_GAME_START = 3002;
    public final static int E_GAME_START = 3102;

    /**
     * Commands in gaming process.
     */
    public final static int R_GAME_PROCEED_DICE = 4000;
    public final static int E_GAME_PROCEED_DICE = 4100;
    public final static int R_GAME_PROCEED_PLANE = 4001;
    public final static int E_GAME_PROCEED_PLANE = 4101;
    public final static int E_GAME_PLAYER_DISCONNECTED = 4102;
    public final static int E_GAME_PLAYER_CONNECTED = 4103;
    public final static int R_GAME_EXIT = 4008;
    public final static int A_GAME_EXIT = 4018;
    public final static int R_GAME_FINISHED = 4009;
    public final static int E_GAME_FINISHED = 4109;

    public final static int TERMINATE = 5000;
    public final static int CONNECTED=6000;




    private int command = 0;
    private Date date = null;
    private boolean isSuccessful = false;
    private List<String> msgList = null;

    public DataPack(int command, boolean isSuccessful, List<String> msgList, Date date){
        this.command = command;
        this.date = date;
        this.isSuccessful = isSuccessful;
        this.msgList = msgList;
    }

    public DataPack(int command, boolean isSuccessful, List<String> msgList){
        this.command = command;
        this.date = new Date();
        this.isSuccessful = isSuccessful;
        this.msgList = msgList;
    }

    public DataPack(int command, List<String> msgList){
        this.command = command;
        this.date = new Date();
        this.msgList = msgList;
        this.isSuccessful = false;
    }

    public DataPack(int command, boolean isSuccessful){
        this.command = command;
        this.date = new Date();
        this.msgList = null;
        this.isSuccessful = isSuccessful;
    }

    public DataPack(int command){
        this.command = command;
        this.date = new Date();
        this.msgList = null;
        this.isSuccessful = false;
    }

    public boolean isValid(){
        return command != INVALID;
    }

    public boolean isSuccessful() { return this.isSuccessful; }

    public void setSuccessful(boolean isSuccessful){ this.isSuccessful = isSuccessful; }

    public List<String> getMessageList(){
        return msgList;
    }

    public void setMessageList(List<String> msgList){
        this.msgList = msgList;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public int getCommand(){
        return command;
    }

    public void setCommand(int command){
        this.command = command;
    }

    public String getMessage(int index){ return this.msgList.get(index); }
}
