package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.hy.lyx.fb.gw.wyx.lks.flyingchess.Server.LocalServer;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by karthur on 2016/4/16.
 */
public class Game{
    public static GameManager gameManager;
    public static DataManager dataManager;
    public static ChessBoard chessBoard;
    public static SocketManager socketManager;
    public static HashMap<String,Role> playersData;
    public static ActivityManager activityManager;
    public static SoundManager soundManager;
    public static UpdateManager updateManager;
    public static LogManager logManager;
    public static ReplayManager replayManager;
    public static LocalServer localServer;

    private static AppCompatActivity activity;
    private static RotateAnimationWorker rotateAnimationWorker;
    private static Typeface typeface;

    public static void init(AppCompatActivity activity){
        Game.activity=activity;
        dataManager=new DataManager();
        socketManager = new SocketManager(activity);
        gameManager = new GameManager();
        chessBoard=new ChessBoard();
        playersData =new HashMap<>();
        activityManager=new ActivityManager(activity);
        soundManager = new SoundManager(activity);
        updateManager = new UpdateManager(activity);
        logManager = new LogManager();
        replayManager = new ReplayManager();
        localServer = new LocalServer(activity);
        rotateAnimationWorker = new RotateAnimationWorker();
        typeface = Typeface.createFromAsset(activity.getAssets(),"fonts/comici.ttf");
    }

    public static void delay(int interval){
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmap(int id){
        DisplayMetrics dm=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        BitmapFactory.Options opt= new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.outWidth=dm.widthPixels;
        opt.outHeight=dm.heightPixels;
        InputStream is = activity.getApplicationContext().getResources().openRawResource(id);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    public static void startWaitAnimation(View view){
        rotateAnimationWorker.setView(view);
        rotateAnimationWorker.stop();
        new Thread(rotateAnimationWorker).start();
    }

    public static void stopWaitAnimation(){
        rotateAnimationWorker.stop();
    }

    public static Typeface getFont(){
        return typeface;
    }

}

class RotateAnimationWorker implements Runnable{
    private View view;
    private boolean run;
    public RotateAnimationWorker(){
        run=true;
    }
    public void setView(View view){
        this.view=view;
    }
    public void stop(){
        run=false;
    }
    @Override
    public void run() {
        run=true;
        while(run){
            Game.delay(80);
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setRotation(view.getRotation()+20);
                }
            });
        }
    }
}
