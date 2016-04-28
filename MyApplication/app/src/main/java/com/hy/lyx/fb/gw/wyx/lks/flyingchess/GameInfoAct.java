package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameInfoAct extends AppCompatActivity implements Target{
    Button create;
    Button join;
    Button back;
    ListView room;
    LinearLayout onlineLayout;
    SimpleAdapter roomListAdapter;
    LinkedList<Map<String,String>> roomList;
    Worker worker;
    int lastSelection;
    String roomId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        create=(Button)findViewById(R.id.create);
        join = (Button)findViewById(R.id.join);
        room=(ListView)findViewById(R.id.roomList);
        roomList=new LinkedList<>();
        String[] t={"room","id","player","state"};
        int[] t2={R.id.roomName,R.id.roomId,R.id.player,R.id.roomState};
        back=(Button)findViewById(R.id.back);

        roomListAdapter=new SimpleAdapter(getApplicationContext(),roomList,R.layout.content_game_info_room_list_item,t,t2);
        room.setAdapter(roomListAdapter);
        worker=new Worker();
        lastSelection=-1;
        onlineLayout=(LinearLayout)findViewById(R.id.onlineLayout);
        //trigger
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//start a new game
                if(Game.getDataManager().getGameMode()==DataManager.GM_WLAN){
                    LinkedList<String> msgs=new LinkedList<String>();
                    msgs.addLast(Game.getDataManager().getId());
                    msgs.addLast(Game.getDataManager().getUserName()+"'s Room");
                    DataPack dataPack=new DataPack(DataPack.ROOM_CREATE,msgs);
                    Game.getSocketManager().send(dataPack);
                }
                else if(Game.getDataManager().getGameMode()==DataManager.GM_LAN){

                }
                else{//local
                    Intent intent = new Intent(getApplicationContext(), RoomAct.class);
                    startActivity(intent);//switch wo chess board activity
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
            }
        });
        room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lastSelection!=-1&&lastSelection!=position&&lastSelection<room.getChildCount())
                    room.getChildAt(lastSelection).setSelected(false);
                view.setSelected(true);
                lastSelection=position;
                join.setEnabled(true);
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (roomList){
                    if(roomList.get(lastSelection).get("state").compareTo("waiting")==0){
                        LinkedList<String> msgs=new LinkedList<>();
                        msgs.addLast(Game.getDataManager().getId());
                        roomId=roomList.get(lastSelection).get("id");
                        msgs.addLast(roomId);
                        DataPack dataPack = new DataPack(DataPack.ROOM_ENTER,msgs);
                        Game.getSocketManager().send(dataPack);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"they are flying!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //network init
        if(Game.getDataManager().getGameMode()==DataManager.GM_WLAN){
            Game.getSocketManager().registerActivity(DataPack.ROOM_LOOKUP,this);
            Game.getSocketManager().registerActivity(DataPack.ROOM_CREATE,this);
            Game.getSocketManager().registerActivity(DataPack.ROOM_ENTER,this);
            new Thread(worker).start();
        }
        // setting
        if(Game.getDataManager().getGameMode()==DataManager.GM_LOCAL) {
            join.setVisibility(View.INVISIBLE);
            onlineLayout.setVisibility(View.INVISIBLE);
        }
        join.setEnabled(false);
    }

    @Override
    public void onStop(){
        super.onStop();
        worker.exit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        if(dataPack.getCommand()==DataPack.ROOM_LOOKUP){
            synchronized(roomList){
                String id="-1";
                if(lastSelection!=-1){
                    id = roomList.get(lastSelection).get("id");
                }
                roomList.clear();
                for(int i=0;i<dataPack.getMessageList().size();){
                    HashMap<String,String> data=new HashMap<>();
                    data.put("room",dataPack.getMessage(i+1));
                    data.put("id",dataPack.getMessage(i));
                    data.put("player",dataPack.getMessage(i+2));
                    if(dataPack.getMessage(i+3).compareTo("0")==0){
                        data.put("state","waiting");
                    }
                    else{
                        data.put("state","flying");
                    }
                    roomList.addLast(data);
                    i+=4;
                    if(data.get("id").compareTo(id)==0){
                        lastSelection=i/4;
                    }
                }
                room.post(new Runnable() {
                    @Override
                    public void run() {
                        roomListAdapter.notifyDataSetChanged();
                        room.setSelection(lastSelection);
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.ROOM_CREATE){
            if(dataPack.isSuccessful()) {
                Game.getDataManager().setOnlineIds(0,Game.getDataManager().getId());
                Game.getDataManager().setOnlineNames(0,Game.getDataManager().getUserName());
                Game.getDataManager().setOnlineScores(0,Game.getDataManager().getOnlineScore());
                Game.getDataManager().setOnlinePos(0,"-1");
                Game.getDataManager().setPlayerNumber(1);

                Game.getDataManager().setRoomId(dataPack.getMessage(0));
                Intent intent = new Intent(getApplicationContext(), RoomAct.class);
                startActivity(intent);//switch wo chess board activity
            }
            else{
                create.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"create room failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.ROOM_ENTER){
            if(dataPack.isSuccessful()){
                int i = 0;
                for(i=0;i<dataPack.getMessageList().size();){
                    Game.getDataManager().setOnlineIds(i/4,dataPack.getMessage(i));
                    Game.getDataManager().setOnlineNames(i/4,dataPack.getMessage(i+1));
                    Game.getDataManager().setOnlineScores(i/4,dataPack.getMessage(i+2));
                    Game.getDataManager().setOnlinePos(i/4,dataPack.getMessage(i+3));
                    i+=4;
                }
                Game.getDataManager().setPlayerNumber(i/4);
                Game.getDataManager().setRoomId(roomId);
                Intent intent = new Intent(getApplicationContext(), RoomAct.class);
                startActivity(intent);//switch wo chess board activity
            }
            else{
                create.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"join room failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
///////////////////////////////////////////////////////////////////////////////////
class Worker implements Runnable{
    private boolean exit;
    public void exit(){
        exit=true;
    }
    @Override
    public void run() {
        exit=false;
        while(!exit){
            DataPack dataPack= new DataPack(DataPack.ROOM_LOOKUP,null);
            Game.getSocketManager().send(dataPack);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}