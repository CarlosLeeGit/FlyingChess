package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UpdateAct extends AppCompatActivity {
    Button ok, cancle;
    boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ok = (Button) findViewById(R.id.rn);
        cancle = (Button) findViewById(R.id.later);
        update = false;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = true;
                finish();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = false;
                finish();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(update){
            Game.updateManager.uw.confirmUpdate();
        }
        else{
            Game.updateManager.uw.cancleUpdate();
        }
    }
}
