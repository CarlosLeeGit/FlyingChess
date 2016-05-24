package com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer;




import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Player;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.dataPack.DataPack;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.dataPack.DataPackUtil;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ryan on 16/4/12.
 */
public class DataPackSocketRunnable implements Runnable {
    private Player selfPlayer = null;
    private DataPackTcpSocket socket = null;
    private TCPServer parent = null;
    
    public DataPackSocketRunnable(DataPackTcpSocket socket, TCPServer server) throws IOException{
        this.selfPlayer = null;
        this.socket = socket;
        this.parent = server;
    }

    public void run(){
        try{


            // enter process loop
            while(true){
                processDataPack(socket.receive());
            }

        } catch(Exception e){

        }
        finally {
            /**
             *  when someone disconnected
             */
            parent.getSelfRoom().removePlayer(selfPlayer);
            try{
                selfPlayer.getSocket().close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * Process the incoming data packs.
     *
     * @param dataPack The data pack to be processed.
     */
    private void processDataPack(DataPack dataPack) throws SocketException, IOException{
        try{
            switch(dataPack.getCommand()){
                case DataPack.INVALID:{
                    return;
                }
                case DataPack.R_LOGIN:{
                    Player player = Player.createPlayer(dataPack.getMessage(0));
                    // set the host if the player is the owner
                    if(socket.getInetSocketAddress().getAddress().isLoopbackAddress())
                        player.setHost(true);
                        
                    parent.getSelfRoom().addPlayer(player);
                    player.setSocket(socket);
                    this.selfPlayer = player;

                    List<String> msgList = new ArrayList<>();
                    msgList.add(String.valueOf(player.getId()));
                    msgList.addAll(DataPackUtil.getRoomPlayerInfoMessage(parent.getSelfRoom()));
                    socket.send(new DataPack(DataPack.A_ROOM_ENTER, true, msgList));
                    return;
                }
                case DataPack.R_LOGOUT:{
                    Player player = Player.createPlayer(dataPack.getMessage(0));
                    if(player != null){
                        parent.getSelfRoom().removePlayer(player);
                    }
                    return;
                }
                case DataPack.R_ROOM_POSITION_SELECT:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();
                    int position = Integer.valueOf(dataPack.getMessage(4));

                    boolean isSuccessful = room.playerSelectPosition(player, position);

                    if(isSuccessful)
                        socket.send(new DataPack(DataPack.E_ROOM_POSITION_SELECT, true, DataPackUtil.getPlayerInfoMessage(player)));
                    else
                        socket.send(new DataPack(DataPack.E_ROOM_POSITION_SELECT, false));

                    return;
                }
                case DataPack.R_ROOM_EXIT:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();
                    room.removePlayer(player);
                    return;
                }
                case DataPack.R_GAME_START:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();
                    if(player.isHost() && room.containsPlayer(player)){
                        room.startGame();
                    }
                    return;
                }
                // the following 2 commands' logic is basically the same(simply forward the datapack)
                case DataPack.R_GAME_FINISHED:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();
                    room.finishGame();
                    for(Player roomPlayer : room.getPlayers()){
                        if(!roomPlayer.isRobot()){
                            if(roomPlayer.equals(player)){
                                player.setPoints(player.getPoints() + 10);
                            }
                            else{
                                roomPlayer.setPoints(roomPlayer.getPoints() - 5);
                            }
                        }
                    }
                    room.broadcastToOthers(selfPlayer, new DataPack(DataPack.E_GAME_FINISHED, DataPackUtil.getRoomPlayerInfoMessage(room)));
                    return;
                }
                case DataPack.R_GAME_PROCEED_DICE:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();
                    // set the command
                    dataPack.setCommand(DataPack.E_GAME_PROCEED_DICE);
                    // update datapack's time
                    dataPack.setDate(new Date());

                    // simply forward the datapack to the users in the same room
                    room.broadcastToOthers(selfPlayer, dataPack);
                    return;
                }
                case DataPack.R_GAME_PROCEED_PLANE:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();

                    // set the command
                    dataPack.setCommand(DataPack.E_GAME_PROCEED_PLANE);
                    // update datapack's time
                    dataPack.setDate(new Date());

                    // simply forward the datapack to the users in the same room
                    room.broadcastToOthers(selfPlayer, dataPack);
                    return;
                }
                case DataPack.R_GAME_EXIT:{
                    Player player = parent.getSelfRoom().getPlayer(Integer.valueOf(dataPack.getMessage(0)));
                    Room room = parent.getSelfRoom();

                    // generate datapack
                    List<String> msgList = new ArrayList<>();
                    msgList.add(String.valueOf(player.getId()));
                    if(player.isHost()){
                        msgList.add("1");
                    }
                    else{
                        msgList.add("0");
                    }
                    dataPack.setCommand(DataPack.E_GAME_PLAYER_DISCONNECTED);
                    dataPack.setDate(new Date());
                    dataPack.setMessageList(msgList);

                    // broadcast disconnected info
                    room.broadcastToOthers(selfPlayer, dataPack);

                    socket.send(new DataPack(DataPack.A_GAME_EXIT, true));
                    return;
                }
                default:
                    return;
            }
        } catch(IndexOutOfBoundsException e){
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;

        if(!(obj instanceof DataPackSocketRunnable))
            return false;

        DataPackSocketRunnable socketRunnable = (DataPackSocketRunnable) obj;

        if(socketRunnable.socket == null)
            return false;

        if(socketRunnable.socket.getInetSocketAddress().equals(this.socket.getInetSocketAddress())){
            return true;
        }

        return false;
    }
}
