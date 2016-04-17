package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/16.
 */
public class Player {//user behavior
    private boolean diceValid,planeValid,canRoll,canChoosePlane;
    private int dice,whichPlane;

    public Player(){
        diceValid=false;
        planeValid=false;
        canRoll=false;
        canChoosePlane=false;
    }

    public boolean canIMove(int color,int dice){//test whether i can move a plane
        if(dice%2==0){//dice=2 4 6
            return true;
        }
        else{
            int[] p = Game.getChessBoard().getAirplane(color).position;
            for(int i=0;i<4;i++){
                if(p[i]>=0)//-1 means stop,-2 means over;
                    return true;
            }
        }
        return false;
    }

    public boolean move(int color,int whichPlane,int dice){
        if((Game.getChessBoard().getAirplane(color).position[whichPlane]==-1&&dice%2!=0)||Game.getChessBoard().getAirplane(color).position[whichPlane]==-2)
            return false;
        if(Game.getChessBoard().getAirplane(color).position[whichPlane]==-1){
            Game.getChessBoard().getAirplane(color).position[whichPlane]=0;
            return true;
        }
        int nextStep=Game.getChessBoard().getAirplane(color).position[whichPlane]+dice;
        if(nextStep>56)
            nextStep=56-(nextStep-56);
        else if(nextStep==56)
            nextStep=-2;
        else if(nextStep==18)
            nextStep=30;
        else if(nextStep<50){
            if((nextStep-2)%4==0)
                nextStep+=4;
        }
        Game.getChessBoard().getAirplane(color).position[whichPlane]=nextStep;
        return true;
    }

    public void setDiceValid(){
        if(canRoll){
            this.dice=Game.getChessBoard().getDice().roll();
            diceValid=true;
        }
    }

    public void setPlaneValid(int whichPlane){
        if(canChoosePlane){
            this.whichPlane=whichPlane;
            planeValid=true;
        }
    }

    public int roll(){
        canRoll=true;
        while(!diceValid) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        canRoll=false;
        diceValid=false;
        return dice;
    }

    public int choosePlane(){
        canChoosePlane=true;
        while(!planeValid){
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        canChoosePlane=false;
        planeValid=false;
        return whichPlane;
    }
}
