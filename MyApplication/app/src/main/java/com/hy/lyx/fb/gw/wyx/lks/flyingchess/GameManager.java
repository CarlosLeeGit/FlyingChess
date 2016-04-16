package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class GameManager {//game process control
    private GameWorker gw;//thread

    public GameManager(){
        gw=new GameWorker();
    }

    public void newTurn(){//call by activity when game start
        Game.getChessBoard().init();
        new Thread(gw).start();
    }

    public void gameOver(){
        gw.exit();
    }

    public void turnTo(byte color){//call by other thread  be careful
        byte dice,whichPlane;
        if(color==Game.getDataManager().getMyColor()){//it is my turn
            //get dice
            dice=Game.getPlayer().roll();
            //get plane
            whichPlane=Game.getPlayer().choosPlane();
            if(Game.getPlayer().canIMove(color,dice)){//can move a plane

            }
            else{

            }
        }
        else{//others
            switch (Game.getDataManager().getGameMode()){
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

}

class GameWorker implements Runnable{
    private boolean run;

    public GameWorker(){
        run=true;
    }

    @Override
    public void run() {
        byte i=0;
        byte n=Game.getDataManager().getPlayerNumber();
        while(run){//control round
            i=(byte) (i%n);
            Game.getGameManager().turnTo(Game.getDataManager().getPlayerOrder()[i]);
            i++;
        }
    }

    public void exit(){
        run=false;
    }
}