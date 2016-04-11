package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/9.
 */
public class GameManager {
    private static DataManager dataManager;
    private static ChessBoard chessBoard;

    public static void init(){
        dataManager=new DataManager();
        chessBoard=new ChessBoard();
        dataManager.init();
        chessBoard.init();
    }

    public static DataManager getDataManager(){
        return dataManager;
    }
    public static ChessBoard getChessBoard(){
        return chessBoard;
    }

}
