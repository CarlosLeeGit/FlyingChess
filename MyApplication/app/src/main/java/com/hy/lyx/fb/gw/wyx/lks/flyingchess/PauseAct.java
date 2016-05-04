package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class PauseAct extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        //trigger
    }
}
