package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeAct extends AppCompatActivity {
    Timer timer;
    Timer timer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //UI setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        int uiOpts = View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOpts);
        //initialization
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ChooseModeAct.class);
                startActivity(intent);
            }
        }, 2000);
        timer2=new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        },4000);

        GameManager.init();
    }
}
