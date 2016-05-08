package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingAct extends AppCompatActivity {
    TextView hint;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Game.activityManager.add(this);
        hint=(TextView)findViewById(R.id.hint);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        hint.setText(getIntent().getExtras().getString("tipe"));
    }
}
