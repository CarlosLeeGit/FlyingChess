package com.flashminds.flyingchess;

import com.flashminds.flyingchess.DataPack.DataPack;

import java.util.HashMap;

/**
 * Created by karthur on 2016/4/24.
 */
public class MsgHandler {
    HashMap<Integer, Target> targets;

    public MsgHandler() {
        targets = new HashMap<>();
    }

    public void registerActivity(int datapack_commond, Target target) {
        targets.put(datapack_commond, target);
    }

    protected void processDataPack(DataPack dataPack) {
        if (targets.containsKey(dataPack.getCommand())) {
            targets.get(dataPack.getCommand()).processDataPack(dataPack);
        }
    }
}

