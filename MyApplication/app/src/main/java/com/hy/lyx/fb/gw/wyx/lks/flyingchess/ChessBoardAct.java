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

import java.io.InputStream;
import java.util.Timer;

public class ChessBoardAct extends AppCompatActivity {
    Timer closeTimer;
    Button pause,dice;
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
        //init
        closeTimer=new Timer();
        pause=(Button)findViewById(R.id.pause);
        dice=(Button)findViewById(R.id.dice);
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
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dice.setOnClickListener(new View.OnClickListener() {//throw dice
            @Override
            public void onClick(View v) {
                Game.getPlayer().setDiceValid();
            }
        });
        /////////////////add four plane trigger and when click a plane, we should call game manager :: choosePlane to choose plane
        plane[0][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_RED)
                    Game.getPlayer().setPlaneValid(0);
            }
        });
        plane[0][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_RED)
                    Game.getPlayer().setPlaneValid(1);
            }
        });
        plane[0][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_RED)
                    Game.getPlayer().setPlaneValid(2);
            }
        });
        plane[0][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_RED)
                    Game.getPlayer().setPlaneValid(3);
            }
        });

        plane[1][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_GREEN)
                    Game.getPlayer().setPlaneValid(0);
            }
        });
        plane[1][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_GREEN)
                    Game.getPlayer().setPlaneValid(1);
            }
        });
        plane[1][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_GREEN)
                    Game.getPlayer().setPlaneValid(2);
            }
        });
        plane[1][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_GREEN)
                    Game.getPlayer().setPlaneValid(3);
            }
        });

        plane[2][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_BLUE)
                    Game.getPlayer().setPlaneValid(0);
            }
        });
        plane[2][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_BLUE)
                    Game.getPlayer().setPlaneValid(1);
            }
        });
        plane[2][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_BLUE)
                    Game.getPlayer().setPlaneValid(2);
            }
        });
        plane[2][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_BLUE)
                    Game.getPlayer().setPlaneValid(3);
            }
        });

        plane[3][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_YELLOW)
                    Game.getPlayer().setPlaneValid(0);
            }
        });
        plane[3][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_YELLOW)
                    Game.getPlayer().setPlaneValid(1);
            }
        });
        plane[3][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_YELLOW)
                    Game.getPlayer().setPlaneValid(2);
            }
        });
        plane[3][3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Game.getDataManager().getMyColor()==ChessBoard.COLOR_YELLOW)
                    Game.getPlayer().setPlaneValid(3);
            }
        });
        Game.getGameManager().newTurn(this);
    }

    @Override
    public void onStart(){
        super.onStart();
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

        for(int i=0;i<4;i++){
            if(Game.getDataManager().getSiteState()[i]==-1)
            {
                plane[i][0].setVisibility(View.INVISIBLE);
                plane[i][1].setVisibility(View.INVISIBLE);
                plane[i][2].setVisibility(View.INVISIBLE);
                plane[i][3].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回按钮
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                Game.getGameManager().gameOver();
                startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onDestroy(){
        Game.getGameManager().gameOver();
        super.onDestroy();
    }

    public void moveTo(Button plane,int x,int y){
        plane.setX(x*dx);
        plane.setY(y*dx);
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
                    parent.moveTo(parent.plane[color][whichPlane],Game.getChessBoard().map[color][pos][0],Game.getChessBoard().map[color][pos][1]);
                }
                else{//消失
                    parent.moveTo(parent.plane[color][whichPlane],Game.getChessBoard().map[color][55][0],Game.getChessBoard().map[color][55][1]);
                }
            }
            break;
            case 2://筛子
                parent.dice.setText(msg.getData().getString("dice"));
                break;
            default:
                super.handleMessage(msg);
        }
    }
}