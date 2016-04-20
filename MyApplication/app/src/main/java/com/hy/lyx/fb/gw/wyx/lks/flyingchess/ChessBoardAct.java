package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ChessBoardAct extends AppCompatActivity {
    RelativeLayout frame;
    Timer screenTimer,closeTimer;
    Button pause,dice;
    Button r1,r2,r3,r4;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        int uiOpts = View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOpts);
        //init
        frame=(RelativeLayout)findViewById(R.id.chessBoard);
        screenTimer=new Timer();
        closeTimer=new Timer();
        pause=(Button)findViewById(R.id.pause);
        dice=(Button)findViewById(R.id.dice);
        r1=(Button)findViewById(R.id.R1);
        r2=(Button)findViewById(R.id.R2);
        r3=(Button)findViewById(R.id.R3);
        r4=(Button)findViewById(R.id.R4);
        tv = (TextView)findViewById(R.id.textView);
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
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameInfoAct.class);
                startActivity(intent);
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });
        dice.setOnClickListener(new View.OnClickListener() {//throw dice
            @Override
            public void onClick(View v) {
                Game.getPlayer().setDiceValid();
            }
        });
        /////////////////add four plane trigger and when click a plane, we should call game manager :: choosePlane to choose plane
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getPlayer().setPlaneValid(0);
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getPlayer().setPlaneValid(1);
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getPlayer().setPlaneValid(2);
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getPlayer().setPlaneValid(3);
            }
        });
        Game.getGameManager().newTurn();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                Game.getGameManager().gameOver();
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
