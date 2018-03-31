package com.flashminds.flyingchess.entity;

import java.util.Random;

/**
 * Created by IACJ on 2018/4/1.
 */
public class Dice {
    Random r;

    public Dice() {
        r = new Random(System.currentTimeMillis());
    }

    public int roll() {
        return (r.nextInt(6) + 1);
    }
}
