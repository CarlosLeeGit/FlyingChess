package DataPack;

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
    public final static int LOGIN = 1000;
    public final static int LOGOUT = 1002;
    public final static int REGISTER = 1003;

    /**
     * Commands in room selecting process.
     */
    public final static int ROOM_ENTER = 2000;
    public final static int ROOM_CREATE = 2001;
    public final static int ROOM_LOOKUP = 2002;

    /**
     * Commands in room process.
     */
    public final static int ROOM_EXIT = 3000;
    public final static int ROOM_SELECT_POSITION = 3001;
    public final static int GAME_START = 3002;
    // notification command which is sent only from server.
    public final static int ROOM_USER_ENTERED = 3010;
    public final static int ROOM_USER_LEFT = 3011;
    public final static int ROOM_USER_PICK_POSITION = 3012;

    /**
     * Commands in gaming process.
     */
    public final static int GAME_PROCEED = 4000;
    public final static int GAME_FINISHED = 4001;

    public final static int TERMINATE = 5000;

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
