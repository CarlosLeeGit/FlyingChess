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

public class GameInfoAct extends AppCompatActivity {
    Timer screenTimer;
    RelativeLayout frame;
    Button start;
    Timer closeTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        int uiOpts = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOpts);
        //init
        frame=(RelativeLayout)findViewById(R.id.gameInfo);
        screenTimer=new Timer();
        start=(Button)findViewById(R.id.start);
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
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//start a new game
                Intent intent = new Intent(getApplicationContext(),ChessBoardAct.class);
                startActivity(intent);//switch wo chess board activity
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }
        });
    }
}
