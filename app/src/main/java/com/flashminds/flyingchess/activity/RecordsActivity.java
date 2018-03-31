package com.flashminds.flyingchess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.flashminds.flyingchess.entity.Game;
import com.flashminds.flyingchess.R;
import com.flashminds.flyingchess.manager.SoundManager;

import java.io.File;
import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {
    ListView recordList;
    ArrayList<String> records;
    ArrayAdapter<String> recordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Game.activityManager.add(this);
        Game.soundManager.playMusic(SoundManager.BACKGROUND);
        //////////////
        setupViewComponent();
    }

    @Override
    public void onStart() {
        super.onStart();
        Game.soundManager.resumeMusic(SoundManager.BACKGROUND);
    }

    @Override
    public void onStop() {
        super.onStop();
        Game.soundManager.pauseMusic();
    }

    private void setupViewComponent() {
        recordList = (ListView) findViewById(R.id.recordList);
        records = searchRecords(Game.replayManager.PATH);
        recordsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);
        if (records.isEmpty())
            Toast.makeText(getApplicationContext(), "No Records!", Toast.LENGTH_SHORT).show();
        else {
            recordList.setAdapter(recordsAdapter);
            recordList.setOnItemClickListener(onItemClickLis);
            recordList.setOnItemLongClickListener(onItemLongClickLis);
        }
    }

    AdapterView.OnItemClickListener onItemClickLis = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String fileName = ((TextView) view).getText().toString();
            Game.replayManager.openFile(Game.replayManager.PATH + fileName);
            Game.replayManager.startReplay();
            Game.playersData.clear();
            int playNum = Game.replayManager.getPlayerNum();
            for (int i = 0; i < playNum; i++) {
                Game.playersData.put(Game.replayManager.getSavedKey(), Game.replayManager.getSavedRole());
            }
            startActivity(new Intent(getApplicationContext(), ChessBoardActivity.class));
        }
    };

    AdapterView.OnItemLongClickListener onItemLongClickLis = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            deleteFile(view, position);
            return true;
        }
    };

    private ArrayList<String> searchRecords(String path) {
        ArrayList<String> records = new ArrayList<>();
        File dir = new File(path);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                records.add(file.getName());
            }
        }

        return records;
    }

    public void deleteFile(final View view, final int pos) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_delete_record, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final String fileName = ((TextView) view).getText().toString();
                File file = new File(Game.replayManager.PATH + fileName);
                file.delete();
                records.remove(pos);
                recordsAdapter.notifyDataSetChanged();
                return false;
            }
        });

        popupMenu.show();
    }
}
