package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/9.
 */
public class DataManager {//数据存储类
    public static final byte GM_LOCAL=0,GM_WIFI=1,GM_BT=2,GM_WLAN=3;//游戏模式
    public static final byte COLOR_RED=0,COLOR_GREEN=1,COLOR_BLUE=2,COLOR_WHITE=3;//玩家颜色

    private String userName;//用户名
    private String password;//加密后的密码字符串
    private byte musicVolume,effectVolume;//0~255 音量
    private byte gameMode;//游戏模式
    private int localScore;//单机分数
    private int onlineScore;//网络分数    WIFI和BT时因为是临时对决  暂定不用积分
    private byte myColor;//游戏时的颜色

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

    public int getScore(){//返回当前模式的分数  -1表示获取出错
        switch (gameMode)
        {
            case GM_LOCAL:
                return localScore;
            case GM_WLAN:
                return onlineScore;
            default:
                return -1;
        }
    }

    public void addScore(int score){//加分 输了加负分
        switch (gameMode)
        {
            case GM_LOCAL:
                localScore+=score;
                break;
            case GM_WLAN:
                onlineScore+=score;
                break;
        }
    }

    public byte getMyColor(){//得到颜色
        return  myColor;
    }

    public void setMyColor(){//设置颜色
        this.myColor=myColor;
    }

    public byte getMusicVolume(){//得到音乐音量
        return musicVolume;
    }

    public byte getEffectVolume(){//得到音效音量
        return effectVolume;
    }

    public void setMusiAByte(byte musicVolume){//设置音乐音量
        this.musicVolume = musicVolume;
    }

    public void setEffectVolume(byte effectVolume){//设置音效音量
        this.effectVolume=effectVolume;
    }
}
