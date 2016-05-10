package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class GameManager implements Target {//game process control
    private GameWorker gw;//thread
    private ChessBoardAct board;
    private boolean waitForPlane,waitForDice,finished;
    private int dice,whichPlane;
    private String winnerId;
    private String winner;
    private ArrayList<Integer> avaPlaneId;
    private boolean[] offline;
    public GameManager(){
        Game.socketManager.registerActivity(DataPack.E_GAME_PROCEED_PLANE,this);
        Game.socketManager.registerActivity(DataPack.E_GAME_PROCEED_DICE,this);
        Game.socketManager.registerActivity(DataPack.E_GAME_FINISHED,this);
        Game.socketManager.registerActivity(DataPack.E_GAME_PLAYER_DISCONNECTED,this);
    }

    public void newTurn(ChessBoardAct board){//call by activity when game start
        Game.chessBoard.init();
        this.board=board;
        finished=false;
        gw=new GameWorker();
        new Thread(gw).start();
        offline=new boolean[4];
        offline[0]=false;
        offline[1]=false;
        offline[2]=false;
        offline[3]=false;
    }

    public void gameOver(){
        gw.exit();
    }

    public void turnTo(int color){//call by other thread  be careful
        if(color==Game.playerMapData.get("me").color){//it is my turn
            //get dice
            dice=Player.roll();

            if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){///发送网络信息
                LinkedList<String> msgs=new LinkedList<>();
                msgs.addLast(Game.playerMapData.get("me").id);
                msgs.addLast(Game.dataManager.getRoomId());
                msgs.addLast(String.format("%d",dice));
                Game.socketManager.send(new DataPack(DataPack.R_GAME_PROCEED_DICE,msgs));
            }
            Game.sound.roll();
            diceAnimate(dice);
            //get plane
            boolean canFly=false;
            if(Player.canIMove(color,dice)){//can move a plane
                //get plane
                do {
                    whichPlane=Player.choosePlane(color,dice);
                }while(!Player.move(color,whichPlane,dice));
                ///UI update
                canFly=true;
            }
            else{
                toast("i can not move");
            }

            //tell other players
            if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                LinkedList<String> msgs2=new LinkedList<>();
                msgs2.addLast(Game.playerMapData.get("me").id);
                msgs2.addLast(Game.dataManager.getRoomId());
                msgs2.addLast(String.format("%d",whichPlane));
                Game.socketManager.send(new DataPack(DataPack.R_GAME_PROCEED_PLANE,msgs2));
            }
            if(canFly){
                flyNow(color);
                amIWin(Game.playerMapData.get("me").id,color);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        else{//others
            switch (Game.dataManager.getGameMode()){
                case DataManager.GM_LOCAL://local game
                {//////////////////////////AI
                    //dice/
                    Random r=new Random(System.currentTimeMillis());
                    dice=r.nextInt(6)+1;
                    Game.sound.roll();
                    diceAnimate(dice);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //plane
                    if(Player.canIMove(color,dice)){
                        do{
                            avaPlaneId=Player.getAvaPlaneId(color,dice);
                            whichPlane=avaPlaneId.get(r.nextInt(avaPlaneId.size()));
                        }while(!Player.move(color,whichPlane,dice));
                        ///UI update
                        flyNow(color);
                        //
                        amIWin("-1",color);
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    break;
                case DataManager.GM_LAN:
                    break;
                case DataManager.GM_WLAN:
                {
                    waitForPlane=true;
                    waitForDice=true;
                    if(Game.playerMapData.get("host").id.compareTo(Game.playerMapData.get("me").id)==0&&Game.playerMapData.containsKey(String.format("%d",-color-1)))
                    {
                        Random r=new Random(System.currentTimeMillis());
                        dice=r.nextInt(6)+1;

                        LinkedList<String> msgs=new LinkedList<>();
                        msgs.addLast(String.valueOf(-color-1));
                        msgs.addLast(Game.dataManager.getRoomId());
                        msgs.addLast(String.format("%d",dice));
                        Game.socketManager.send(new DataPack(DataPack.R_GAME_PROCEED_DICE,msgs));

                        Game.sound.roll();
                        diceAnimate(dice);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        boolean canFly=false;
                        if(Player.canIMove(color,dice)){
                            do{
                                avaPlaneId=Player.getAvaPlaneId(color,dice);
                                whichPlane=avaPlaneId.get(r.nextInt(avaPlaneId.size()));
                            }while(!Player.move(color,whichPlane,dice));
                            ///UI update
                            canFly=true;
                        }

                        LinkedList<String> msgs2=new LinkedList<>();
                        msgs2.addLast(String.valueOf(-color-1));
                        msgs2.addLast(Game.dataManager.getRoomId());
                        msgs2.addLast(String.format("%d",whichPlane));
                        Game.socketManager.send(new DataPack(DataPack.R_GAME_PROCEED_PLANE,msgs2));

                        if(canFly){
                            flyNow(color);
                            amIWin("-1",color);
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        while(waitForDice){
                            if(offline[color]&&Game.playerMapData.get("host").id.compareTo(Game.playerMapData.get("me").id)==0){//断线且我是房主
                                Random r=new Random(System.currentTimeMillis());
                                dice=r.nextInt(6)+1;

                                LinkedList<String> msgs=new LinkedList<>();
                                msgs.addLast(Game.playerMapData.get("me").id);//设定值
                                msgs.addLast(Game.dataManager.getRoomId());
                                msgs.addLast(String.format("%d",dice));
                                Game.socketManager.send(new DataPack(DataPack.R_GAME_PROCEED_DICE,msgs));
                                break;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Game.sound.roll();
                        diceAnimate(dice);

                        while(waitForPlane) {
                            if(offline[color]&&Game.playerMapData.get("host").id.compareTo(Game.playerMapData.get("me").id)==0){//断线且我是房主
                                Random r = new Random(System.currentTimeMillis());
                                avaPlaneId=Player.getAvaPlaneId(color,dice);
                                whichPlane=avaPlaneId.get(r.nextInt(avaPlaneId.size()));

                                LinkedList<String> msgs2=new LinkedList<>();
                                msgs2.addLast(Game.playerMapData.get("me").id);//设定值
                                msgs2.addLast(Game.dataManager.getRoomId());
                                msgs2.addLast(String.format("%d",whichPlane));
                                Game.socketManager.send(new DataPack(DataPack.R_GAME_PROCEED_PLANE,msgs2));
                                break;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(Player.canIMove(color,dice)) {
                            Player.move(color, whichPlane, dice);
                            flyNow(color);
                            amIWin("z",color);
                        }
                    }
                }
                    break;
            }
        }

    }

    private void amIWin(String id,int color){
        boolean win=true;
        for(int i=0;i<4;i++){
            if(Game.chessBoard.getAirplane(color).position[i]!=-2){
                win=false;
            }
        }
        if(win){
            if(id.compareTo("z")!=0){
                if(Integer.valueOf(id)<0) {
                    String[] s = {"Red","Green","Blue","Yellow"};
                    toast(s[color]+" robot is the winner!");
                }
                else
                    toast("I am the winner!");
                if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                    LinkedList<String> msgs=new LinkedList<>();
                    msgs.addLast(id);
                    msgs.addLast(Game.dataManager.getRoomId());
                    if(Integer.valueOf(id)<0)
                        msgs.addLast("ROBOT");
                    else
                        msgs.addLast(Game.dataManager.getMyName());

                    Game.socketManager.send(new DataPack(DataPack.R_GAME_FINISHED,msgs));
                }
                Game.dataManager.setWinner(id);
            }
            else{
                while(!finished){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                toast("player"+winner+"is the winner!");
                Game.dataManager.setWinner(winnerId);
            }
            gameOver();
            Message msg = new Message();
            msg.what=5;
            board.handler.sendMessage(msg);
        }
    }

    private void diceAnimate(int dice){
        for(int i=0;i<10;i++){
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
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("dice",String.format("%d",dice));
        msg.setData(b);
        msg.what=2;
        board.handler.sendMessage(msg);
    }

    private void planeAnimate(int color, int pos){
        Message msg2 = new Message();
        Bundle b2 = new Bundle();
        b2.putInt("color",color);
        b2.putInt("whichPlane",whichPlane);
        b2.putInt("pos", pos);
        msg2.setData(b2);
        msg2.what = 1;
        board.handler.sendMessage(msg2);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void planeCrash(int color, int crashPlane) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putInt("color", color);
        b.putInt("whichPlane", crashPlane);
        b.putInt("pos", Game.chessBoard.getAirplane(color).position[crashPlane]);
        msg.setData(b);
        msg.what = 4;
        board.handler.sendMessage(msg);
    }

    private void flyNow(int color) {
            int toPos = Game.chessBoard.getAirplane(color).position[whichPlane];
            int curPos = Game.chessBoard.getAirplane(color).lastPosition[whichPlane];
            if(curPos + dice == toPos || curPos == -1) {
                for (int pos = curPos + 1; pos <= toPos; pos++) {
                    planeAnimate(color, pos);
                }
                crash(color, toPos);
            }
            else if(curPos + dice + 4 == toPos) { // short jump
                for(int pos = curPos + 1; pos <= curPos + dice; pos++) {
                    planeAnimate(color, pos);
                }
                crash(color, curPos + dice);
                planeAnimate(color, toPos);
                crash(color, curPos);
            }
            else if(toPos == 30) { // short jump and then long jump
                for(int pos = curPos + 1; pos <= curPos + dice; pos++) {
                    planeAnimate(color, pos);
                }
                crash(color, curPos + dice);
                planeAnimate(color, 18);
                crash(color, 18);
                planeAnimate(color, 30);
                crash(color, 30);
            }
            else if(toPos == 34) { // long jump and then short jump
                for(int pos =curPos + 1; pos <= 18; pos++) {
                    planeAnimate(color, pos);
                }
                crash(color, 18);
                planeAnimate(color, 30);
                crash(color, 30);
            planeAnimate(color, 34);
            crash(color, 34);
        }
        else if(Game.chessBoard.isOverflow()) { // overflow
            for (int pos = curPos + 1; pos <= 56; pos++) {
                planeAnimate(color, pos);
            }
            for(int pos = 55; pos >= toPos; pos--)
                planeAnimate(color, pos);
            crash(color, toPos);
            Game.chessBoard.setOverflow(false);
        }
        else if(toPos==-2) {
            for(int pos = curPos+1;pos<=56;pos++)
                planeAnimate(color,pos);
        }
    }

    public void crash(int color, int pos) {
        if(pos>=50)//不被人撞
            return;
        int crashColor = color;
        int crashPlane = whichPlane;
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(i != color) {
                for(int j = 0; j < 4; j++) {
                    int crackPos = Game.chessBoard.getAirplane(i).position[j];
                    int factor = (i - color + 4) % 4;
                    if(pos != 0 && crackPos != 0 && crackPos == (pos + 13 * factor) % 52&&crackPos<50) {//撞别人
                        crashPlane = j;
                        count++;
                    }
                }
                if(count == 1)
                    crashColor = i;
                if(count >= 1)
                    break;
            }
        }
        if(count >= 1) {
            planeCrash(crashColor, crashPlane);
            Game.chessBoard.getAirplane(crashColor).position[crashPlane] = -1;
            Game.chessBoard.getAirplane(crashColor).lastPosition[crashPlane] = -1;
        }
    }

    private void toast(String msgs){
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg",msgs);
        msg.setData(b);
        msg.what=3;
        board.handler.sendMessage(msg);
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        switch (dataPack.getCommand()){
            case DataPack.E_GAME_FINISHED:
                winnerId=dataPack.getMessage(0);
                winner=dataPack.getMessage(2);
                finished=true;
                break;
            case DataPack.E_GAME_PROCEED_DICE:
                dice=Integer.valueOf(dataPack.getMessage(2));
                waitForDice=false;
                break;
            case DataPack.E_GAME_PROCEED_PLANE:
                whichPlane=Integer.valueOf(dataPack.getMessage(2));
                waitForPlane=false;
                break;
            case DataPack.E_GAME_PLAYER_DISCONNECTED:
                if(dataPack.getMessage(0).compareTo(Game.playerMapData.get("me").id)!=0){//不是我
                    if(dataPack.getMessage(1).compareTo("1")==0){//退出游戏
                        gameOver();
                        board.startActivity(new Intent(board.getApplicationContext(),GameInfoAct.class));
                        Game.dataManager.giveUp(false);
                    }
                    else{//由电脑托管
                        for(String key:Game.playerMapData.keySet()){
                            if(Game.playerMapData.get(key).id.compareTo(dataPack.getMessage(0))==0){
                                Game.playerMapData.get(key).id=String.valueOf(Game.playerMapData.get(key).color*(-1)-1);
                                Game.playerMapData.get(key).name = "ROBOT";
                                offline[Game.playerMapData.get(key).color]=true;
                            }
                        }
                    }
                }
                break;
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