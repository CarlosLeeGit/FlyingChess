package com.flashminds.flyingchess.manager;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by karthur on 2016/5/13.
 */
public class LogManager {
    private File file;
    private FileOutputStream os;
    private DataOutputStream dos;

    public LogManager() {
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/FlashMinds.com/FlyingChess/log.log");
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void p(Object... argv) {
        String log = new String();
        for (int i = 0; i < argv.length; i++) {
            log += String.valueOf(argv[i]);
            log += "\t";
        }
        log += "\n";
        try {
            dos.writeUTF(log);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
