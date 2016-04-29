package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

/**
 * Created by karthur on 2016/4/16.
 */
public class Player {//user
    public String id;
    public String name;
    public String score;
    public int color;// -1 空闲  -2 无效

    private static boolean diceValid=false,planeValid=false,canRoll=false,canChoosePlane=false;
    private static int dice,whichPlane;

    public Player(String id,String name,String score,int color){
        this.id=id;
        this.name=name;
        this.score=score;
        this.color=color;
    }

    public static boolean canIMove(int color,int dice){//test whether i can move a plane
        if(dice%2==0){//dice=2 4 6
            return true;
        }
        else{
            int[] p = Game.chessBoard.getAirplane(color).position;
            for(int i=0;i<4;i++){
                if(p[i]>=0)//-1 means stop,-2 means over;
                    return true;
            }
        }
        return false;
    }

    public static boolean move(int color,int whichPlane,int dice){
        if((Game.chessBoard.getAirplane(color).position[whichPlane]==-1&&dice%2!=0)||Game.chessBoard.getAirplane(color).position[whichPlane]==-2)
            return false;
        if(Game.chessBoard.getAirplane(color).position[whichPlane]==-1){
            Game.chessBoard.getAirplane(color).position[whichPlane]=0;
            return true;
        }
        int nextStep=Game.chessBoard.getAirplane(color).position[whichPlane]+dice;
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
        Game.chessBoard.getAirplane(color).position[whichPlane]=nextStep;
        return true;
    }

    public static void setDiceValid(){
        if(canRoll){
            dice=Game.chessBoard.getDice().roll();
            diceValid=true;
        }
    }

    public static void setPlaneValid(int _whichPlane){
        if(canChoosePlane){
            whichPlane=_whichPlane;
            planeValid=true;
        }
    }

    public static int roll(){
        canRoll=true;
        while(!diceValid) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        canRoll=false;
        diceValid=false;
        return dice;
    }

    public static int choosePlane(){
        canChoosePlane=true;
        while(!planeValid){
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        canChoosePlane=false;
        planeValid=false;
        return whichPlane;
    }
}
