package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class RoomAct extends AppCompatActivity implements Target {
    Button startButton,backButton,site[],addRobotButton[];
    Timer closeTimer;
    int[] siteState;// -1 none   0 robot    1 people
    ListView idlePlayerView;
    LinkedList<HashMap<String,String>> idlePlayerListData;
    SimpleAdapter idlePlayerListAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Game.activityManager.add(this);
        //init
        closeTimer=new Timer();
        startButton=(Button)findViewById(R.id.start);
        backButton=(Button)findViewById(R.id.back);
        site=new Button[4];
        site[0]=(Button)findViewById(R.id.R);
        site[1]=(Button)findViewById(R.id.G);
        site[2]=(Button)findViewById(R.id.B);
        site[3]=(Button)findViewById(R.id.Y);
        addRobotButton=new Button[4];
        addRobotButton[0]=(Button)findViewById(R.id.jr);
        addRobotButton[1]=(Button)findViewById(R.id.jg);
        addRobotButton[2]=(Button)findViewById(R.id.jb);
        addRobotButton[3]=(Button)findViewById(R.id.jy);
        siteState=new int[4];
        idlePlayerView=(ListView)findViewById(R.id.playerInRoom);
        idlePlayerListData=new LinkedList<>();
        idlePlayerListAdapter=new SimpleAdapter(getApplicationContext(),idlePlayerListData,R.layout.conteng_room_player_list_item,new String[] {"name","score"},new int[] {R.id.nameInRoom,R.id.scoreInRoom});
        idlePlayerView.setAdapter(idlePlayerListAdapter);
        //trigger
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.sound.button();
                if(idlePlayerListData.size()>1)
                    Toast.makeText(getApplicationContext(),"some one is not ready!",Toast.LENGTH_SHORT).show();
                else{
                    if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                        if(Game.playerMapData.get("host").id.compareTo(Game.playerMapData.get("me").id)!=0){
                            Toast.makeText(getApplicationContext(),"wait for room host",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LinkedList<String> msgs=new LinkedList<>();
                        msgs.addLast(Game.playerMapData.get("me").id);
                        msgs.addLast(Game.dataManager.getRoomId());
                        DataPack dataPack=new DataPack(DataPack.R_GAME_START,msgs);
                        Game.socketManager.send(dataPack);
                    }
                    else if(Game.dataManager.getGameMode()==DataManager.GM_LOCAL){
                        Intent intent=new Intent(getApplicationContext(),ChessBoardAct.class);
                        startActivity(intent);
                        closeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },2000);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.sound.returnButton();
                if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                    LinkedList<String> msgs=new LinkedList<>();
                    msgs.addLast(Game.playerMapData.get("me").id);
                    msgs.addLast(Game.dataManager.getRoomId());
                    msgs.addLast(String.format("%d",Game.playerMapData.get("me").color));
                    DataPack dataPack = new DataPack(DataPack.R_ROOM_EXIT,msgs);
                    Game.socketManager.send(dataPack);
                }
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }
        });

        site[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSite(0);
            }
        });

        site[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSite(1);
            }
        });

        site[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSite(2);
            }
        });

        site[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSite(3);
            }
        });

        addRobotButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRobot(0);
            }
        });

        addRobotButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRobot(1);
            }
        });

        addRobotButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRobot(2);
            }
        });

        addRobotButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRobot(3);
            }
        });
        //internet init
        if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
            Game.socketManager.registerActivity(DataPack.E_ROOM_POSITION_SELECT,this);
            Game.socketManager.registerActivity(DataPack.E_GAME_START,this);
            Game.socketManager.registerActivity(DataPack.A_ROOM_ENTER,this);
            Game.socketManager.registerActivity(DataPack.E_ROOM_ENTER,this);
            Game.socketManager.registerActivity(DataPack.A_ROOM_EXIT,this);
            Game.socketManager.registerActivity(DataPack.E_ROOM_EXIT,this);
        }
        //setting
        siteState[0] = -1;
        siteState[1] = -1;
        siteState[2] = -1;
        siteState[3] = -1;
        HashMap<String,String> map=new HashMap<>();
        map.put("name","name");
        map.put("score","score");
        idlePlayerListData.addLast(map);
        Game.playerMapData.clear();
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> players = bundle.getStringArrayList("msgs");
        Game.playerMapData.put("host",new Player(players.get(0),players.get(1),players.get(2),Integer.valueOf(players.get(3))));
        for(int i=0;i<players.size();) {//添加玩家到指定的位置
            int color = Integer.valueOf(players.get(i+3));
            if (Integer.valueOf(players.get(i))<0) {//机器人
                siteState[color]=0;
                site[color].setText("ROBOT");
                addRobotButton[color].setText("-");
            }
            else {//玩家
                if(color==-1){
                    HashMap<String,String> map2=new HashMap<>();
                    map2.put("name",players.get(i+1));
                    map2.put("score",players.get(i+2));
                    idlePlayerListData.addLast(map2);
                }
                else{
                    site[color].setText(players.get(i+1));
                    siteState[color] = 1;
                }
            }
            HashMap<String,Player> g = Game.playerMapData;
            if(Game.dataManager.getMyName().compareTo(players.get(i+1))!=0){
                Game.playerMapData.put(players.get(i),new Player(players.get(i),players.get(i+1),players.get(i+2),Integer.valueOf(players.get(i+3))));
            }
            else{
                Game.playerMapData.put("me",new Player(players.get(i),players.get(i+1),players.get(i+2),Integer.valueOf(players.get(i+3))));
            }
            i+=4;
        }
        idlePlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exit();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void processDataPack(final DataPack dataPack) {
        if(dataPack.getCommand()==DataPack.E_ROOM_ENTER){
            HashMap<String,String> map=new HashMap<>();
            map.put("name",dataPack.getMessage(1));
            map.put("score",dataPack.getMessage(2));
            Game.playerMapData.put(dataPack.getMessage(0),new Player(dataPack.getMessage(0),dataPack.getMessage(1),dataPack.getMessage(2),Integer.valueOf(dataPack.getMessage(3))));
            idlePlayerListData.addLast(map);
            site[0].post(new Runnable() {
                @Override
                public void run() {
                    idlePlayerListAdapter.notifyDataSetChanged();
                }
            });
        }
        else if(dataPack.getCommand()==DataPack.E_ROOM_EXIT){
            if(dataPack.getMessage(0).compareTo(Game.playerMapData.get("host").id)==0)//是房主
            {
                Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
                startActivity(intent);
                closeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }
            else{
                for(HashMap<String,String> map:idlePlayerListData){
                    if(map.get("name").compareTo(dataPack.getMessage(1))==0){
                        idlePlayerListData.remove(map);
                        site[0].post(new Runnable() {
                            @Override
                            public void run() {
                                idlePlayerListAdapter.notifyDataSetChanged();
                            }
                        });
                        return ;
                    }
                }
                for(int i=0;i<4;i++){
                    if(site[i].getText().toString().compareTo(dataPack.getMessage(1))==0){
                        final int tmp=i;
                        site[i].post(new Runnable() {
                            @Override
                            public void run() {
                                site[tmp].setText("");
                                siteState[tmp]=-1;
                            }
                        });
                    }
                }
                for(String key:Game.playerMapData.keySet()){
                    if(Game.playerMapData.get(key).name.compareTo(dataPack.getMessage(1))==0){
                        Game.playerMapData.remove(key);
                        break;
                    }
                }
            }
        }
        else if(dataPack.getCommand()==DataPack.E_ROOM_POSITION_SELECT) {
            if(dataPack.isSuccessful()){
                final int id = Integer.valueOf(dataPack.getMessage(0));
                final int np = Integer.valueOf(dataPack.getMessage(3));
                if (id < 0) {//robot choose
                    if(np!=-1){//如果添加机器人
                        siteState[np]=0;
                        site[0].post(new Runnable() {
                            @Override
                            public void run() {
                                site[np].setText("ROBOT");
                                addRobotButton[np].setText("-");
                            }
                        });
                        Game.playerMapData.put(dataPack.getMessage(0),new Player(dataPack.getMessage(0),"ROBOT","0",np));
                    }
                    else{
                        siteState[-id-1]=-1;
                        site[0].post(new Runnable() {
                            @Override
                            public void run() {
                                site[-id-1].setText("");
                                addRobotButton[-id-1].setText("+");
                            }
                        });
                        Game.playerMapData.remove(dataPack.getMessage(0));
                    }
                }
                else {
                    for (HashMap<String, String> map : idlePlayerListData) {
                        if (map.get("name").compareTo(dataPack.getMessage(1)) == 0) {
                            idlePlayerListData.remove(map);
                            break;
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        if (site[i].getText().toString().compareTo(dataPack.getMessage(1)) == 0) {
                            final int tmp = i;
                            site[0].post(new Runnable() {
                                @Override
                                public void run() {
                                    site[tmp].setText("");
                                    siteState[tmp] = -1;
                                }
                            });
                            break;
                        }
                    }
                    if (np == -1) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", dataPack.getMessage(1));
                        map.put("score", dataPack.getMessage(2));
                        idlePlayerListData.addLast(map);
                    }
                    else {
                        site[np].post(new Runnable() {
                            @Override
                            public void run() {
                                siteState[np] = 1;
                                site[np].setText(dataPack.getMessage(1));
                            }
                        });
                    }
                    for(String key:Game.playerMapData.keySet()){
                        if(Game.playerMapData.get(key).id.compareTo(dataPack.getMessage(0))==0){
                            Game.playerMapData.get(key).color=np;
                        }
                    }
                    site[np].post(new Runnable() {
                        @Override
                        public void run() {
                            idlePlayerListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            else{
                idlePlayerView.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"position select failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.E_GAME_START){
            System.out.println(dataPack.toString());
            Intent intent=new Intent(getApplicationContext(),ChessBoardAct.class);
            startActivity(intent);
            closeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            },2000);
        }
    }

    private void chooseSite(int color){
        if(siteState[color]==-1) {
            if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                LinkedList<String> msgs = new LinkedList<>();
                msgs.addLast(Game.playerMapData.get("me").id);
                msgs.addLast(Game.dataManager.getRoomId());
                msgs.addLast(Game.playerMapData.get("me").name);
                msgs.addLast(Game.playerMapData.get("me").score);
                msgs.addLast(String.format("%d", color));
                Game.socketManager.send(new DataPack(DataPack.R_ROOM_POSITION_SELECT,msgs));
            }
            else if(Game.dataManager.getGameMode()==DataManager.GM_LOCAL){
                if(Game.playerMapData.get("me").color==ChessBoard.COLOR_Z){
                    idlePlayerListData.removeLast();
                    idlePlayerListAdapter.notifyDataSetChanged();
                }
                else{
                    site[Game.playerMapData.get("me").color].setText("");
                    siteState[Game.playerMapData.get("me").color]=-1;
                }
                for(String key:Game.playerMapData.keySet()){
                    if(Game.playerMapData.get(key).name.compareTo(Game.dataManager.getMyName())==0){
                        Game.playerMapData.get(key).color=color;
                    }
                }
                site[color].setText("ME");
                siteState[color]=1;
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"select position failed!",Toast.LENGTH_SHORT).show();
        }
    }

    private void addRobot(int color){
        if(Game.playerMapData.get("host").id.compareTo(Game.playerMapData.get("me").id)==0){
            if(siteState[color]==1){
                Toast.makeText(getApplicationContext(),"add robot failed!",Toast.LENGTH_SHORT).show();
            }
            else {
                if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                    LinkedList<String> msgs=new LinkedList<>();
                    msgs.addLast(String.format("%d",-color-1));
                    msgs.addLast(Game.dataManager.getRoomId());
                    msgs.addLast("ROBOT");
                    msgs.addLast("0");
                    if(siteState[color]==-1){
                        msgs.addLast(String.format("%d",color));
                    }
                    else{
                        msgs.addLast("-1");
                    }
                    Game.socketManager.send(new DataPack(DataPack.R_ROOM_POSITION_SELECT,msgs));
                }
                else if(Game.dataManager.getGameMode()==DataManager.GM_LOCAL){
                    if(siteState[color]==-1){
                        site[color].setText("ROBOT");
                        siteState[color]=0;
                        addRobotButton[color].setText("-");
                        Game.playerMapData.put(String.format("%d",-color-1),new Player(String.format("%d",-color-1),"robot","0",color));
                    }
                    else{
                        site[color].setText("");
                        siteState[color]=-1;
                        addRobotButton[color].setText("+");
                        Game.playerMapData.remove(String.format("%d",-color-1));
                    }
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"only host can add robot",Toast.LENGTH_SHORT).show();
        }
    }

    private void exit(){
        if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
            LinkedList<String> msgs=new LinkedList<>();
            msgs.addLast(Game.playerMapData.get("me").id);
            msgs.addLast(Game.dataManager.getRoomId());
            msgs.addLast(String.format("%d",Game.playerMapData.get("me").color));
            DataPack dataPack2 = new DataPack(DataPack.R_ROOM_EXIT,msgs);
            Game.socketManager.send(dataPack2);
        }
        Intent intent=new Intent(getApplicationContext(),GameInfoAct.class);
        startActivity(intent);
        closeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
}
