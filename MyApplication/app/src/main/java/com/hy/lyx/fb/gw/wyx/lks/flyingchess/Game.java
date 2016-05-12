package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

/**
 * Created by karthur on 2016/4/16.
 */
public class Game{
    public static GameManager gameManager;
    public static DataManager dataManager;
    public static ChessBoard chessBoard;
    public static SocketManager socketManager;
    public static HashMap<String,Role> playersData;
    public static ActivityManager activityManager;
    public static SoundManager soundManager;
    public static UpdateManager updateManager;

    public static void init(AppCompatActivity activity){
        dataManager=new DataManager();
        socketManager = new SocketManager(activity);
        gameManager = new GameManager();
        chessBoard=new ChessBoard();
        playersData =new HashMap<>();
        activityManager=new ActivityManager();
        soundManager = new SoundManager(activity);
        updateManager = new UpdateManager(activity);
    }

    public static void delay(int interval){
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
