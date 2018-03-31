package com.flashminds.flyingchess.manager;

import android.support.v7.app.AppCompatActivity;

import com.flashminds.flyingchess.entity.UpdateWorker;

/**
 * Created by karthur on 2016/5/12.
 */
public class UpdateManager {
    public UpdateWorker uw;
    private boolean checked;

    public UpdateManager(AppCompatActivity activity) {
        uw = new UpdateWorker(activity);
        checked = false;
    }

    public void checkUpdate() {
        if (checked == false) {
            new Thread(uw).start();
        }
        checked = true;
    }
}

