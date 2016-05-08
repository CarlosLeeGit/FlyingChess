package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by karthur on 2016/4/9.
 */
public class DataManager {//数据存储类
    public static final int GM_LOCAL=0,GM_LAN=1,GM_WLAN=3;//游戏模式
    private boolean autoLogin;//需要登录
    private String roomId;//房间ID
    private int gameMode;//游戏模式
    private int lastWinner;
    Data data;

    public DataManager(){//加载本地数据
        autoLogin=false;
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess/data.dat");
        if(file.exists()){
            try {
                FileInputStream fis=new FileInputStream(file);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                Gson gson = new Gson();
                data = gson.fromJson(new String(bytes,"utf-8"),Data.class);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            File file2 = new File(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess");
            file2.mkdirs();
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                data = new Data();
                data.effectVolume=255;
                data.musicVolume=255;
                data.ip="115.159.183.72";
                data.myName="me";
                data.myName="x";
                data.score=0;
                Gson gson=new Gson();
                byte[] bytes = gson.toJson(data).getBytes("utf-8");
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //////////////////////////////////////////////////getter
    public String getMyName(){//返回用户名
        return data.myName;
    }

    public String getPassword(){//返回用户密码
        return data.password;
    }

    public int getMusicVolume(){//得到音乐音量
        return data.musicVolume;
    }

    public int getEffectVolume(){//得到音效音量
        return data.effectVolume;
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

    public int getScore(){
        return (data.score);
    }

    public int getLastWinner(){
        return lastWinner;
    }
    /////////////////////////////////////////////////////setter

    public void setMyName(String myName){//设置用户名
        data.myName=myName;
    }

    public void setPassword(String password){//设置密码 并使用加密存储
        data.password=password;
    }

    public void setMusicVolume(int musicVolume){//设置音乐音量
        data.musicVolume = musicVolume;
    }

    public void setEffectVolume(int effectVolume){//设置音效音量
        data.effectVolume=effectVolume;
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

    public void setScore(int score){
        data.score=score;
    }

    public void setWinner(int winner){
        lastWinner=winner;
    }
    //////////////////////////////////
    public void saveData(){
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess/data.dat");
        if(file.exists()){
            try {
                FileOutputStream fos = new FileOutputStream(file);
                Gson gson = new Gson();
                fos.write(gson.toJson(data).getBytes("utf-8"));
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Data{
    String ip;
    String myName;
    String password;
    int musicVolume,effectVolume;//0~255 音量
    int score;
    public Data(){
        ip=new String();
        myName=new String();
        password=new String();
        musicVolume=0;
        effectVolume=0;
        score=0;
    }
}