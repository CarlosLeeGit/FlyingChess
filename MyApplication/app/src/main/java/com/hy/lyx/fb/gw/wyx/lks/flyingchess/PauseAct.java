package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.LinkedList;

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
                    robot.setText("Cancel auto");
                    Game.dataManager.giveUp(true);
                }
                else{
                    robot.setText("Auto");
                    Game.dataManager.giveUp(false);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Game.dataManager.getGameMode()!=DataManager.GM_LOCAL){
                    Game.socketManager.send(DataPack.R_GAME_EXIT, Game.dataManager.getMyId(), Game.dataManager.getRoomId());
                }
                if(Game.replayManager.isReplay == false) {
                    Game.replayManager.closeRecord();
                    Game.replayManager.clearRecord();
                }
                Game.gameManager.gameOver();
                Game.replayManager.stopReplay();
                if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                    startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
                }
                Game.dataManager.giveUp(false);
                Game.soundManager.playMusic(SoundManager.BACKGROUND);
            }
        });
        if(Game.dataManager.isGiveUp()){
            robot.setText("Cancel auto");
        }
        resume.setTypeface(Game.getFont());
        robot.setTypeface(Game.getFont());
        exit.setTypeface(Game.getFont());
    }
    @Override
    public void onStart(){
        super.onStart();
        Game.soundManager.resumeMusic(SoundManager.BACKGROUND);
    }
}
