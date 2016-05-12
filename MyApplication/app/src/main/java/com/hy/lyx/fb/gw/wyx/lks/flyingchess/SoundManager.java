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

    public static final int BACKGROUND=-1;
    public static final int GAME=-2;

    public static final int BOOM=1;
    public static final int MSG=2;
    public static final int BUMP=3;
    public static final int ARRIVE=4;
    public static final int WIN=5;
    public static final int FLYSHORT=6;
    public static final int FLYLONG=7;
    public static final int ROLL=8;
    public static final int BACK=9;
    public static final int BUTTON=10;

    private MediaPlayer bk,game;
    private SoundPool soundPool;
    private Context context;

    private Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表

    public SoundManager(AppCompatActivity activity){
        this.context=activity.getApplicationContext();
        initSound();
        bk = MediaPlayer.create(context,R.raw.bg);
        bk.setLooping(true);
        game=MediaPlayer.create(context,R.raw.bg2);
        game.setLooping(true);
    }

    //初始化音效播放器
    private void initSound()
    {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,100);

        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(BUTTON, soundPool.load(context, R.raw.itemboom, 1));
        soundMap.put(BOOM, soundPool.load(context, R.raw.sel, 1));
        soundMap.put(ARRIVE, soundPool.load(context, R.raw.arrive, 1));
        soundMap.put(BACK, soundPool.load(context, R.raw.click, 1));
        soundMap.put(ROLL, soundPool.load(context, R.raw.dice, 1));
        soundMap.put(FLYLONG, soundPool.load(context, R.raw.fly, 1));
        soundMap.put(MSG, soundPool.load(context, R.raw.message, 1));
        soundMap.put(FLYSHORT, soundPool.load(context, R.raw.onestep, 1));
        soundMap.put(WIN, soundPool.load(context, R.raw.win1, 1));
    }

    public void playSound(int type)
    {
        soundPool.play(soundMap.get(type), 1, 1, 1, 0, 1);
    }

    public void playMusic(int type){
        switch (type){
            case BACKGROUND:
                if(!bk.isPlaying()){
                    bk.start();
                }
                if(game.isPlaying()){
                    game.stop();
                }
                break;
            case GAME:
                if(!game.isPlaying()) {
                    game.start();
                }
                if(bk.isPlaying()){
                    bk.stop();
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
