package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/16.
 */
public class Player {//user behavior
    private boolean diceValid,planeValid;
    private byte dice,whichPlane;

    public Player(){
        diceValid=false;
        planeValid=false;
    }

    public boolean canIMove(byte color,byte dice){//test whether i can move a plane
        if(dice%2==0){//dice=2 4 6
            return true;
        }
        else{
            byte[] p = Game.getChessBoard().getAirplane(color).position;
            for(int i=0;i<4;i++){
                if(p[i]>=0)//-1 means stop,-2 means over;
                    return true;
            }
        }
        return false;
    }

    public boolean move(byte color,byte whichPlane,byte dice){
        return true;
    }

    public void setDiceValid(){
        this.dice=Game.getChessBoard().getDice().roll();
        diceValid=true;
    }

    public void setPlaneValid(byte whichPlane){
        this.whichPlane=whichPlane;
        planeValid=true;
    }

    public byte roll(){
        while(!diceValid) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        diceValid=false;
        return dice;
    }

    public byte choosPlane(){
        while(!planeValid){
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        planeValid=false;
        return whichPlane;
    }
}
