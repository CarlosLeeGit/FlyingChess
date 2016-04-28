package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/9.
 */
public class DataManager {//数据存储类
    public static final int GM_LOCAL=0,GM_LAN=1,GM_WLAN=3;//游戏模式
    private boolean needLogin;//需要登录
    private String userName;//用户名
    private String password;//加密后的密码字符串
    private String id;//玩家ID
    private String roomId;//房间ID
    private int musicVolume,effectVolume;//0~255 音量
    private int gameMode;//游戏模式
    private int localScore;//单机分数
    private String onlineScore;//网络分数    WIFI和BT时因为是临时对决  暂定不用积分
    //房间内控制
    private int myColor;//游戏时的颜色
    private int[] siteState;//房间位置信息
    ///网络玩家位置
    private String[] onlineNames;
    private String[] onlineIds;
    private String[] onlineScores;
    private int[] onlinePos;
    private int playerNumber;

    public DataManager(){//加载本地数据
        myColor=0;
        siteState =new int[4];
        needLogin=true;
        onlineNames=new String[4];
        onlineIds=new String[4];
        onlineScores=new String[4];
        onlinePos=new int[4];
    }

    //////////////////////////////////////////////////getter
    public String getUserName(){//返回用户名
        return userName;
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

    public int getMyColor(){//得到颜色
        return  myColor;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public int[] getSiteState(){
        return siteState;
    }

    public String getId(){
        return id;
    }

    public String getRoomId(){
        return roomId;
    }

    public boolean needLogin(){
        return needLogin;
    }

    public String[] getOnlineNames(){
        return onlineNames;
    }

    public String[] getOnlineIds(){
        return onlineIds;
    }

    public String[] getOnlineScores(){
        return onlineScores;
    }

    public int[] getOnlinePos(){
        return onlinePos;
    }

    public String getOnlineScore(){
        return onlineScore;
    }
    /////////////////////////////////////////////////////setter

    public void setUserName(String userName){//设置用户名
        this.userName=userName;
    }

    public void setPassword(String password){//设置密码 并使用加密存储

    }

    public void addScore(int score){
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

    public void setMusicVolume(int musicVolume){//设置音乐音量
        this.musicVolume = musicVolume;
    }

    public void setEffectVolume(int effectVolume){//设置音效音量
        this.effectVolume=effectVolume;
    }

    public void setGameMode(int gameMode){//设置当前的游戏模式
        this.gameMode=gameMode;
    }

    public void setMyColor(int myColor){//设置颜色
        this.myColor=myColor;
    }

    public void setSiteState(int[] pos){
        this.siteState =pos;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setRoomId(String roomId){
        this.roomId=roomId;
    }

    public void setLogin(boolean nl){
        needLogin=!nl;
    }

    public void setOnlineScores(int n,String scores){
        onlineScores[n]=scores;
    }

    public void setOnlineNames(int n,String name){
        onlineNames[n]=name;
    }

    public void setOnlineIds(int n,String id){
        onlineIds[n]=id;
    }

    public void setOnlinePos(int n,String pos){
        this.onlinePos[n]=Integer.valueOf(pos);
    }

    public void setPlayerNumber(int playerNumber){
        this.playerNumber=playerNumber;
    }

     void setOnlineScore(String score){
        this.onlineScore=score;
    }
}

