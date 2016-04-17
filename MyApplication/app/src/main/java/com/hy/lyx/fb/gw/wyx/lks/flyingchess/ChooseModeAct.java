package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class ChooseModeAct extends AppCompatActivity {
    RelativeLayout frame;
    Timer screenTimer,closeTimer;
    Button local,lan,wlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        int uiOpts = View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOpts);
        //init
        local=(Button)findViewById(R.id.button2);
        lan=(Button)findViewById(R.id.button3);
        wlan=(Button)findViewById(R.id.button4);
        frame=(RelativeLayout)findViewById(R.id.chooseMode);
        screenTimer = new Timer();
        closeTimer=new Timer();
        //trigger
        frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                screenTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        frame.post(new Runnable() {
                            @Override
                            public void run() {
                                int uiOpts = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                getWindow().getDecorView().setSystemUiVisibility(uiOpts);
                            }
                        });
                    }
                }, 3500);
                return true;
            }
        });
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//choose local game
                Game.getDataManager().setGameMode(DataManager.GM_LOCAL);//set game mode
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);//switch to GameInfoAct
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }
        });
        lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }
        });
        wlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getDataManager().setGameMode(DataManager.GM_WLAN);
                Intent intent=new Intent(getApplicationContext(),LoginAct.class);
                startActivity(intent);
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });
    }
}
