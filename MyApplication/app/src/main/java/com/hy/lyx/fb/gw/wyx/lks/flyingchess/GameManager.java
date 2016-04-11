package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class GameManager {
    private static DataManager dataManager;
    private static ChessBoard chessBoard;
    private static byte dice;
    private static boolean diceValid;
    private static byte whichPlane;
    private static boolean planeValid;
    private static GameWorker gw;//thread

    public static void init(){//call when app launch
        dataManager=new DataManager();
        chessBoard=new ChessBoard();
        dataManager.init();
        chessBoard.init();
        gw=new GameWorker();
    }

    public static void start(){//call by activity when game start
        Random r = new Random(System.currentTimeMillis());
        dataManager.setMyColor((byte) r.nextInt(4));
        new Thread(gw).start();
    }

    public static void throwDice(){//call by user when press dice button
        dice = chessBoard.throwDice();
        diceValid =true;
    }

    public static void choosePlane(byte _whichPlane){//call by user when choose plane
        whichPlane=_whichPlane;
        planeValid =true;
    }

    public static void turnTo(byte color){//call by other thread  be careful
        if(color==dataManager.getMyColor()){//it is my turn
            //get dice
            while(diceValid){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            diceValid =false;
            //get plane
            while(planeValid){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            planeValid =false;
            if(chessBoard.canIMove(color,dice)){//can move a plane

            }
            else{

            }
        }
        else{//others
            switch (dataManager.getGameMode()){
                case DataManager.GM_LOCAL://local game
                {

                }
                    break;
                case DataManager.GM_BT:
                    break;
                case DataManager.GM_WIFI:
                    break;
                case DataManager.GM_WLAN:
                    break;
            }
        }

    }

    public static DataManager getDataManager(){//common api
        return dataManager;
    }

}

class GameWorker implements Runnable{
    @Override
    public void run() {
        byte i=0;
        for (;;){//control round
            i=(byte)(i%4);
            GameManager.turnTo(i);
            i++;
        }
    }
}