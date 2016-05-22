package com.hy.lyx.fb.gw.wyx.lks.flyingchess;



import android.os.Environment;

import com.google.gson.*;

import java.io.*;

/**
 * Created by Great on 2016/5/12.
 */
public class ReplayManager {
    public boolean isReplay;
    File file;
    BufferedWriter writer;
    BufferedReader reader;
    Gson gson;

    public ReplayManager() {
        file = new File(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess/record.dat");
        isReplay = false;
        gson = new Gson();
    }

    public void startRecord() {
        if(isReplay == false) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean startReplay() {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            isReplay = false;
        }
        isReplay = true;
        return isReplay;
    }

    public void savePlayerNum(int num) {
        if(isReplay == false) {
            try {
                writer.write(num + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveRoleKey(String key) {
        if(isReplay == false) {
            try {
                writer.write(key + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveRoleInfo(Role role) {
        if(isReplay == false) {
            try {
                writer.write(gson.toJson(role) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDice(int dice) {
        if(isReplay == false) {
            try {
                writer.write(dice + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveWhichPlane(int whichPlane) {
        if(isReplay == false) {
            try {
                writer.write(whichPlane + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveWinnerInfo(String id, String name, int score) {
        if(isReplay == false) {
            try {
                writer.write(id + "\n" + name + "\n" + score + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getWinnerId() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {

        }
        return temp;
    }

    public String getWinnerName() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {

        }
        return temp;
    }

    public String getWinnerScore() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {

        }
        return temp;
    }

    public int getPlayerNum() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {

        }
        return Integer.parseInt(temp);
    }

    public String getSavedKey() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {

        }
        return temp;
    }

    public Role getSavedRole() {
        String temp;
        Role role = null;
        try {
            if((temp = reader.readLine()) != null) {
                role = gson.fromJson(temp, Role.class);
            }
        } catch (Exception e) {

        }
        return role;
    }

    public int getSavedDice() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {

        }
        return Integer.parseInt(temp);
    }

    public int getSavedWhichPlane() {
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(temp);
    }

    public void stopReplay() {
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isReplay = false;
        }
    }

    public void closeRecord() {
        if(isReplay == false) {
            try {
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearRecord() {
        file.delete();
    }
}


