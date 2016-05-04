package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by karthur on 2016/4/16.
 */
public class Game{
    public static GameManager gameManager;
    public static DataManager dataManager;
    public static ChessBoard chessBoard;
    public static SocketManager socketManager;
    public static HashMap<String,Player> playerMapData;//me host
    public static ActivityManager activityManager;
    public static Sound sound;

    public static void init(AppCompatActivity activity){
        dataManager=new DataManager();
        socketManager = new SocketManager(activity);
        gameManager = new GameManager();
        chessBoard=new ChessBoard();
        playerMapData=new HashMap<>();
        playerMapData.put("me",new Player("0","ME","0",0));
        activityManager=new ActivityManager();
        sound = new Sound();
        sound.init(activity.getApplicationContext());
    }
}
