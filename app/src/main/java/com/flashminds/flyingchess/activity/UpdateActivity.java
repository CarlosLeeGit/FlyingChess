package com.flashminds.flyingchess.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.flashminds.flyingchess.entity.Game;
import com.flashminds.flyingchess.R;

public class UpdateActivity extends Activity {
    Button ok, cancel;
    boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ok = (Button) findViewById(R.id.rn);
        cancel = (Button) findViewById(R.id.later);
        update = false;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = true;
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update = false;
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (update) {
            Game.updateManager.uw.confirmUpdate();
        } else {
            Game.updateManager.uw.cancleUpdate();
        }
    }
}
