package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class ChessBoard {
    private Random dice;

    public void init(){//call in game manager when a new game start
        dice = new Random(System.currentTimeMillis());
        GameManager.getDataManager().getAirplane(DataManager.COLOR_RED).init();
        GameManager.getDataManager().getAirplane(DataManager.COLOR_GREEN).init();
        GameManager.getDataManager().getAirplane(DataManager.COLOR_BLUE).init();
        GameManager.getDataManager().getAirplane(DataManager.COLOR_WHITE).init();
    }

    public byte throwDice(){//call in game manager when user throw a dice
        return (byte)(dice.nextInt(5)+1);
    }

    public boolean canIMove(byte color,byte dice){//test whether i can move a plane
        if(dice%2==0){//dice=2 4 6
            return true;
        }
        else{
            byte[] p = GameManager.getDataManager().getAirplane(color).position;
            for(int i=0;i<4;i++){
                if(p[i]>=0)//-1 means stop,-2 means over;
                    return true;
            }
        }
        return false;
    }

    public boolean move(byte color,int whichPlane,byte dice){//call in game manager when move plane
        return true;
    }

}
