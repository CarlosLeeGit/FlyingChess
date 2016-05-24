package com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer;



import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;

import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Game;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.dataPack.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by BingF on 2016/5/15.
 */
public class BroadcastSender implements Runnable {
    private DataPackUdpSocket sendSocket;
    private boolean isRunning = true;
    private int ip;
    private int mask;
    private String ipStr = null;
    private int port = 6667;
    private UDPServer parent = null;
    private DataPack dataPack = null;

    public BroadcastSender(UDPServer parent, final AppCompatActivity activity){
        this.parent = parent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                WifiManager wm = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
                ip=wm.getDhcpInfo().ipAddress;
                ipStr= InetAddress.getLocalHost().getHostAddress();
                mask=wm.getDhcpInfo().netmask;
                sendSocket = new DataPackUdpSocket(new DatagramSocket());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void run(){
        try{
            while(isRunning){
                if(this.dataPack == null)
                {
                    Game.delay(300);
                }
                for(int i=mask;i !=-1;i++) {
                    int targetIp=mask&ip+i-mask;
                    if(targetIp!=ip){
                        this.sendSocket.send(this.dataPack,InetAddress.getByName(int2ipString(targetIp)),port);
                    }
                }
                Thread.sleep(4000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        this.isRunning = false;
    }

    private String int2ipString(int ip){
        String str=new String();
        str+=String.valueOf(ip>>0xffffff);
        str+=".";
        str+=String.valueOf((ip>>0xffff)&0xff);
        str+=".";
        str+=String.valueOf((ip>>0xff)&0xff);
        str+=".";
        str+=String.valueOf((ip)&0xff);
        return str;
    }

    void roomChanged(Room room){
        List<String> msgList = new ArrayList<>();
        msgList.add(room.getId().toString());
        msgList.add(room.getName());
        msgList.add(String.valueOf(room.getAllPlayers().size()));
        msgList.add(room.isPlaying()==true?"1":"0");
        msgList.add(this.ipStr);
        msgList.add(String.valueOf(port));
        this.dataPack = new DataPack(DataPack.E_ROOM_BROADCAST, msgList);
    }

}
