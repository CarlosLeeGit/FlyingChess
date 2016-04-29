package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/9.
 */
public class DataManager {//数据存储类
    public static final int GM_LOCAL=0,GM_LAN=1,GM_WLAN=3;//游戏模式
    private boolean autoLogin;//需要登录
    private String myName;//用户名
    private String password;//加密后的密码字符串
    private String roomId;//房间ID
    private int musicVolume,effectVolume;//0~255 音量
    private int gameMode;//游戏模式

    public DataManager(){//加载本地数据
        autoLogin=false;
    }

    //////////////////////////////////////////////////getter
    public String getMyName(){//返回用户名
        return myName;
    }

    public String getPassword(){//返回用户密码
        return password;
    }

    public int getMusicVolume(){//得到音乐音量
        return musicVolume;
    }

    public int getEffectVolume(){//得到音效音量
        return effectVolume;
    }

    public int getGameMode(){
        return gameMode;
    }

    public String getRoomId(){
        return roomId;
    }

    public boolean autoLogin(){
        return autoLogin;
    }


    /////////////////////////////////////////////////////setter

    public void setMyName(String myName){//设置用户名
        this.myName=myName;
    }

    public void setPassword(String password){//设置密码 并使用加密存储
        this.password=password;
    }

    public void setMusicVolume(int musicVolume){//设置音乐音量
        this.musicVolume = musicVolume;
    }

    public void setEffectVolume(int effectVolume){//设置音效音量
        this.effectVolume=effectVolume;
    }

    public void setGameMode(int gameMode){//设置当前的游戏模式
        this.gameMode=gameMode;
    }

    public void setRoomId(String roomId){
        this.roomId=roomId;
    }

    public void setAutoLogin(boolean autoLogin){
        this.autoLogin=autoLogin;
    }
}

