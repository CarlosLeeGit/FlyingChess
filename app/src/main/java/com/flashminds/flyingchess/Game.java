package com.flashminds.flyingchess;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.flashminds.flyingchess.LocalServer.LocalServer;
import com.flashminds.flyingchess.SoundManager.SoundManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by karthur on 2016/4/16.
 */
public class Game {
    public static GameManager gameManager;
    public static DataManager dataManager;
    public static ChessBoard chessBoard;
    public static SocketManager socketManager;
    public static HashMap<String, Role> playersData;
    public static ActivityManager activityManager;
    public static SoundManager soundManager;
    public static UpdateManager updateManager;
    public static LogManager logManager;
    public static ReplayManager replayManager;
    public static LocalServer localServer;
    public static Drawable d[];

    private static AppCompatActivity activity;
    private static RotateAnimationWorker rotateAnimationWorker;
    private static Typeface typeface;
    private static HashMap<Integer, Bitmap> bitmaps;

    public static void init(AppCompatActivity activity) {
        Game.activity = activity;
        dataManager = new DataManager();
        socketManager = new SocketManager(activity);
        gameManager = new GameManager();
        chessBoard = new ChessBoard();
        playersData = new HashMap<>();
        activityManager = new ActivityManager(activity);
        soundManager = new SoundManager(activity);
        updateManager = new UpdateManager(activity);
        logManager = new LogManager();
        replayManager = new ReplayManager();
        localServer = new LocalServer(activity);
        rotateAnimationWorker = new RotateAnimationWorker();
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/comici.ttf");
        bitmaps = new HashMap<>();
        initBitmap();
    }

    public static void delay(int interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(int id) {
        return bitmaps.get(id);
    }

    public static void initBitmap() {
        bitmaps.put(R.raw.choosemodebk, Game.loadBitmap(R.raw.choosemodebk));
        bitmaps.put(R.raw.cloud, Game.loadBitmap(R.raw.cloud));
        bitmaps.put(R.raw.map_min, Game.loadRectBitMap(R.raw.map_min));
        d = new Drawable[6];
        d[0] = activity.getResources().getDrawable(R.drawable.dices, null);
        d[1] = activity.getResources().getDrawable(R.drawable.dices2, null);
        d[2] = activity.getResources().getDrawable(R.drawable.dices3, null);
        d[3] = activity.getResources().getDrawable(R.drawable.dices4, null);
        d[4] = activity.getResources().getDrawable(R.drawable.dices5, null);
        d[5] = activity.getResources().getDrawable(R.drawable.dices6, null);
    }

    private static Bitmap loadBitmap(int id) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.outWidth = dm.widthPixels;
        opt.outHeight = dm.heightPixels;
        InputStream is = activity.getApplicationContext().getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Bitmap loadRectBitMap(int id) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.outWidth = dm.heightPixels;
        opt.outHeight = dm.heightPixels;
        InputStream is = activity.getApplicationContext().getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void startWaitAnimation(View view) {
        rotateAnimationWorker.setView(view);
        rotateAnimationWorker.stop();
        new Thread(rotateAnimationWorker).start();
    }

    public static void stopWaitAnimation() {
        rotateAnimationWorker.stop();
    }

    public static Typeface getFont() {
        return typeface;
    }

    public static void offlineTip() {
        activity.startActivity(new Intent(activity.getApplicationContext(), ChooseModeAct.class));
    }

}

class RotateAnimationWorker implements Runnable {
    private View view;
    private boolean run;

    public RotateAnimationWorker() {
        run = true;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void stop() {
        run = false;
    }

    @Override
    public void run() {
        run = true;
        while (run) {
            Game.delay(80);
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setRotation(view.getRotation() + 20);
                }
            });
        }
    }
}
