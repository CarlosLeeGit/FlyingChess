package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.os.Bundle;
import android.os.Message;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class GameManager {//game process control
    private GameWorker gw;//thread
    private ChessBoardAct board;
    public GameManager(){
        gw=new GameWorker();
    }

    public void newTurn(ChessBoardAct board){//call by activity when game start
        Game.getChessBoard().init();
        this.board=board;
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

            Message msg = new Message();
            Bundle b = new Bundle();
            b.putString("dice",String.format("%d",dice));
            msg.setData(b);
            msg.what=2;
            board.handler.sendMessage(msg);

            if(Game.getPlayer().canIMove(color,dice)){//can move a plane
                //get plane
                do {
                    whichPlane=Game.getPlayer().choosePlane();
                }while(!Game.getPlayer().move(color,whichPlane,dice));
                ///UI update
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
        while(run){//control round
            i=(i%4);
            if(Game.getDataManager().getPosition()[i]!=-1) {
                Game.getGameManager().turnTo(i);
            }
            i++;
        }
    }

    public void exit(){
        run=false;
    }
}