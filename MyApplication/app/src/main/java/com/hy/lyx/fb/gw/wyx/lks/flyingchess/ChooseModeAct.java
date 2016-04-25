package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ChooseModeAct extends AppCompatActivity {
    Button local,lan,wlan;
    boolean exit;
    Timer closeTimer;
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
        exit=false;
        closeTimer=new Timer();
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
                Game.getDataManager().setGameMode(DataManager.GM_LAN);
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);
            }
        });
        wlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getDataManager().setGameMode(DataManager.GM_WLAN);
                if(Game.getDataManager().needLogin()){
                    startActivity(new Intent(getApplicationContext(),LoginAct.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        exit=false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if(exit){
                    System.exit(0);
                }
                else{
                    exit=true;
                    Toast.makeText(getApplicationContext(),"press again to exit",Toast.LENGTH_SHORT).show();
                    closeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            exit=false;
                        }
                    },1200);
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
