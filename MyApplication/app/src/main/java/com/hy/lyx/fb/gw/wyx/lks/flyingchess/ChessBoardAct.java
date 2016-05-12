package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class ChessBoardAct extends AppCompatActivity {
    Button pauseButton,throwDiceButton;
    Button[][] plane;
    int boardWidth;
    Handler handler;
    ImageView map;
    float dx;
    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Game.activityManager.add(this);
        Game.soundManager.playMusic(SoundManager.GAME);
        //init
        pauseButton=(Button)findViewById(R.id.pause);
        throwDiceButton=(Button)findViewById(R.id.dice);
        plane=new Button[4][4];
        plane[0][0]=(Button)findViewById(R.id.R1);
        plane[0][1]=(Button)findViewById(R.id.R2);
        plane[0][2]=(Button)findViewById(R.id.R3);
        plane[0][3]=(Button)findViewById(R.id.R4);

        plane[1][0]=(Button)findViewById(R.id.G1);
        plane[1][1]=(Button)findViewById(R.id.G2);
        plane[1][2]=(Button)findViewById(R.id.G3);
        plane[1][3]=(Button)findViewById(R.id.G4);

        plane[2][0]=(Button)findViewById(R.id.B1);
        plane[2][1]=(Button)findViewById(R.id.B2);
        plane[2][2]=(Button)findViewById(R.id.B3);
        plane[2][3]=(Button)findViewById(R.id.B4);

        plane[3][0]=(Button)findViewById(R.id.Y1);
        plane[3][1]=(Button)findViewById(R.id.Y2);
        plane[3][2]=(Button)findViewById(R.id.Y3);
        plane[3][3]=(Button)findViewById(R.id.Y4);

        handler=new MyHandler(this);
        map=(ImageView)findViewById(R.id.map);
        //set data
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        boardWidth=dm.heightPixels;
        n=19;
        dx=boardWidth/n+0.8f;
        BitmapFactory.Options opt= new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.outWidth=dm.heightPixels;
        opt.outHeight=dm.heightPixels;
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.map);
        map.setImageBitmap(BitmapFactory.decodeStream(is,null,opt));
        //trigger
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PauseAct.class));
            }
        });
        throwDiceButton.setOnClickListener(new View.OnClickListener() {//throw dice
            @Override
            public void onClick(View v) {
                Game.playersData.get(Game.dataManager.getMyId()).setDiceValid(0);
            }
        });
        /////////////////
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                plane[i][j].setOnClickListener(new myOnClickListener(i,j));
            }
        }
        ///setting
        moveTo(plane[0][0],1,n-4);
        moveTo(plane[0][1],3,n-4);
        moveTo(plane[0][2],1,n-2);
        moveTo(plane[0][3],3,n-2);

        moveTo(plane[1][0],n-4,n-4);
        moveTo(plane[1][1],n-2,n-4);
        moveTo(plane[1][2],n-4,n-2);
        moveTo(plane[1][3],n-2,n-2);

        moveTo(plane[2][0],n-4,1);
        moveTo(plane[2][1],n-2,1);
        moveTo(plane[2][2],n-4,3);
        moveTo(plane[2][3],n-2,3);

        moveTo(plane[3][0],1,1);
        moveTo(plane[3][1],3,1);
        moveTo(plane[3][2],1,3);
        moveTo(plane[3][3],3,3);

        plane[0][0].setVisibility(View.INVISIBLE);
        plane[0][1].setVisibility(View.INVISIBLE);
        plane[0][2].setVisibility(View.INVISIBLE);
        plane[0][3].setVisibility(View.INVISIBLE);
        plane[1][0].setVisibility(View.INVISIBLE);
        plane[1][1].setVisibility(View.INVISIBLE);
        plane[1][2].setVisibility(View.INVISIBLE);
        plane[1][3].setVisibility(View.INVISIBLE);
        plane[2][0].setVisibility(View.INVISIBLE);
        plane[2][1].setVisibility(View.INVISIBLE);
        plane[2][2].setVisibility(View.INVISIBLE);
        plane[2][3].setVisibility(View.INVISIBLE);
        plane[3][0].setVisibility(View.INVISIBLE);
        plane[3][1].setVisibility(View.INVISIBLE);
        plane[3][2].setVisibility(View.INVISIBLE);
        plane[3][3].setVisibility(View.INVISIBLE);

        for(String key:Game.playersData.keySet()){
            plane[Game.playersData.get(key).color][0].setVisibility(View.VISIBLE);
            plane[Game.playersData.get(key).color][1].setVisibility(View.VISIBLE);
            plane[Game.playersData.get(key).color][2].setVisibility(View.VISIBLE);
            plane[Game.playersData.get(key).color][3].setVisibility(View.VISIBLE);
        }
        Game.gameManager.newTurn(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        Game.soundManager.pauseMusic();
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

    public void exit(){
        Game.socketManager.send(DataPack.R_GAME_EXIT,Game.dataManager.getMyId(),Game.dataManager.getRoomId());
        Game.gameManager.gameOver();
        if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
            startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
        }
        else{
            startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
        }
        Game.dataManager.giveUp(false);
        Game.soundManager.playMusic(SoundManager.BACKGROUND);
    }

    public void moveTo(Button plane,int x,int y){
        plane.setX(x*dx);
        plane.setY(y*dx);
    }

    public void animMoveTo(Button plane, int x, int y) {
        plane.animate().setDuration(500);
        plane.animate().translationX(x * dx);
        plane.animate().translationY(y * dx);
    }
}


class MyHandler extends Handler{
    ChessBoardAct parent;
    public MyHandler(ChessBoardAct parent){
        this.parent=parent;
    }
    @Override
    public void handleMessage(Message msg){//事件回掉
        switch (msg.what){
            case 1://飞机
            {
                int color=msg.getData().getInt("color");
                int whichPlane=msg.getData().getInt("whichPlane");
                int pos=msg.getData().getInt("pos");
                if(pos!=-2){
                    parent.animMoveTo(parent.plane[color][whichPlane], Game.chessBoard.map[color][pos][0], Game.chessBoard.map[color][pos][1]);
                }
                else{//到达
                    parent.animMoveTo(parent.plane[color][whichPlane], Game.chessBoard.map[color][55][0], Game.chessBoard.map[color][55][1]);
                    Game.soundManager.playSound(SoundManager.ARRIVE);
                }
            }
            break;
            case 2://骰子
                parent.throwDiceButton.setText(msg.getData().getString("dice"));
                break;
            case 3://显示消息
                Toast.makeText(parent.getApplicationContext(),msg.getData().getString("msg"),Toast.LENGTH_SHORT).show();
                break;
            case 4: // crash
            {
                int color=msg.getData().getInt("color");
                int whichPlane=msg.getData().getInt("whichPlane");
                parent.animMoveTo(parent.plane[color][whichPlane],Game.chessBoard.mapStart[color][whichPlane][0],Game.chessBoard.mapStart[color][whichPlane][1]);
            }
            break;
            case 5://finished
            {
                Intent intent = new Intent(parent.getApplicationContext(), RoomAct.class);
                ArrayList<String> msgs=new ArrayList<>();
                if(Game.dataManager.getGameMode()==DataManager.GM_LOCAL){
                    if(Game.dataManager.getLastWinner().compareTo(Game.dataManager.getMyId())==0)//更新分数
                        Game.dataManager.setScore(Game.dataManager.getScore()+10);
                    else
                        Game.dataManager.setScore(Game.dataManager.getScore()-5);

                    Game.dataManager.saveData();
                    msgs.add(Game.dataManager.getMyId());
                    msgs.add(Game.playersData.get(Game.dataManager.getMyId()).name);
                    msgs.add(String.valueOf(Game.dataManager.getScore()));
                    msgs.add("-1");
                }
                else if(Game.dataManager.getGameMode()==DataManager.GM_WLAN){
                    for(String key:Game.playersData.keySet()){//更新玩家的分数
                        if(Game.playersData.get(key).offline==false) {
                            if (Game.playersData.get(key).id.compareTo(Game.dataManager.getLastWinner()) == 0) {
                                Game.playersData.get(key).score = String.valueOf(Integer.valueOf(Game.playersData.get(key).score) + 10);
                            } else {
                                Game.playersData.get(key).score = String.valueOf(Integer.valueOf(Game.playersData.get(key).score) - 5);
                            }
                        }
                    }

                    Game.dataManager.setOnlineScore(Game.playersData.get(Game.dataManager.getMyId()).score);

                    msgs.add(Game.playersData.get(Game.dataManager.getHostId()).id);
                    msgs.add(Game.playersData.get(Game.dataManager.getHostId()).name);
                    msgs.add(Game.playersData.get(Game.dataManager.getHostId()).score);
                    msgs.add("-1");
                    for(String key:Game.playersData.keySet()){
                        Game.playersData.get(key).color=-1;
                        if(Game.dataManager.getHostId().compareTo(Game.playersData.get(key).id)!=0&&Integer.valueOf(Game.playersData.get(key).id)>=0&&Game.playersData.get(key).offline==false){
                            msgs.add(Game.playersData.get(key).id);
                            msgs.add(Game.playersData.get(key).name);
                            msgs.add(Game.playersData.get(key).score);
                            msgs.add("-1");
                        }
                    }
                }
                intent.putStringArrayListExtra("msgs",msgs);
                parent.startActivity(intent);
                Intent intent2 = new Intent(parent.getApplicationContext(),GameEndAct.class);
                intent2.putStringArrayListExtra("msgs",msgs);
                parent.startActivity(intent2);
                Game.dataManager.giveUp(false);
                Game.gameManager.gameOver();
                Game.soundManager.playMusic(SoundManager.BACKGROUND);
            }
            default:
                super.handleMessage(msg);
        }
    }
}

class myOnClickListener implements View.OnClickListener{
    int color,which;
    public myOnClickListener(int color,int which){
        this.color=color;
        this.which=which;
    }
    @Override
    public void onClick(View v) {
        if (Game.playersData.get(Game.dataManager.getMyId()).color==color)
            Game.playersData.get(Game.dataManager.getMyId()).setPlaneValid(which);
    }
}