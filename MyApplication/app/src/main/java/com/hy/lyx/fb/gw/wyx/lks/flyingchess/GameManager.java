package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.os.Bundle;
import android.os.Message;

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

    public void turnTo(int color){//call by other thread  be careful
        int dice,whichPlane;
        if(color==Game.getDataManager().getMyColor()){//it is my turn
            //get dice
            dice=Game.getPlayer().roll();
            if(Game.getPlayer().canIMove(color,dice)){//can move a plane
                //get plane
                do {
                    whichPlane=Game.getPlayer().choosePlane();
                }while(!Game.getPlayer().move(color,whichPlane,dice));
                ///UI update
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("hh",String.format("dice:%d 1:%d 2:%d 3:%d 4:%d",dice,Game.getChessBoard().getAirplane(color).position[0],Game.getChessBoard().getAirplane(color).position[1],Game.getChessBoard().getAirplane(color).position[2],Game.getChessBoard().getAirplane(color).position[3]));
                msg.setData(b);
                msg.what=1;
                ChessBoardAct.handler.sendMessage(msg);
            }
            else{

            }
        }
        else{//others
            switch (Game.getDataManager().getGameMode()){
                case DataManager.GM_LOCAL://local game
                {
                    Random r=new Random(System.currentTimeMillis());
                    dice=r.nextInt(6)+1;
                    if(Game.getPlayer().canIMove(color,dice)){
                        do{
                            whichPlane=r.nextInt(4);
                        }while(!Game.getPlayer().move(color,whichPlane,dice));
                        ///UI update
                    }
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
        int i=0;
        int n=Game.getDataManager().getPlayerNumber();
        while(run){//control round
            i=(i%4);
            Game.getGameManager().turnTo(Game.getDataManager().getPlayerOrder()[i]);
            i++;
        }
    }

    public void exit(){
        run=false;
    }
}