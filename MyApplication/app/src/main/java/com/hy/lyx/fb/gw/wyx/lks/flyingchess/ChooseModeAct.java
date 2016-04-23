package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class ChooseModeAct extends AppCompatActivity {
    Button local,lan,wlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        local=(Button)findViewById(R.id.button2);
        lan=(Button)findViewById(R.id.button3);
        wlan=(Button)findViewById(R.id.button4);
        //trigger
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//choose local game
                Game.getDataManager().setGameMode(DataManager.GM_LOCAL);//set game mode
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);//switch to GameInfoAct
            }
        });
        lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);
            }
        });
        wlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getDataManager().setGameMode(DataManager.GM_WLAN);
                Intent intent=new Intent(getApplicationContext(),LoginAct.class);
                startActivity(intent);
            }
        });
    }
}
