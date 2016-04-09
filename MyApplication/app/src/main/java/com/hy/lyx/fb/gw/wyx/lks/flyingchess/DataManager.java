package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/9.
 */
public class DataManager {//数据存储类
    public static final int GM_LOCAL=0,GM_WIFI=1,GM_BT=2,GM_WLAN=3;//游戏模式

    private String userName;//用户名
    private String password;//加密后的密码字符串
    private byte musicVolume,effectVolume;//0~255
    private byte gameMode;//游戏模式
    private int localScore;//单机分数
    private int onlineScore;//网络分数    WIFI和BT时因为是临时对决  暂定不用积分

    public void init(){//初始化本地数据

    }

    public String getUserName(){//返回用户名
        return userName;
    }

    public String getPassword(){//返回用户密码
        return password;
    }

    public void setUserName(String userName){//设置用户名
        this.userName=userName;
    }

    public void setPassword(String password){//设置密码 并使用加密存储

    }

    public void setGameMode(byte gameMode){//设置当前的游戏模式
        this.gameMode=gameMode;
    }

    public int getScore(){//

    }
}
