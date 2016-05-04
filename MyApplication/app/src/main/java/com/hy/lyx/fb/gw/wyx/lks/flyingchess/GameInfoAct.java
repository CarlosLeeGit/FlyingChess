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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GameInfoAct extends AppCompatActivity implements Target{
    Button createButton,joinButton,backButton;
    ListView roomListView;
    LinearLayout onlineLayout;
    SimpleAdapter roomListAdapter;
    LinkedList<HashMap<String,String>> roomListData;
    Worker worker;
    String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Game.activityManager.add(this);
        //init
        createButton=(Button)findViewById(R.id.create);
        backButton=(Button)findViewById(R.id.back);
        joinButton = (Button)findViewById(R.id.join);
        roomListView=(ListView)findViewById(R.id.roomList);
        roomListData=new LinkedList<>();
        String[] t={"room","id","player","state"};
        int[] t2={R.id.roomName,R.id.roomId,R.id.player,R.id.roomState};
        roomListAdapter=new SimpleAdapter(getApplicationContext(),roomListData,R.layout.content_game_info_room_list_item,t,t2);
        roomListView.setAdapter(roomListAdapter);
        worker=new Worker();
        onlineLayout=(LinearLayout)findViewById(R.id.onlineLayout);
        roomId="";
        //trigger
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//start a new game
                if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                    LinkedList<String> msgs=new LinkedList<String>();
                    msgs.addLast(Game.playerMapData.get("me").id);
                    msgs.addLast(Game.dataManager.getMyName()+"'s Room");
                    DataPack dataPack=new DataPack(DataPack.R_ROOM_CREATE,msgs);
                    Game.socketManager.send(dataPack);
                }
                else if(Game.dataManager.getGameMode()==DataManager.GM_LAN){

                }
                else{//local
                    Intent intent = new Intent(getApplicationContext(), RoomAct.class);
                    ArrayList<String> msgs=new ArrayList<String>();
                    msgs.add(Game.playerMapData.get("me").id);
                    msgs.add(Game.dataManager.getMyName());
                    msgs.add(Game.playerMapData.get("me").score);
                    msgs.add("-1");
                    intent.putStringArrayListExtra("msgs",msgs);
                    startActivity(intent);//switch wo chess board activity
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomId=roomListData.get(position).get("id");
                view.setSelected(true);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean find=false;
                synchronized (roomListData){
                    for(HashMap<String,String> map:roomListData){
                        if(map.get("id").compareTo(roomId)==0){
                            if(map.get("state").compareTo("waiting")==0) {
                                LinkedList<String> msgs=new LinkedList<>();
                                msgs.addLast(Game.playerMapData.get("me").id);
                                msgs.addLast(roomId);
                                DataPack dataPack = new DataPack(DataPack.R_ROOM_ENTER,msgs);
                                Game.socketManager.send(dataPack);
                                find=true;
                            }
                            break;
                        }
                    }
                    if(!find)
                        Toast.makeText(getApplicationContext(),"join room failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //network init
        if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
            Game.socketManager.registerActivity(DataPack.A_ROOM_LOOKUP,this);
            Game.socketManager.registerActivity(DataPack.A_ROOM_CREATE,this);
            Game.socketManager.registerActivity(DataPack.A_ROOM_ENTER,this);
            new Thread(worker).start();
        }
        // setting
        if(Game.dataManager.getGameMode()==DataManager.GM_LOCAL) {
            joinButton.setVisibility(View.INVISIBLE);
            onlineLayout.setVisibility(View.INVISIBLE);
            Game.dataManager.setMyName("ME");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                goBack();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void goBack(){
        if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
            LinkedList<String> msgs=new LinkedList<>();
            msgs.addLast(Game.playerMapData.get("me").id);
            DataPack dataPack=new DataPack(DataPack.R_LOGOUT,msgs);
            Game.socketManager.send(dataPack);
        }
        startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        if(dataPack.getCommand()==DataPack.A_ROOM_LOOKUP){
            synchronized(roomListData){
                roomListData.clear();
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
                    roomListData.addLast(data);
                    i+=4;
                }
                roomListView.post(new Runnable() {
                    @Override
                    public void run() {
                        roomListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.A_ROOM_CREATE){
            if(dataPack.isSuccessful()) {
                Game.dataManager.setRoomId(dataPack.getMessage(0));
                Intent intent = new Intent(getApplicationContext(), RoomAct.class);
                ArrayList<String> msgs=new ArrayList<>();
                msgs.add(Game.playerMapData.get("me").id);
                msgs.add(Game.playerMapData.get("me").name);
                msgs.add(Game.playerMapData.get("me").score);
                msgs.add("-1");
                intent.putStringArrayListExtra("msgs",msgs);
                startActivity(intent);//switch wo chess board activity
            }
            else{
                createButton.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"create room failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.A_ROOM_ENTER){
            if(dataPack.isSuccessful()){
                Game.dataManager.setRoomId(roomId);
                Intent intent = new Intent(getApplicationContext(), RoomAct.class);
                ArrayList<String> msgs = new ArrayList<>(dataPack.getMessageList());
                intent.putStringArrayListExtra("msgs",msgs);
                startActivity(intent);//switch wo chess board activity
            }
            else{
                createButton.post(new Runnable() {
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
    @Override
    public void run() {
        DataPack dataPack= new DataPack(DataPack.R_ROOM_LOOKUP,null);
        Game.socketManager.send(dataPack);
    }
}