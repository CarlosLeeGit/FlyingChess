package com.flashminds.flyingchess.LocalServer.TCPServer.GameObjects;


import com.flashminds.flyingchess.DataPack.DataPack;
import com.flashminds.flyingchess.DataPack.DataPackUtil;
import com.flashminds.flyingchess.LocalServer.TCPServer.TCPServer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ryan on 16/4/21.
 */
public class Room {
    private UUID id = null;
    private String name = null;
    private Player[] readyPlayers = null;
    private boolean isPlaying = false;
    private Map<Integer, Player> players = null;
    private TCPServer parent = null;

    public Room(String name, TCPServer parent) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.readyPlayers = new Player[4];
        this.isPlaying = false;
        this.players = new HashMap<>();
        this.parent = parent;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    /**
     * Add the player to the room.
     *
     * @param player The player object.
     */
    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
        player.setRoom(this);

        // notify other players
        broadcastToOthers(player, new DataPack(DataPack.E_ROOM_ENTER, DataPackUtil.getPlayerInfoMessage(player)));
        parent.onRoomChanged(this);
    }

    public Player getPlayer(int playerId) {
        for (Player player : players.values()) {
            if (player.getId() == playerId)
                return player;
        }
        return null;
    }

    public Collection<Player> getAllPlayers() {
        return Collections.unmodifiableCollection(this.players.values());
    }

    public void setHost(Player host) {
        if (this.players.containsValue(host))
            host.setHost(true);
    }

    public Player getHost() {
        for (Player player : players.values()) {
            if (player.isHost())
                return player;
        }
        return null;
    }

    public int getPlayerPosition(Player player) {
        for (int i = 0; i < 4; i++) {
            if (player.equals(readyPlayers[i]))
                return i;
        }
        return -1;
    }

    public boolean playerSelectPosition(Player player, int position) {
        if (position < -1 || position >= 4 || (!player.isRobot() && !this.players.containsKey(player.getId())))
            return false;

        // remove the player from the current position
        for (int i = 0; i < 4; i++) {
            if (player.equals(readyPlayers[i])) {
                readyPlayers[i] = null;
                if (player.isRobot()) {
                    player.setRoom(this);
                    players.remove(player.getId());
                    parent.onRoomChanged(this);
                }
                break;
            }
        }

        // if the player wants to pick another position
        if (position != -1) {
            if (readyPlayers[position] != null)
                return false;

            readyPlayers[position] = player;

            // robot is first created
            if (player.isRobot()) {
                player.setRoom(this);
                players.put(player.getId(), player);
                parent.onRoomChanged(this);
            }
        }

        // notify other players
        broadcastToOthers(player, new DataPack(DataPack.E_ROOM_POSITION_SELECT, true, DataPackUtil.getPlayerInfoMessage(player)));
        return true;
    }

    public void broadcastToAll(DataPack dataPack) {
        for (Player roomPlayer : players.values()) {
            if (!roomPlayer.isRobot()) {
                try {
                    roomPlayer.getSocket().send(dataPack);
                } catch (Exception e) {

                }
            }
        }
    }

    public void broadcastToOthers(Player broadcaster, DataPack dataPack) {
        if (players.containsValue(broadcaster)) {
            for (Player roomPlayer : players.values()) {
                if (!roomPlayer.equals(broadcaster) && !roomPlayer.isRobot()) {
                    try {
                        roomPlayer.getSocket().send(dataPack);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    public boolean containsPlayer(Player player) {
        return players.containsValue(player);
    }

    public void removePlayer(Player player) {
        // notify other players
        broadcastToOthers(player, new DataPack(DataPack.E_ROOM_EXIT, DataPackUtil.getPlayerInfoMessage(player)));

        // remove the player from ready players' array.
        for (int i = 0; i < 4; i++) {
            if (player.equals(readyPlayers[i]))
                readyPlayers[i] = null;
        }
        player.setRoom(null);
        this.players.remove(player.getId());
        parent.onRoomChanged(this);
    }

    public void startGame() {
        this.isPlaying = true;
        for (Player player : players.values())
            player.setStatus(Player.PLAYING);

        // send out game start signal to the players
        broadcastToAll(new DataPack(DataPack.E_GAME_START, true));
    }

    public void finishGame() {
        this.isPlaying = false;
        for (Player player : players.values()) {
            player.setStatus(Player.ROOM_WAITING);
        }
    }

    @Override
    public String toString() {
        return name + '(' + String.valueOf(id) + ')';
    }
}
