package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseModeAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        int uiOpts = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOpts);
    }
}
