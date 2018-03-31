package com.flashminds.flyingchess.manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by karthur on 2016/5/4.
 */
public class ActivityManager {
    private LinkedList<AppCompatActivity> list;
    private android.app.ActivityManager activityManager;

    public ActivityManager(AppCompatActivity activity) {
        list = new LinkedList<>();
        activityManager = (android.app.ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void add(AppCompatActivity activity) {
        list.addLast(activity);
        if (list.size() > 2) {
            list.getFirst().finish();
            list.removeFirst();
        }
    }

    public boolean isSuspend() {
        List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return true;
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(list.getFirst().getPackageName())
                    && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return false;
            }
        }
        return true;
    }

    public void closeAll() {
        for (AppCompatActivity activity : list) {
            activity.finish();
        }
        list.clear();
    }
}
