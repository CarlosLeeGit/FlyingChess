package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 背景音乐控制类
 * Created by BingF on 2016/4/26.
 */
public class SoundManager {


    public static final int ARRIVE=1;
    public static final int BACKGROUND =2 ;
    public static final int BUTTON = 3;
    public static final int DICE = 4;
    public static final int FLYCRASH=5;
    public static final int FLYLONG = 6;
    public static final int FLYSHORT = 7;
    public static final int FLYMID =8;
    public static final int FLYOUT = 9;
    public static final int WIN = 10;
    public static final int LOSE = 11;
    public static final int GAME=12;

    private MediaPlayer bk,game;
    private SoundPool soundPool;
    private Context context;

    private Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表

    public SoundManager(AppCompatActivity activity){
        this.context=activity.getApplicationContext();
        initSound();
        bk = MediaPlayer.create(context,R.raw.backgroundmusic);
        bk.setLooping(true);
        game=MediaPlayer.create(context,R.raw.gamemusic);
        game.setLooping(true);
    }

    //初始化音效播放器
    private void initSound()
    {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,100);

        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(ARRIVE, soundPool.load(context, R.raw.arrive, 1));
        soundMap.put(BUTTON, soundPool.load(context, R.raw.button, 1));
        soundMap.put(DICE, soundPool.load(context, R.raw.dice, 1));
        soundMap.put(FLYCRASH, soundPool.load(context, R.raw.flycrash, 1));
        soundMap.put(FLYLONG, soundPool.load(context, R.raw.flylong, 1));
        soundMap.put(FLYSHORT, soundPool.load(context, R.raw.flyshort, 1));
        soundMap.put(FLYMID, soundPool.load(context, R.raw.flymid, 1));
        soundMap.put(FLYOUT, soundPool.load(context, R.raw.flyout, 1));
        soundMap.put(WIN, soundPool.load(context, R.raw.win, 1));
        //soundMap.put(, soundPool.load(context, R.raw., 1));
    }

    public void playSound(int type)
    {
        soundPool.play(soundMap.get(type), 2, 2, 1, 0, 1);
    }

    public void playMusic(int type){
        switch (type){
            case BACKGROUND:
                if(!bk.isPlaying()){
                    bk.start();
                }
                if(game.isPlaying()){
                    game.pause();
                }
                break;
            case GAME:
                if(!game.isPlaying()) {
                    game.start();
                }
                if(bk.isPlaying()){
                    bk.pause();
                }
                break;
        }
    }

    public void pauseMusic() {
       /*if(bk.isPlaying())
            bk.pause();
        if(game.isPlaying())
            game.pause();*/
    }
}
