package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class RoomAct extends AppCompatActivity implements Target {
    Button start,back,r,g,b,y,jr,jg,jb,jy;
    Timer closeTimer;
    int me;
    int[] pos;// -1 none   0 robot    1 people
    ListView players;
    LinkedList<HashMap<String,String>> playerList;
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        closeTimer=new Timer();
        start=(Button)findViewById(R.id.start);
        back=(Button)findViewById(R.id.back);
        r=(Button)findViewById(R.id.R);
        g=(Button)findViewById(R.id.G);
        b=(Button)findViewById(R.id.B);
        y=(Button)findViewById(R.id.Y);
        jr=(Button)findViewById(R.id.jr);
        jg=(Button)findViewById(R.id.jg);
        jb=(Button)findViewById(R.id.jb);
        jy=(Button)findViewById(R.id.jy);
        pos=new int[4];
        players=(ListView)findViewById(R.id.playerInRoom);
        playerList=new LinkedList<>();
        simpleAdapter=new SimpleAdapter(getApplicationContext(),playerList,R.layout.conteng_room_player_list_item,new String[] {"name","score"},new int[] {R.id.nameInRoom,R.id.scoreInRoom});
        players.setAdapter(simpleAdapter);
        //trigger
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(me==-1)
                    Toast.makeText(getApplicationContext(),"you have to choose a color",Toast.LENGTH_SHORT).show();
                else{
                    Game.getDataManager().setSiteState(pos);
                    Game.getDataManager().setMyColor(me);
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
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedList<String> msgs=new LinkedList<>();
                msgs.addLast(Game.getDataManager().getId());
                msgs.addLast(Game.getDataManager().getRoomId());
                DataPack dataPack = new DataPack(DataPack.ROOM_EXIT,msgs);
                Game.getSocketManager().send(dataPack);
                Game.getDataManager().setMyColor(-1);
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

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_RED]!=-1)
                    return;
                switch (me){
                    case ChessBoard.COLOR_BLUE:
                        b.setText("");
                        pos[ChessBoard.COLOR_BLUE]=-1;
                        break;
                    case ChessBoard.COLOR_GREEN:
                        g.setText("");
                        pos[ChessBoard.COLOR_GREEN]=-1;
                        break;
                    case ChessBoard.COLOR_YELLOW:
                        y.setText("");
                        pos[ChessBoard.COLOR_YELLOW]=-1;
                        break;
                }
                me=ChessBoard.COLOR_RED;
                pos[ChessBoard.COLOR_RED]=1;
                r.setText("Me");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_GREEN]!=-1)
                    return;
                switch (me){
                    case ChessBoard.COLOR_BLUE:
                        b.setText("");
                        pos[ChessBoard.COLOR_BLUE]=-1;
                        break;
                    case ChessBoard.COLOR_RED:
                        r.setText("");
                        pos[ChessBoard.COLOR_RED]=-1;
                        break;
                    case ChessBoard.COLOR_YELLOW:
                        y.setText("");
                        pos[ChessBoard.COLOR_YELLOW]=-1;
                        break;
                }
                me=ChessBoard.COLOR_GREEN;
                pos[ChessBoard.COLOR_GREEN]=1;
                g.setText("Me");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_BLUE]!=-1)
                    return;
                switch (me){
                    case ChessBoard.COLOR_GREEN:
                        g.setText("");
                        pos[ChessBoard.COLOR_GREEN]=-1;
                        break;
                    case ChessBoard.COLOR_RED:
                        r.setText("");
                        pos[ChessBoard.COLOR_RED]=-1;
                        break;
                    case ChessBoard.COLOR_YELLOW:
                        y.setText("");
                        pos[ChessBoard.COLOR_YELLOW]=-1;
                        break;
                }
                me=ChessBoard.COLOR_BLUE;
                pos[ChessBoard.COLOR_BLUE]=1;
                b.setText("Me");
            }
        });

        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_YELLOW]!=-1)
                    return;
                switch (me){
                    case ChessBoard.COLOR_BLUE:
                        b.setText("");
                        pos[ChessBoard.COLOR_BLUE]=-1;
                        break;
                    case ChessBoard.COLOR_GREEN:
                        g.setText("");
                        pos[ChessBoard.COLOR_GREEN]=-1;
                        break;
                    case ChessBoard.COLOR_RED:
                        r.setText("");
                        pos[ChessBoard.COLOR_RED]=-1;
                        break;
                }
                me=ChessBoard.COLOR_YELLOW;
                pos[ChessBoard.COLOR_YELLOW]=1;
                y.setText("Me");
            }
        });

        jr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_RED]==-1)
                {
                    r.setText("robot");
                    pos[ChessBoard.COLOR_RED]=0;
                    jr.setText("-");
                }
                else if(pos[ChessBoard.COLOR_RED]==0)
                {
                    jr.setText("+");
                    r.setText("");
                    pos[ChessBoard.COLOR_RED]=-1;
                }
            }
        });

        jg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_GREEN]==-1)
                {
                    g.setText("robot");
                    pos[ChessBoard.COLOR_GREEN]=0;
                    jg.setText("-");
                }
                else if(pos[ChessBoard.COLOR_GREEN]==0)
                {
                    g.setText("");
                    pos[ChessBoard.COLOR_GREEN]=-1;
                    jg.setText("+");
                }
            }
        });

        jb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_BLUE]==-1)
                {
                    b.setText("robot");
                    pos[ChessBoard.COLOR_BLUE]=0;
                    jb.setText("-");
                }
                else if(pos[ChessBoard.COLOR_BLUE]==0)
                {
                    b.setText("");
                    pos[ChessBoard.COLOR_BLUE]=-1;
                    jb.setText("+");
                }
            }
        });

        jy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos[ChessBoard.COLOR_YELLOW]==-1)
                {
                    y.setText("robot");
                    pos[ChessBoard.COLOR_YELLOW]=0;
                    jy.setText("-");
                }
                else if(pos[ChessBoard.COLOR_YELLOW]==0)
                {
                    y.setText("");
                    pos[ChessBoard.COLOR_YELLOW]=-1;
                    jy.setText("+");
                }
            }
        });
        //internet init
        if(Game.getDataManager().getGameMode()==DataManager.GM_WLAN){
            Game.getSocketManager().registerActivity(DataPack.ROOM_SELECT_POSITION,this);
            Game.getSocketManager().registerActivity(DataPack.GAME_START,this);
            Game.getSocketManager().registerActivity(DataPack.ROOM_USER_ENTERED,this);
            Game.getSocketManager().registerActivity(DataPack.ROOM_USER_LEFT,this);
            Game.getSocketManager().registerActivity(DataPack.ROOM_USER_PICK_POSITION,this);
        }
        //setting
        me = -1;
        pos[0] = -1;
        pos[1] = -1;
        pos[2] = -1;
        pos[3] = -1;
        HashMap<String,String> map=new HashMap<>();
        map.put("name","name");
        map.put("score","score");
        playerList.addLast(map);
        if(Game.getDataManager().getGameMode()==DataManager.GM_WLAN){
            for(int i=0;i<Game.getDataManager().getPlayerNumber();i++){//添加玩家到指定的位置
                if(Game.getDataManager().getOnlinePos()[i]==-1){
                    map=new HashMap<>();
                    map.put("name",Game.getDataManager().getOnlineNames()[i]);
                    map.put("score",Game.getDataManager().getOnlineScores()[i]);
                    playerList.addLast(map);
                }
                else
                {
                    switch (Game.getDataManager().getOnlinePos()[i]) {
                        case 0:
                            r.setText(Game.getDataManager().getOnlineNames()[i]);
                            if (Game.getDataManager().getOnlineIds()[i].compareTo("-1") == 0)
                            {
                                pos[ChessBoard.COLOR_RED] = 0;
                                jr.setText("-");
                            }
                            else{
                                pos[ChessBoard.COLOR_RED]=1;
                            }
                            break;
                        case 1:
                            g.setText(Game.getDataManager().getOnlineNames()[i]);
                            if(Game.getDataManager().getOnlineIds()[i].compareTo("-1")==0)
                            {
                                pos[ChessBoard.COLOR_GREEN]=0;
                                jg.setText("-");
                            }
                            else{
                                pos[ChessBoard.COLOR_GREEN]=1;
                            }
                            break;
                        case 2:
                            b.setText(Game.getDataManager().getOnlineNames()[i]);
                            if(Game.getDataManager().getOnlineIds()[i].compareTo("-1")==0)
                            {
                                pos[ChessBoard.COLOR_BLUE]=0;
                                jb.setText("-");

                            }
                            else{
                                pos[ChessBoard.COLOR_BLUE]=1;
                            }
                            break;
                        case 3:
                            y.setText(Game.getDataManager().getOnlineNames()[i]);
                            if(Game.getDataManager().getOnlineIds()[i].compareTo("-1")==0)
                            {
                                pos[ChessBoard.COLOR_YELLOW]=0;
                                jy.setText("-");
                            }
                            else{
                                pos[ChessBoard.COLOR_YELLOW]=1;
                            }
                            break;
                    }
                }
            }
        }
        else if(Game.getDataManager().getGameMode()==DataManager.GM_LOCAL){
            map=new HashMap<>();
            map.put("name","ME");
            map.put("score",String.format("%d",Game.getDataManager().getOnlineScore()));
            playerList.addLast(map);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                LinkedList<String> msgs=new LinkedList<>();
                msgs.addLast(Game.getDataManager().getId());
                msgs.addLast(Game.getDataManager().getRoomId());
                DataPack dataPack = new DataPack(DataPack.ROOM_EXIT,msgs);
                Game.getSocketManager().send(dataPack);
                Game.getGameManager().gameOver();
                startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        if(dataPack.getCommand()==DataPack.ROOM_USER_ENTERED){
            if(dataPack.getMessage(0).compareTo("-1")==0){//加入了机器人
                int p = Integer.valueOf(dataPack.getMessage(3));
                pos[p]=0;
                switch (p){
                    case 0:
                        r.setText("ROBOT");
                        jr.setText("-");
                        break;
                    case 1:
                        g.setText("ROBOT");
                        jg.setText("-");
                        break;
                    case 2:
                        b.setText("ROBOT");
                        jb.setText("-");
                        break;
                    case 3:
                        y.setText("ROBOT");
                        jy.setText("-");
                        break;
                }

            }
            else{//加入了玩家
                HashMap<String,String> map=new HashMap<>();
                map.put("name",dataPack.getMessage(1));
                map.put("score",dataPack.getMessage(2));
                playerList.addLast(map);
                r.post(new Runnable() {
                    @Override
                    public void run() {
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.ROOM_USER_LEFT){
            if(dataPack.getMessage(0).compareTo("-1")==0){//删除机器人

            }
            else{//玩家离开
                if(dataPack.getMessage(0).compareTo(Game.getDataManager().getOnlineIds()[0])==0)//是房主
                {
                    LinkedList<String> msgs=new LinkedList<>();
                    msgs.addLast(Game.getDataManager().getId());
                    msgs.addLast(Game.getDataManager().getRoomId());
                    DataPack dataPack2 = new DataPack(DataPack.ROOM_EXIT,msgs);
                    Game.getSocketManager().send(dataPack2);
                    Game.getDataManager().setMyColor(-1);
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

                }
            }
        }
        else if(dataPack.getCommand()==DataPack.ROOM_USER_PICK_POSITION){

        }
        else if(dataPack.getCommand()==DataPack.ROOM_SELECT_POSITION){

        }
        else if(dataPack.getCommand()==DataPack.GAME_START){

        }
    }
}
