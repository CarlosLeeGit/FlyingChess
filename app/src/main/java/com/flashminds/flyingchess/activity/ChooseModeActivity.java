package com.flashminds.flyingchess.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.flashminds.flyingchess.manager.DataManager;
import com.flashminds.flyingchess.dataPack.DataPack;
import com.flashminds.flyingchess.entity.Game;
import com.flashminds.flyingchess.R;
import com.flashminds.flyingchess.manager.SoundManager;
import com.flashminds.flyingchess.dataPack.Target;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ChooseModeActivity extends AppCompatActivity implements Target {
    Button local, lan, wlan;
    boolean exit;
    Timer closeTimer;
    ImageView bk, bk2;
    ImageView waitImage;
    Button waitBackground;
    Button records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Game.soundManager.playMusic(SoundManager.BACKGROUND);
        //init
        local = (Button) findViewById(R.id.button2);
        lan = (Button) findViewById(R.id.button3);
        wlan = (Button) findViewById(R.id.button4);
        exit = false;
        closeTimer = new Timer();
        exit = false;
        bk = (ImageView) findViewById(R.id.backgroud);
        bk2 = (ImageView) findViewById(R.id.backgroud2);
        waitImage = (ImageView) findViewById(R.id.wait);
        waitBackground = (Button) findViewById(R.id.waitbackground);
        records = (Button) findViewById(R.id.records);
        //trigger
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//choose local game
                Game.soundManager.playSound(SoundManager.BUTTON);
                Game.dataManager.setGameMode(DataManager.GM_LOCAL);//set game mode

                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                ArrayList<String> msgs = new ArrayList<>();
                Game.dataManager.setMyId("0");
                msgs.add("0");
                msgs.add("ME");
                msgs.add(String.valueOf(Game.dataManager.getScore()));
                msgs.add("-1");
                intent.putStringArrayListExtra("msgs", msgs);
                startActivity(intent);//switch wo chess board activity
                clean();
            }
        });
        lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.soundManager.playSound(SoundManager.BUTTON);
                Game.dataManager.setGameMode(DataManager.GM_LAN);
                Intent intent = new Intent(getApplicationContext(), GameInfoActivity.class);
                startActivity(intent);
                Game.dataManager.setMyName(new Build().MODEL);
                Game.localServer.startListen();
                clean();
            }
        });
        wlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.soundManager.playSound(SoundManager.BUTTON);
                Game.socketManager.connectToRemoteServer();
                local.setVisibility(View.INVISIBLE);
                lan.setVisibility(View.INVISIBLE);
                wlan.setVisibility(View.INVISIBLE);
                records.setVisibility(View.INVISIBLE);
                waitImage.setVisibility(View.VISIBLE);
                waitBackground.setVisibility(View.VISIBLE);
                Game.startWaitAnimation(waitImage);
                clean();
            }
        });
        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecordsActivity.class));
            }
        });
        //internet init
        Game.socketManager.registerActivity(DataPack.CONNECTED, this);
        //setting
        Game.activityManager.add(this);
        Game.updateManager.checkUpdate();
        waitImage.setVisibility(View.INVISIBLE);
        waitBackground.setVisibility(View.INVISIBLE);
        //background img
        bk.setImageBitmap(Game.getBitmap(R.raw.choosemodebk));
        bk2.setImageBitmap(Game.getBitmap(R.raw.cloud));
        lan.setTypeface(Game.getFont());
        wlan.setTypeface(Game.getFont());
        local.setTypeface(Game.getFont());
    }

    @Override
    public void onStart() {
        super.onStart();
        Game.soundManager.resumeMusic(SoundManager.BACKGROUND);
    }

    @Override
    public void onStop() {
        super.onStop();
        Game.soundManager.pauseMusic();
    }

    public void clean() {
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if (exit) {
                    Game.activityManager.closeAll();
                    System.exit(0);
                } else {
                    exit = true;
                    Toast.makeText(getApplicationContext(), "press again to exit", Toast.LENGTH_SHORT).show();
                    closeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 1200);
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        if (dataPack.getCommand() == DataPack.CONNECTED) {
            if (dataPack.isSuccessful()) {
                Game.dataManager.setGameMode(DataManager.GM_WLAN);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                wlan.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Sorry,i can not connect to server now!", Toast.LENGTH_SHORT).show();
                        local.setVisibility(View.VISIBLE);
                        lan.setVisibility(View.VISIBLE);
                        wlan.setVisibility(View.VISIBLE);
                        records.setVisibility(View.VISIBLE);
                        waitBackground.setVisibility(View.INVISIBLE);
                        waitImage.setVisibility(View.INVISIBLE);
                    }
                });
            }
            Game.stopWaitAnimation();
        }
    }
}
