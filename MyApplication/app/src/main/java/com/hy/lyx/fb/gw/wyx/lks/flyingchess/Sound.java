package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 背景音乐控制类
 * Created by BingF on 2016/4/26.
 */
public class Sound {

    private MediaPlayer music;
    private SoundPool soundPool;

    private boolean musicSt = true; //音乐开关
    private boolean soundSt = true; //音效开关
    private Context context;

    private Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表

    /**
     * 初始化方法
     * @param c
     */
    public void init(Context c)
    {
        context = c;
        initSound();
    }

    //初始化音效播放器
    private void initSound()
    {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,100);

        soundMap = new HashMap<Integer,Integer>();
        soundMap.put(R.raw.itemboom, soundPool.load(context, R.raw.itemboom, 1));
        soundMap.put(R.raw.sel, soundPool.load(context, R.raw.sel, 1));
        soundMap.put(R.raw.arrive, soundPool.load(context, R.raw.arrive, 1));
        soundMap.put(R.raw.click, soundPool.load(context, R.raw.click, 1));
        soundMap.put(R.raw.dice, soundPool.load(context, R.raw.dice, 1));
        soundMap.put(R.raw.fly, soundPool.load(context, R.raw.fly, 1));
        soundMap.put(R.raw.fly1, soundPool.load(context, R.raw.fly1, 1));
        soundMap.put(R.raw.message, soundPool.load(context, R.raw.message, 1));
        soundMap.put(R.raw.onestep, soundPool.load(context, R.raw.onestep, 1));
        soundMap.put(R.raw.win1, soundPool.load(context, R.raw.win1, 1));
        soundMap.put(R.raw.win2, soundPool.load(context, R.raw.win2, 1));
    }

    /**
     * 播放音效
     * @param resId 音效资源id
     */
    private void playSound(int resId)
    {
        if(soundSt == false)
            return;

        Integer soundId = soundMap.get(resId);
        if(soundId != null)
            soundPool.play(soundId, 1, 1, 1, 0, 1);
    }

    /**
     * 暂停音乐
     */
    public void pauseMusic()
    {
        if(music.isPlaying())
            music.pause();
    }

    /**
     * 播放音乐
     */
    public void startMusic()
    {
        if(musicSt){
            music = MediaPlayer.create(context,R.raw.bg);
            music.setLooping(true);
            music.start();
        }
    }

    /**
     * 播放游戏时的背景音乐
     */
    public void startGamingMusic(){
        if(musicSt) {
            music = MediaPlayer.create(context,R.raw.bg2);
            music.setLooping(true);
            music.start();
        }
    }


    /**
     * 获得音乐开关状态
     * @return
     */
    public boolean isMusicSt() {
        return musicSt;
    }

    /**
     * 设置音乐开关
     * @param musicSt
     */
    public void setMusicSt(boolean musicSt) {
        this.musicSt = musicSt;
    }

    /**
     * 发出‘邦’的声音
     */
    public void boom()
    {
        playSound(R.raw.itemboom);
    }

    /**
     * 弹出消息的声音
     */
    public void message(){
        playSound(R.raw.message);
    }

    /**
     * 飞机碰撞的声音
     */
    public void bump(){
        playSound(R.raw.itemboom);
    }

    /**
     * 飞机到终点的声音
     */
    public void arrive(){
        playSound(R.raw.arrive);
    }

    /**
     * 胜利的声音
     */
    public void win2(){
        playSound(R.raw.win2);
    }
    public void win1() { playSound(R.raw.win1); }

    /**
     * 飞机飞行的声音
     */
    public void fly_oneStep(){
        playSound(R.raw.onestep);
    }

    public void fly(){playSound(R.raw.fly);}

    /**
     * 掷骰子的声音
     */
    public void roll(){
        playSound(R.raw.dice);
    }

    /**
     * 返回按钮的声音
     */
    public void returnButton(){
        playSound(R.raw.click);
    }

    /**
     * 普通按钮的声音
     */
    public void button(){
        playSound(R.raw.itemboom);
    }


}
