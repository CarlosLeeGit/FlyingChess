package com.flashminds.flyingchess.dataPack;


import com.flashminds.flyingchess.localServer.TCPServer.GameObjects.Player;
import com.flashminds.flyingchess.localServer.TCPServer.GameObjects.Room;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ryan on 16/5/2.
 */
public class DataPackUtil {
    public static List<String> getRoomPlayerInfoMessage(Room room) {
        List<String> msgList = new LinkedList<>();
        List<String> otherPlayerMsgList = new LinkedList<>();
        for (Player player : room.getPlayers()) {
            // put the host player in the front
            if (player.isHost())
                msgList.addAll(getPlayerInfoMessage(player));
            else
                otherPlayerMsgList.addAll(getPlayerInfoMessage(player));
        }
        msgList.addAll(otherPlayerMsgList);
        return msgList;
    }

    public static List<String> getPlayerInfoMessage(Player player) {
        List<String> msgList = new LinkedList<>();
        msgList.add(String.valueOf(player.getId()));
        msgList.add(player.getName());
        msgList.add(String.valueOf(player.getPoints()));
        msgList.add(String.valueOf(player.getRoom().getPlayerPosition(player)));
        return msgList;
    }

    public static List<String> getRoomInfoMessage(Room room) {
        List<String> msgList = new LinkedList<>();
        // add room id
        msgList.add(String.valueOf(room.getId()));
        // add room name
        msgList.add(room.getName());
//        msgList.add(String.valueOf(room.getPlayers().size()));
        return msgList;
    }
}
