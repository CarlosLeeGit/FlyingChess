package com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer;



import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Game;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.TCPServer.GameObjects.Room;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.DataPack;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Created by BingF on 2016/5/15.
 */
public class BroadcastSender implements Runnable {
    private DataPackUdpSocket sendSocket;
    private boolean isRunning = true;
    private String localIp = null;
    private int port = 6667;
    private UDPServer parent = null;
    private DataPack dataPack = null;
    private List<String> ipSection = null;

    public BroadcastSender(UDPServer parent, final AppCompatActivity activity){
        this.parent = parent;
        try{
            WifiManager wm = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
            localIp = getLocalHostIp();
            ipSection = getIpSection(localIp, wm.getDhcpInfo().netmask);
            sendSocket = new DataPackUdpSocket(new DatagramSocket());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getLocalHostIp()
    {
        String ipaddress = "";
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements())
            {
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                while (inet.hasMoreElements())
                {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()&&ip.getHostAddress().length()<=15)
                    {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        return ipaddress;
    }



    public void run(){
        try{
            while(isRunning){
                if(this.dataPack != null){
                    for(String ip : ipSection){
                        this.sendSocket.send(this.dataPack,InetAddress.getByName(ip),port);
                    }
                }
                Thread.sleep(3000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void stop(Room room){
        this.isRunning = false;
        try{
            if(room != null){
                List<String> msgList = new ArrayList<>();
                msgList.add(room.getId().toString());
                for(String ip : ipSection)
                    this.sendSocket.send(new DataPack(DataPack.E_ROOM_REMOVE_BROADCAST,msgList),InetAddress.getByName(ip),port);
            }
            sendSocket.close();
            sendSocket=null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    void onRoomChanged(Room room){
        List<String> msgList = new ArrayList<>();
        msgList.add(room.getId().toString());
        msgList.add(room.getName());
        msgList.add(String.valueOf(room.getAllPlayers().size()));
        msgList.add(room.isPlaying()==true?"1":"0");
        msgList.add(this.localIp);
        msgList.add(String.valueOf(port));
        this.dataPack = new DataPack(DataPack.E_ROOM_CREATE_BROADCAST, msgList);
    }


    /**
     * Converts an string formatted ip into int, note that this conversion simply
     * put the dotted format into its corresponding int format, without transforming
     * into network representation.
     * @param ip String formatted ip.
     * @return Int formatted ip.
     */
    public int stringToInt(String ip){
        ip.trim();

        String[] dots = ip.split("\\.");

        if(dots.length < 4)
            throw new IllegalArgumentException();

        return (Integer.valueOf(dots[0]) << 24) + (Integer.valueOf(dots[1]) << 16) + (Integer.valueOf(dots[2]) << 8) + Integer.valueOf(dots[3]);
    }

    /**
     * Converts an int formatted ip into string, note that this conversion simply
     * put the int format into its corresponding dotted ip format, without transforming
     * from network representation.
     * @param ip Int formatted ip.
     * @return String formatted ip.
     */
    public String intToString(int ip){
        StringBuilder sb = new StringBuilder();

        sb.append(String.valueOf((ip >>> 24)));
        sb.append(".");

        sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));
        sb.append(".");

        sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));
        sb.append(".");

        sb.append(String.valueOf((ip & 0x000000FF)));
        return sb.toString();
    }

    /**
     * Given the string formatted ip and network represented mask,
     * returns the ip sections (List of string formatted ip).
     * @param ip Any string formatted ip in the section.
     * @param mask Sub-net mask.
     * @return List of string formatted ip in the section without
     * broadcast addresses and itself.
     */
    public List<String> getIpSection(String ip, Integer mask){
        List<String> ipSection = new LinkedList<>();

        int orderedMask = ((mask & 0xFF000000) >>> 24) | ((mask & 0x00FF0000) >>> 8 ) | ((mask & 0x0000FF00) << 8) | ((mask & 0x000000FF) << 24);

        int startIp = stringToInt(ip) & orderedMask;
        for(int i = startIp; i < ((startIp) | (~orderedMask));i ++){

            String ipStr = intToString(i);
            if(ipStr.equals(ip) || ipStr.contains("255"))
                continue;

            ipSection.add(ipStr);
        }
        return ipSection;
    }

}
