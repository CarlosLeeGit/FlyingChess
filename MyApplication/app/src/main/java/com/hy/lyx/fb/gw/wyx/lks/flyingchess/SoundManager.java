package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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

    private MediaPlayer mediaPlayer,bk,game;
    private AssetManager assetManager;
    private LinkedList<MediaPlayer> mediaPlayers;
    private Map<Integer,String> soundMap;

    public SoundManager(AppCompatActivity activity){
        bk = new MediaPlayer();
        game = new MediaPlayer();
        assetManager = activity.getAssets();
        mediaPlayers=new LinkedList<>();
        initMap();
    }

    private void initMap(){
        soundMap = new HashMap<>();
        soundMap.put(ARRIVE,"music/arrive.ogg");
        soundMap.put(BACKGROUND,"music/backgroundmusic.mp3");
        soundMap.put(BUTTON,"music/button.ogg");
        soundMap.put(DICE,"music/dice.ogg");
        soundMap.put(FLYCRASH,"music/flycrash.ogg");
        soundMap.put(FLYLONG,"music/flylong.ogg");
        soundMap.put(FLYSHORT,"music/flyshort.ogg");
        soundMap.put(FLYMID,"music/flymid.ogg");
        soundMap.put(WIN,"music/win.ogg");
        soundMap.put(LOSE,"music/lose.ogg");
        soundMap.put(GAME,"music/gamemusic.mp3");
    }

    public void playSound(int type)
    {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayers.addLast(mediaPlayer);
            AssetFileDescriptor assetFileDescriptor =  assetManager.openFd(soundMap.get(type));
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
            mediaPlayer.start();
            if(mediaPlayers.size()>3){
                mediaPlayers.getFirst().release();
                mediaPlayers.removeFirst();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMusic(int type){
        switch (type){
            case BACKGROUND:
                if(!bk.isPlaying()){
                    try{
                        bk.reset();
                        AssetFileDescriptor assetFileDescriptor =  assetManager.openFd(soundMap.get(type));
                        bk.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                        bk.setLooping(true);
                        bk.prepare();
                        bk.start();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if(game.isPlaying()){
                    game.stop();
                }
                break;
            case GAME:
                if(!game.isPlaying()) {
                    try{
                        game.reset();
                        AssetFileDescriptor assetFileDescriptor =  assetManager.openFd(soundMap.get(type));
                        game.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                        game.setLooping(true);
                        game.prepare();
                        game.start();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if(bk.isPlaying()){
                    bk.stop();
                }
                break;
        }
    }

    public void pauseMusic() {
        if(Game.activityManager.isSuspend()){
            if(bk.isPlaying())
                bk.pause();
            if(game.isPlaying())
                game.pause();
        }
    }

    public void resumeMusic(int type){
        if(type==GAME&&!game.isPlaying()){
            game.start();
        }
        else if(type==BACKGROUND&&!bk.isPlaying()){
            bk.start();
        }
    }
}
