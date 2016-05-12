package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by karthur on 2016/5/12.
 */
public class UpdateManager {
    public UpdateWorker uw;
    public UpdateManager(AppCompatActivity activity){
        uw=new UpdateWorker(activity);
    }
    public void checkUpdate(){
        uw.cancleUpdate();
        new Thread(uw).start();
    }
}

class UpdateWorker implements Runnable{
    private AppCompatActivity activity;
    private boolean waitForResult;
    private boolean update;
    private boolean check;
    NotificationManager nm;
    Notification notification;
    public UpdateWorker(AppCompatActivity activity){
        this.activity=activity;
        nm = (NotificationManager)activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notification =new NotificationCompat.Builder(activity.getApplicationContext()).setTicker("Download start in background").setSmallIcon(R.mipmap.ic_launcher).build();
        notification.contentView=new RemoteViews(activity.getPackageName(),R.layout.content_notification);
        notification.flags=Notification.FLAG_NO_CLEAR;
    }

    public void cancleUpdate(){
        waitForResult=false;
        update=false;
    }

    public void confirmUpdate(){
        waitForResult=false;
        update=true;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getByName("115.159.4.119"),7654);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            int versionCode;
            {//确定是否更新
                versionCode = dis.readInt();////
                PackageManager pm = activity.getApplicationContext().getPackageManager();
                PackageInfo pi = pm.getPackageInfo(activity.getPackageName(),0);
                if (pi.versionCode < versionCode) {//需要更新
                    activity.startActivity(new Intent(activity.getApplicationContext(), UpdateAct.class));
                }
                else{
                    dos.writeInt(0);
                    dos.close();
                    dis.close();
                    socket.close();
                    return;
                }
            }
            update=false;
            waitForResult=true;
            while(waitForResult){//等待确认
                Game.delay(600);
            }
            if(update){//确认升级
                dos.writeInt(1);
                //////////检查断点
                File verFile = new File(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess/version.dat");
                if(!verFile.exists()){
                    verFile.createNewFile();
                    FileOutputStream verfos = new FileOutputStream(verFile);
                    DataOutputStream verdos = new DataOutputStream(verfos);
                    verdos.writeInt(0);
                    verdos.close();
                    verfos.close();
                }
                FileInputStream verfis = new FileInputStream(verFile);
                DataInputStream verdis = new DataInputStream(verfis);
                int vc = verdis.readInt();
                verdis.close();
                verfis.close();
                ////对比版本
                long num=0;
                File apk = new File(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess/new.apk");
                if(!apk.exists()){//apk不存在
                    apk.createNewFile();
                }
                if(vc ==versionCode){
                    num=apk.length();
                }
                else{//清除以前版本
                    apk.createNewFile();
                }
                //更新版本号
                FileOutputStream verfos= new FileOutputStream(verFile);
                DataOutputStream verdos = new DataOutputStream(verfos);
                verdos.writeInt(versionCode);
                /////准备写入新的数据
                FileOutputStream fos = new FileOutputStream(apk);
                dos.writeLong(num);////
                Long length;
                int count;
                length=dis.readLong();////
                byte[] data = new byte[1024*64];
                notification.contentView.setTextViewText(R.id.textView10,String.format("progress: 0kb of %dkb",length/1024));
                notification.contentView.setProgressBar(R.id.progressBar2,100,0,false);
                nm.notify(0,notification);
                for(long i=num;i<length;){
                    count=is.read(data);////
                    fos.write(data,0,count);
                    i+=count;
                    notification.contentView.setTextViewText(R.id.textView10,String.format("progress: %dkb of %dkb  (%d%%)",i/1024,length/1024,i*100/length));
                    notification.contentView.setProgressBar(R.id.progressBar2,100,(int)(i*100/length),false);
                    nm.notify(0,notification);
                }
                nm.cancel(0);
                fos.close();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/ksymphony.com/FlyingChess/new.apk"),
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
            dos.writeInt(0);
            dis.close();
            dos.close();
            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}