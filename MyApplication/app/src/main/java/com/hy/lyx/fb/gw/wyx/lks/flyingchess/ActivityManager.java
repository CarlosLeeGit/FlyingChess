package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by karthur on 2016/5/4.
 */
public class ActivityManager {
    private LinkedList<AppCompatActivity> list;
    public ActivityManager(){
        list=new LinkedList<>();
    }
    public void add(AppCompatActivity activity){
        list.addLast(activity);
    }
    public void back(){
        list.getLast().finish();
        list.removeLast();
    }
    public void closeAll(){
        for(AppCompatActivity activity: list){
            activity.finish();
        }
        list.clear();
    }
}
