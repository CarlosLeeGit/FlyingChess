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

public class GameInfoAct extends AppCompatActivity {
    Button create;
    Button join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        create=(Button)findViewById(R.id.create);
        join = (Button)findViewById(R.id.join);
        //trigger
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//start a new game
                Intent intent = new Intent(getApplicationContext(),RoomAct.class);
                startActivity(intent);//switch wo chess board activity
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        if(Game.getDataManager().getGameMode()==DataManager.GM_LOCAL)
            join.setVisibility(View.INVISIBLE);
        join.setEnabled(false);
    }
}
