package com.flashminds.flyingchess.manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.flashminds.flyingchess.dataPack.DataPack;
import com.flashminds.flyingchess.entity.Game;
import com.flashminds.flyingchess.entity.Role;
import com.flashminds.flyingchess.dataPack.Target;
import com.flashminds.flyingchess.activity.ChessBoardActivity;
import com.flashminds.flyingchess.activity.GameInfoActivity;

import java.util.LinkedList;

/**
 * Created by karthur on 2016/4/9.
 */
public class GameManager implements Target {//game process control
    private GameWorker gw;//thread
    private ChessBoardActivity board;
    private boolean finished;
    private int dice, whichPlane;

    public GameManager() {
        Game.socketManager.registerActivity(DataPack.E_GAME_PROCEED_PLANE, this);
        Game.socketManager.registerActivity(DataPack.E_GAME_PROCEED_DICE, this);
        Game.socketManager.registerActivity(DataPack.E_GAME_FINISHED, this);
        Game.socketManager.registerActivity(DataPack.E_GAME_PLAYER_DISCONNECTED, this);
    }

    public void newTurn(ChessBoardActivity board) {//call by activity when game start
        Game.chessBoard.init();
        this.board = board;
        finished = false;
        gw = new GameWorker();
        new Thread(gw).start();
        Game.logManager.p("new Turn");
    }

    public void gameOver() {
        Game.logManager.p("game over");
        gw.exit();
    }

    public void turnTo(int color) {//call by other thread  be careful
        Role role = null;
        for (String key : Game.playersData.keySet()) {
            if (Game.playersData.get(key).color == color) {
                role = Game.playersData.get(key);
            }
        }
        if (role != null) {//此颜色有玩家
            Game.logManager.p("turn to:", role.name, " color is:", role.color);
            Message msg = new Message();
            Bundle b = new Bundle();
            b.putInt("color", color);
            msg.setData(b);
            msg.what = 6;
            board.handler.sendMessage(msg);

            whichPlane = -1;
            Game.logManager.p("turn to:", "waite for dice");

            if (Game.replayManager.isReplay == true) {
                dice = Game.replayManager.getSavedDice();
                role.setDice(dice);
            } else
                dice = role.roll();

            Game.replayManager.saveDice(dice);
            Game.logManager.p("turn to:", "dice is :", dice);
            if (!Game.replayManager.isReplay && Game.dataManager.getGameMode() != DataManager.GM_LOCAL) {
                if ((role.offline || role.type == Role.ROBOT) && Game.dataManager.getHostId().compareTo(Game.dataManager.getMyId()) == 0 || role.type == Role.ME) {
                    Game.socketManager.send(DataPack.R_GAME_PROCEED_DICE, role.id, Game.dataManager.getRoomId(), dice);
                    Game.logManager.p("turn to:", "send dice:", dice);
                }
            }
            Game.soundManager.playSound(SoundManager.DICE);
            diceAnimate(dice);
            Game.delay(200);
            boolean canFly = false;
            if (role.canIMove()) {
                if (Game.replayManager.isReplay == true) {
                    whichPlane = Game.replayManager.getSavedWhichPlane();
                    role.setWhichPlane(whichPlane);
                    role.move();
                } else {
                    do {
                        Game.logManager.p("turu to: wait for which plane");
                        whichPlane = role.choosePlane();
                        Game.logManager.p("turn to: plane result:", whichPlane);
                    } while (!role.move());
                }
                canFly = true;
                Game.replayManager.saveWhichPlane(whichPlane);
            } else if (role.type == Role.ME) {
                toast("sad...I can not move");
                Game.logManager.p("turn to: i can not fly");
            }
            if (!Game.replayManager.isReplay && Game.dataManager.getGameMode() != DataManager.GM_LOCAL) {
                if ((role.offline || role.type != Role.PLAYER) && Game.dataManager.getHostId().compareTo(Game.dataManager.getMyId()) == 0 || role.type == Role.ME) {
                    Game.socketManager.send(DataPack.R_GAME_PROCEED_PLANE, role.id, Game.dataManager.getRoomId(), whichPlane);
                    Game.logManager.p("turn to: send which plane :", whichPlane);
                }
            }
            if (canFly) {
                flyNow(color, whichPlane);
                amIWin(role.id, color);
            }
            Game.delay(200);
        }
    }

    private void amIWin(String id, int color) {
        boolean win = true;
        for (int i = 0; i < 4; i++) {
            if (Game.chessBoard.getAirplane(color).position[i] != -2) {
                win = false;
            }
        }
        if (win) {
            if (Integer.valueOf(id) < 0) {
                String[] s = {"Red", "Green", "Blue", "Yellow"};
                toast(s[color] + " robot is the winner!");
                if (Game.dataManager.getGameMode() == DataManager.GM_WLAN && Game.dataManager.getHostId().compareTo(Game.dataManager.getMyId()) == 0) {//我是房主
                    LinkedList<String> msgs = new LinkedList<>();
                    msgs.addLast(id);
                    msgs.addLast(Game.dataManager.getRoomId());
                    msgs.addLast("ROBOT");
                    Game.socketManager.send(new DataPack(DataPack.R_GAME_FINISHED, msgs));
                }
            } else if (id.compareTo(Game.dataManager.getMyId()) == 0) {//我赢了
                toast("I am the winner!");
                if (Game.dataManager.getGameMode() == DataManager.GM_WLAN) {
                    LinkedList<String> msgs = new LinkedList<>();
                    msgs.addLast(id);
                    msgs.addLast(Game.dataManager.getRoomId());
                    msgs.addLast(Game.dataManager.getMyName());
                    Game.socketManager.send(new DataPack(DataPack.R_GAME_FINISHED, msgs));
                }
            } else {//player
                toast("player" + Game.playersData.get(id).name + "is the winner!");
                if (Game.dataManager.getGameMode() == DataManager.GM_WLAN && Game.playersData.get(id).offline && Game.dataManager.getHostId().compareTo(Game.dataManager.getMyId()) == 0) {//掉线且我是房主
                    LinkedList<String> msgs = new LinkedList<>();
                    msgs.addLast(id);
                    msgs.addLast(id);
                    msgs.addLast(Game.playersData.get(id).name);
                    Game.socketManager.send(new DataPack(DataPack.R_GAME_FINISHED, msgs));
                }
            }
            Game.dataManager.setWinner(id);
            gameOver();
            Message msg = new Message();
            msg.what = 5;
            board.handler.sendMessage(msg);
        }
    }

    private void diceAnimate(int dice) {
        for (int i = 0; i < 10; i++) {
            Message msg = new Message();
            Bundle b = new Bundle();
            b.putInt("dice", Game.chessBoard.getDice().roll());
            msg.setData(b);
            msg.what = 2;
            board.handler.sendMessage(msg);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putInt("dice", dice);
        msg.setData(b);
        msg.what = 2;
        board.handler.sendMessage(msg);
    }

    private void planeAnimate(int color, int pos) {
        Message msg2 = new Message();
        Bundle b2 = new Bundle();
        b2.putInt("color", color);
        b2.putInt("whichPlane", whichPlane);
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
        Game.soundManager.playSound(SoundManager.FLYCRASH);
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putInt("color", color);
        b.putInt("whichPlane", crashPlane);
        b.putInt("pos", Game.chessBoard.getAirplane(color).position[crashPlane]);
        msg.setData(b);
        //Game.logManager.p("planeCrash:","color:",color,"crashplane:",crashPlane,"position:",Game.chessBoard.getAirplane(color).position[crashPlane]);
        msg.what = 4;
        board.handler.sendMessage(msg);
    }

    private void flyNow(int color, int whichPlane) {
        int toPos = Game.chessBoard.getAirplane(color).position[whichPlane];
        int curPos = Game.chessBoard.getAirplane(color).lastPosition[whichPlane];
        if (curPos + dice == toPos || curPos == -1) {
            for (int pos = curPos + 1; pos <= toPos; pos++) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            crash(color, toPos, whichPlane);
        } else if (curPos + dice + 4 == toPos) { // short jump
            for (int pos = curPos + 1; pos <= curPos + dice; pos++) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            crash(color, curPos + dice, whichPlane);
            Game.soundManager.playSound(SoundManager.FLYMID);
            planeAnimate(color, toPos);
            crash(color, toPos, whichPlane);
        } else if (toPos == 30) { // short jump and then long jump
            for (int pos = curPos + 1; pos <= curPos + dice; pos++) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            crash(color, curPos + dice, whichPlane);
            Game.soundManager.playSound(SoundManager.FLYMID);
            planeAnimate(color, 18);
            crash(color, 18, whichPlane);
            Game.soundManager.playSound(SoundManager.FLYLONG);
            planeAnimate(color, 30);
            crash(color, 30, whichPlane);
        } else if (toPos == 34) { // long jump and then short jump
            for (int pos = curPos + 1; pos <= 18; pos++) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            crash(color, 18, whichPlane);
            Game.soundManager.playSound(SoundManager.FLYLONG);
            planeAnimate(color, 30);
            crash(color, 30, whichPlane);
            Game.soundManager.playSound(SoundManager.FLYMID);
            planeAnimate(color, 34);
            crash(color, 34, whichPlane);
        } else if (Game.chessBoard.isOverflow()) { // overflow
            for (int pos = curPos + 1; pos <= 56; pos++) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            for (int pos = 55; pos >= toPos; pos--) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            crash(color, toPos, whichPlane);
            Game.chessBoard.setOverflow(false);
        } else if (toPos == -2) {
            for (int pos = curPos + 1; pos <= 55; pos++) {
                Game.soundManager.playSound(SoundManager.FLYSHORT);
                planeAnimate(color, pos);
            }
            Game.soundManager.playSound(SoundManager.ARRIVE);
            planeAnimate(color, 56);
        }
    }

    public void crash(int color, int pos, int whichPlane) {
        Game.logManager.p("crash:", "my color:", color, "my pos:", pos, "which plane:", whichPlane);
        if (pos >= 50)//不被人撞
            return;
        int crashColor = color;
        int crashPlane = whichPlane;
        int count = 0;
        int curX = Game.chessBoard.map[color][pos][0];
        int curY = Game.chessBoard.map[color][pos][1];
        for (int i = 0; i < 4; i++) {
            if (i != color) {
                Game.logManager.p("crash:", "find color:", i);
                for (int j = 0; j < 4; j++) {
                    int crashPos = Game.chessBoard.getAirplane(i).position[j];
                    if (crashPos > 0) {
                        Game.logManager.p("crash:", "find plane:", j, "position:", crashPos, "(x,y):", "(", Game.chessBoard.map[i][crashPos][0], ",", Game.chessBoard.map[i][crashPos][1], ")");
                    } else {
                        Game.logManager.p("crash:", "find plane:", j, "position:", crashPos, "(x,y):", "(", "home", ")");
                    }
                    if (crashPos > 0) {
                        if (Game.chessBoard.map[i][crashPos][0] == curX && Game.chessBoard.map[i][crashPos][1] == curY) {//撞别人
                            Game.logManager.p("crash:", "crash success");
                            crashPlane = j;
                            count++;
                        }
                    }
                }
                if (count == 1) {
                    crashColor = i;
                    break;
                }
                if (count > 1) {
                    crashPlane = whichPlane;
                    break;
                }
            }
        }
        if (count >= 1) {
            planeCrash(crashColor, crashPlane);
            Game.chessBoard.getAirplane(crashColor).position[crashPlane] = -1;
            Game.chessBoard.getAirplane(crashColor).lastPosition[crashPlane] = -1;
        }
    }


    private void toast(String msgs) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", msgs);
        msg.setData(b);
        msg.what = 3;
        board.handler.sendMessage(msg);
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        switch (dataPack.getCommand()) {
            case DataPack.E_GAME_FINISHED:
                finished = true;
                break;
            case DataPack.E_GAME_PROCEED_DICE:
                if ((Integer.valueOf(dataPack.getMessage(0)) < 0 || Game.playersData.get(dataPack.getMessage(0)).offline) && Game.dataManager.getMyId().compareTo(Game.dataManager.getHostId()) != 0 || Game.playersData.get(dataPack.getMessage(0)).type == Role.PLAYER) {//机器人且我不是房主  或者 是玩家且没有掉线 或者 是玩家且掉线当我不是房主
                    Game.playersData.get(dataPack.getMessage(0)).setDiceValid(Integer.valueOf(dataPack.getMessage(2)));
                    //Game.logManager.p("processDataPack:","dice:",dataPack.getMessage(2));
                }
                break;
            case DataPack.E_GAME_PROCEED_PLANE:
                if ((Integer.valueOf(dataPack.getMessage(0)) < 0 || Game.playersData.get(dataPack.getMessage(0)).offline) && Game.dataManager.getMyId().compareTo(Game.dataManager.getHostId()) != 0 || Game.playersData.get(dataPack.getMessage(0)).type == Role.PLAYER) {//机器人且我不是房主  或者 是玩家且没有掉线 或者 是玩家且掉线当我不是房主
                    if (Integer.valueOf(dataPack.getMessage(2)) >= 0) {
                        Game.playersData.get(dataPack.getMessage(0)).setPlaneValid(Integer.valueOf(dataPack.getMessage(2)));
                    }
                    //Game.logManager.p("processDataPack:", "plane:", dataPack.getMessage(2));
                }
                break;
            case DataPack.E_GAME_PLAYER_DISCONNECTED:
                if (dataPack.getMessage(0).compareTo(Game.dataManager.getMyId()) != 0) {//不是我
                    if (dataPack.getMessage(0).compareTo(Game.dataManager.getHostId()) == 0) {//是房主  退出游戏
                        gameOver();
                        board.startActivity(new Intent(board.getApplicationContext(), GameInfoActivity.class));
                        Game.dataManager.giveUp(false);
                    } else {//由电脑托管
                        Game.playersData.get(dataPack.getMessage(0)).offline = true;
                    }
                }
                break;
        }
    }
}

class GameWorker implements Runnable {
    private boolean run;

    public GameWorker() {
        run = true;
    }

    @Override
    public void run() {
        run = true;
        int i = 0;
        while (run) {//control round
            i = (i % 4);//轮询颜色
            Game.gameManager.turnTo(i);
            i++;
        }
    }

    public void exit() {
        run = false;
    }
}