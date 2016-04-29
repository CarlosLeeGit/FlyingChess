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
        Game.chessBoard.init();
        this.board=board;
        new Thread(gw).start();
    }

    public void gameOver(){
        gw.exit();
    }

    public void turnTo(int color){//call by other thread  be careful
        int dice,whichPlane;
        if(color==Game.playerMapData.get("me").color){//it is my turn
            //get dice
            dice=Player.roll();
            //UI update
            for(int i=0;i<15;i++){
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("dice",String.format("%d",Game.chessBoard.getDice().roll()));
                msg.setData(b);
                msg.what=2;
                board.handler.sendMessage(msg);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.gc();
            if(Player.canIMove(color,dice)){//can move a plane
                //get plane
                do {
                    whichPlane=Player.choosePlane();
                }while(!Player.move(color,whichPlane,dice));
                ///UI update
                Message msg2 = new Message();
                Bundle b2 = new Bundle();
                b2.putInt("color",color);
                b2.putInt("whichPlane",whichPlane);
                b2.putInt("pos",Game.chessBoard.getAirplane(color).position[whichPlane]);
                msg2.setData(b2);
                msg2.what=1;
                board.handler.sendMessage(msg2);
            }
            else{

            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{//others
            switch (Game.dataManager.getGameMode()){
                case DataManager.GM_LOCAL://local game
                {
                    Random r=new Random(System.currentTimeMillis());
                    dice=r.nextInt(6)+1;
                    //UI
                    for(int i=0;i<15;i++){
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("dice",String.format("%d",Game.chessBoard.getDice().roll()));
                        msg.setData(b);
                        msg.what=2;
                        board.handler.sendMessage(msg);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.gc();
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

                    if(Player.canIMove(color,dice)){
                        do{
                            whichPlane=r.nextInt(4);
                        }while(!Player.move(color,whichPlane,dice));
                        ///UI update
                        Message msg2 = new Message();
                        Bundle b2 = new Bundle();
                        b2.putInt("color",color);
                        b2.putInt("whichPlane",whichPlane);
                        b2.putInt("pos",Game.chessBoard.getAirplane(color).position[whichPlane]);
                        msg2.setData(b2);
                        msg2.what=1;
                        board.handler.sendMessage(msg2);
                        //
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
            for(String key:Game.playerMapData.keySet()){
                if(Game.playerMapData.get(key).color==i){
                    Game.gameManager.turnTo(i);
                    break;
                }
            }
            i++;
        }
    }

    public void exit(){
        run=false;
    }
}