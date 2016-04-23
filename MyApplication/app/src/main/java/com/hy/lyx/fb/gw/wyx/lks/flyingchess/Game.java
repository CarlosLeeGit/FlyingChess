package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.os.Handler;
import android.os.Message;

/**
 * Created by karthur on 2016/4/16.
 */
public class Game {
    private static GameManager gameManager;
    private static DataManager dataManager;
    private static ChessBoard chessBoard;
    private static Player player;

    public static void init(){
        gameManager = new GameManager();
        dataManager=new DataManager();
        chessBoard=new ChessBoard();
        player=new Player();
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

}
