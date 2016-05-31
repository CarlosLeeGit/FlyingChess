package com.flashminds.flyingchess;

import java.util.Random;

/**
 * Created by karthur on 2016/4/9.
 */
public class ChessBoard {//chess board data
    public static final int COLOR_RED = 0, COLOR_GREEN = 1, COLOR_BLUE = 2, COLOR_YELLOW = 3;//玩家颜色
    public static final int COLOR_Z = -1, COLOR_X = -2;
    private boolean overflow;
    private Dice dice;
    private Airplane[] airplanes;
    public int[][][] map = {
            {{5, 18}, {6, 16}, {5, 15}, {5, 14}, {6, 13}, {5, 12}, {4, 13}, {3, 13}, {2, 12}, {1, 11}, {1, 10}, {1, 9}, {1, 8}, {1, 7}, {2, 6}, {3, 5}, {4, 5}, {5, 6}, {6, 5}, {5, 4}, {5, 3}, {6, 2}, {7, 1}, {8, 1}, {9, 1}, {10, 1}, {11, 1}, {12, 2}, {13, 3}, {13, 4}, {12, 5}, {13, 6}, {14, 5}, {15, 5}, {16, 6}, {17, 7}, {17, 8}, {17, 9}, {17, 10}, {17, 11}, {16, 12}, {15, 13}, {14, 13}, {13, 12}, {12, 13}, {13, 14}, {13, 15}, {12, 16}, {11, 17}, {10, 17}, {9, 16}, {9, 15}, {9, 14}, {9, 13}, {9, 12}, {9, 11}, {9, 10}},
            {{18, 13}, {16, 12}, {15, 13}, {14, 13}, {13, 12}, {12, 13}, {13, 14}, {13, 15}, {12, 16}, {11, 17}, {10, 17}, {9, 17}, {8, 17}, {7, 17}, {6, 16}, {5, 15}, {5, 14}, {6, 13}, {5, 12}, {4, 13}, {3, 13}, {2, 12}, {1, 11}, {1, 10}, {1, 9}, {1, 8}, {1, 7}, {2, 6}, {3, 5}, {4, 5}, {5, 6}, {6, 5}, {5, 4}, {5, 3}, {6, 2}, {7, 1}, {8, 1}, {9, 1}, {10, 1}, {11, 1}, {12, 2}, {13, 3}, {13, 4}, {12, 5}, {13, 6}, {14, 5}, {15, 5}, {16, 6}, {17, 7}, {17, 8}, {16, 9}, {15, 9}, {14, 9}, {13, 9}, {12, 9}, {11, 9}, {10, 9}},
            {{13, 0}, {12, 2}, {13, 3}, {13, 4}, {12, 5}, {13, 6}, {14, 5}, {15, 5}, {16, 6}, {17, 7}, {17, 8}, {17, 9}, {17, 10}, {17, 11}, {16, 12}, {15, 13}, {14, 13}, {13, 12}, {12, 13}, {13, 14}, {13, 15}, {12, 16}, {11, 17}, {10, 17}, {9, 17}, {8, 17}, {7, 17}, {6, 16}, {5, 15}, {5, 14}, {6, 13}, {5, 12}, {4, 13}, {3, 13}, {2, 12}, {1, 11}, {1, 10}, {1, 9}, {1, 8}, {1, 7}, {2, 6}, {3, 5}, {4, 5}, {5, 6}, {6, 5}, {5, 4}, {5, 3}, {6, 2}, {7, 1}, {8, 1}, {9, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, {9, 8}},
            {{0, 5}, {2, 6}, {3, 5}, {4, 5}, {5, 6}, {6, 5}, {5, 4}, {5, 3}, {6, 2}, {7, 1}, {8, 1}, {9, 1}, {10, 1}, {11, 1}, {12, 2}, {13, 3}, {13, 4}, {12, 5}, {13, 6}, {14, 5}, {15, 5}, {16, 6}, {17, 7}, {17, 8}, {17, 9}, {17, 10}, {17, 11}, {16, 12}, {15, 13}, {14, 13}, {13, 12}, {12, 13}, {13, 14}, {13, 15}, {12, 16}, {11, 17}, {10, 17}, {9, 17}, {8, 17}, {7, 17}, {6, 16}, {5, 15}, {5, 14}, {6, 13}, {5, 12}, {4, 13}, {3, 13}, {2, 12}, {1, 11}, {1, 10}, {2, 9}, {3, 9}, {4, 9}, {5, 9}, {6, 9}, {7, 9}, {8, 9}}
    };
    public int[][][] mapStart = {
            {{1, 15}, {3, 15}, {1, 17}, {3, 17}},
            {{15, 15}, {17, 15}, {15, 17}, {17, 17}},
            {{15, 1}, {17, 1}, {15, 3}, {17, 3}},
            {{1, 1}, {3, 1}, {1, 3}, {3, 3}}
    };

    public void init() {//call in game manager when a new game start
        dice = new Dice();
        airplanes = new Airplane[4];
        airplanes[0] = new Airplane();
        airplanes[0].init();
        airplanes[1] = new Airplane();
        airplanes[1].init();
        airplanes[2] = new Airplane();
        airplanes[2].init();
        airplanes[3] = new Airplane();
        airplanes[3].init();
    }

    public Dice getDice() {//call in game manager when user throw a dice
        return dice;
    }

    public Airplane getAirplane(int color) {
        return airplanes[color];
    }

    public boolean isOverflow() {
        return overflow;
    }

    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }

}

class Airplane {
    int[] position;
    int[] lastPosition;

    Airplane() {
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

class Dice {
    Random r;

    public Dice() {
        r = new Random(System.currentTimeMillis());
    }

    public int roll() {
        return (r.nextInt(6) + 1);
    }
}