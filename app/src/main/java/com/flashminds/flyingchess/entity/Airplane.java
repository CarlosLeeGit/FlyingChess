package com.flashminds.flyingchess.entity;

/**
 * Created by IACJ on 2018/4/1.
 */
public class Airplane {
    public int[] position;
    public int[] lastPosition;

    public Airplane() {
        position = new int[4];
        lastPosition = new int[4];
        init();
    }

    public void init() {
        lastPosition[0] = -1;
        lastPosition[1] = -1;
        lastPosition[2] = -1;
        lastPosition[3] = -1;
        position[0] = -1;
        position[1] = -1;
        position[2] = -1;
        position[3] = -1;
    }
}
