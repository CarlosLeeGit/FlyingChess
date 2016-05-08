package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PauseAct extends Activity {
    Button resume,robot,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        resume=(Button)findViewById(R.id.resume);
        robot=(Button)findViewById(R.id.robot);
        exit=(Button)findViewById(R.id.exit);
        //trigger
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        robot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Game.dataManager.isGiveUp()){
                    robot.setText("取消托管");
                    Game.dataManager.giveUp(true);
                }
                else{
                    robot.setText("托管");
                    Game.dataManager.giveUp(false);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){

                }
                Game.dataManager.setWinner("x");
                Game.gameManager.gameOver();
                startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
                Game.dataManager.giveUp(false);
            }
        });
        if(Game.dataManager.isGiveUp()){
            robot.setText("取消托管");
        }
    }
}
