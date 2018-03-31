package com.flashminds.flyingchess.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.flashminds.flyingchess.manager.DataManager;
import com.flashminds.flyingchess.dataPack.DataPack;
import com.flashminds.flyingchess.entity.Game;
import com.flashminds.flyingchess.R;

public class PauseActivity extends Activity {
    Button resume, robot, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        resume = (Button) findViewById(R.id.resume);
        robot = (Button) findViewById(R.id.robot);
        exit = (Button) findViewById(R.id.exit);
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
                if (!Game.dataManager.isGiveUp()) {
                    robot.setText("Cancel auto");
                    Game.dataManager.giveUp(true);
                } else {
                    robot.setText("Auto");
                    Game.dataManager.giveUp(false);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.dataManager.getGameMode() == DataManager.GM_WLAN) {
                    Game.socketManager.send(DataPack.R_GAME_EXIT, Game.dataManager.getMyId(), Game.dataManager.getRoomId());
                }
                if (Game.replayManager.isReplay == false) {
                    Game.replayManager.closeRecord();
                    Game.replayManager.clearRecord();
                }
                Game.gameManager.gameOver();
                Game.replayManager.stopReplay();
                if (Game.dataManager.getGameMode() != DataManager.GM_LOCAL) {
                    startActivity(new Intent(getApplicationContext(), GameInfoActivity.class));
                    if (Game.dataManager.getGameMode() == DataManager.GM_LAN) {
                        Game.localServer.stopHost();
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), ChooseModeActivity.class));
                }
                Game.dataManager.giveUp(false);
            }
        });
        if (Game.dataManager.isGiveUp()) {
            robot.setText("Cancel auto");
        }
        resume.setTypeface(Game.getFont());
        robot.setTypeface(Game.getFont());
        exit.setTypeface(Game.getFont());
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
