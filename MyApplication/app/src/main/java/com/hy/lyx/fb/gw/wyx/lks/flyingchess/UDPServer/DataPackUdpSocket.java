package com.hy.lyx.fb.gw.wyx.lks.flyingchess.UDPServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hy.lyx.fb.gw.wyx.lks.flyingchess.DataPack;


import java.io.IOException;
import java.net.*;

/**
 * Created by Ryan on 16/5/15.
 */
public class DataPackUdpSocket {
    protected DatagramSocket socket = null;
    protected Gson dataPackGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    protected byte[] inBuf = null;

    public DataPackUdpSocket(DatagramSocket socket){
        this.socket = socket;
        inBuf = new byte[1024];
    }
    /**
     * This method sends out the datapack immediately, in the thread
     * which calls the method.
     *
     * @param dataPack The datapack to be sent.
     * @param ip The ip to which the datapack is sent.
     */
    public void send(DataPack dataPack, InetAddress ip, int port) throws IOException {
        byte[] bytes = dataPackGson.toJson(dataPack, DataPack.class).getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ip, port);
        socket.send(packet);
    }

    /**
     * Receive one data pack from the inputstream, which
     * will be blocking until one data pack is successfully read.
     *
     * @return The data pack read.
     */
    public DataPack receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(inBuf, inBuf.length);
        socket.receive(packet);
        System.out.println(new String(packet.getData()));
        return dataPackGson.fromJson(new String(packet.getData()).trim(), DataPack.class);
    }

    /**
     * Close the socket.
     */
    public void close() throws IOException {
        this.socket.close();
    }

    public InetSocketAddress getInetSocketAddress() {
        InetAddress ip = null;
        ip = this.socket.getInetAddress();
        int port = this.socket.getPort();
        InetSocketAddress address = new InetSocketAddress(ip,port);
        return address;
    }
}
