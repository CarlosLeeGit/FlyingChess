package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class ChessBoard {//chess board data
    public static final int COLOR_RED=0,COLOR_GREEN=1,COLOR_BLUE=2,COLOR_WHITE=3;//玩家颜色

    private Dice dice;
    private Airplane[] airplanes;

    public void init(){//call in game manager when a new game start
        dice = new Dice();
        airplanes=new Airplane[4];
        airplanes[0]=new Airplane();
        airplanes[0].init();
        airplanes[1]=new Airplane();
        airplanes[1].init();
        airplanes[2]=new Airplane();
        airplanes[2].init();
        airplanes[3]=new Airplane();
        airplanes[3].init();

    }

    public Dice getDice(){//call in game manager when user throw a dice
        return dice;
    }

    public Airplane getAirplane(int color){
        return airplanes[color];
    }

}

class Airplane{
    int[] position;
    Airplane(){
        position=new int[4];
        init();
    }
    public void init(){
        position[0]=-1;
        position[1]=-1;
        position[2]=-1;
        position[3]=-1;
    }
}

class Dice{
    Random r;
    public Dice(){
        r=new Random(System.currentTimeMillis());
    }
    public int roll(){
        return (r.nextInt(6)+1);
    }
}