package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import java.util.Date;
import java.util.List;

/**
 * Created by Ryan on 16/4/8.
 */
public class DataPack {
    // Common command
    public final static int INVALID = 0;
    public final static int LOGIN = 1000;
    public final static int LOGOUT = 1002;
    public final static int REGISTER = 1003;
    public final static int ROOM_ENTER = 2004;
    public final static int ROOM_CREATE = 2005;
    public final static int GAME_START = 2006;
    public final static int GAME_PROCEED = 2007;
    public final static int GAME_FINISHED = 2008;
    public final static int ROOM_EXIT = 2009;
    public final static int ROOM_LOOKUP = 3000;
    public final static int ROOM_USER_ENTERED = 3001;
    public final static int ROOM_USER_LEFT = 3002;
    public final static int TERMINATE = 5000;

    private int command = 0;
    private Date date = null;
    private boolean isSuccessful = false;
    private List<String> msgList = null;

    public DataPack(int command, Date date, boolean isSuccessful, List<String> msgList){
        this.command = command;
        this.date = date;
        this.isSuccessful = isSuccessful;
        this.msgList = msgList;
    }

    public boolean isValid(){
        return command != INVALID;
    }

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
