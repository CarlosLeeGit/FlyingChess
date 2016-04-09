package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class ChessBoard {//棋盘
    private Random dice;

    public void init(){//初始化棋盘
        dice = new Random(System.currentTimeMillis());
    }

    public int throwDice(){//扔骰子
        return dice.nextInt(5)+1;
    }
}
