package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class SettingAct extends AppCompatActivity {
    RelativeLayout frame;
    Timer screenTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        int uiOpts = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOpts);
        //init
        frame=(RelativeLayout)findViewById(R.id.setting);
        screenTimer=new Timer();
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
    }
}
