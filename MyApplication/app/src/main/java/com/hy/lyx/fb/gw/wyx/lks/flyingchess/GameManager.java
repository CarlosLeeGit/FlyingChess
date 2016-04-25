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
            //UI update
            for(int i=0;i<15;i++){
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("dice",String.format("%d",dice));
                msg.setData(b);
                msg.what=2;
                board.handler.sendMessage(msg);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(Game.getPlayer().canIMove(color,dice)){//can move a plane
                //get plane
                do {
                    whichPlane=Game.getPlayer().choosePlane();
                }while(!Game.getPlayer().move(color,whichPlane,dice));
                ///UI update
                Message msg2 = new Message();
                Bundle b2 = new Bundle();
                b2.putInt("color",color);
                b2.putInt("whichPlane",whichPlane);
                b2.putInt("pos",Game.getChessBoard().getAirplane(color).position[whichPlane]);
                msg2.setData(b2);
                msg2.what=1;
                board.handler.sendMessage(msg2);
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
                    //UI
                    for(int i=0;i<15;i++){
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("dice",String.format("%d",Game.getChessBoard().getDice().roll()));
                        msg.setData(b);
                        msg.what=2;
                        board.handler.sendMessage(msg);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("dice",String.format("%d",dice));
                    msg.setData(b);
                    msg.what=2;
                    board.handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(Game.getPlayer().canIMove(color,dice)){
                        do{
                            whichPlane=r.nextInt(4);
                        }while(!Game.getPlayer().move(color,whichPlane,dice));
                        ///UI update
                        Message msg2 = new Message();
                        Bundle b2 = new Bundle();
                        b2.putInt("color",color);
                        b2.putInt("whichPlane",whichPlane);
                        b2.putInt("pos",Game.getChessBoard().getAirplane(color).position[whichPlane]);
                        msg2.setData(b2);
                        msg2.what=1;
                        board.handler.sendMessage(msg2);
                        //
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                    break;
                case DataManager.GM_LAN:
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