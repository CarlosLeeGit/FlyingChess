package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by karthur on 2016/4/16.
 */
public class Game{
    private static GameManager gameManager;
    private static DataManager dataManager;
    private static ChessBoard chessBoard;
    private static Player player;
    private static SocketManager socketManager;

    public static void init(AppCompatActivity activity){
        gameManager = new GameManager();
        dataManager=new DataManager();
        chessBoard=new ChessBoard();
        player=new Player();
        socketManager = new SocketManager(activity);
    }

    public static GameManager getGameManager(){
        return gameManager;
    }

    public static DataManager getDataManager(){
        return dataManager;
    }

    public static ChessBoard getChessBoard(){
        return chessBoard;
    }

    public static Player getPlayer(){
        return player;
    }

    public static SocketManager getSocketManager(){
        return socketManager;
    }




}
