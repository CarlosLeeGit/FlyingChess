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
    public static HashMap<String,Player> playerMapData;//me host z x c

    public static void init(AppCompatActivity activity){
        gameManager = new GameManager();
        dataManager=new DataManager();
        chessBoard=new ChessBoard();
        socketManager = new SocketManager(activity);
        playerMapData=new HashMap<>();
        playerMapData.put("me",new Player("0","ME","0",0));
    }
}
