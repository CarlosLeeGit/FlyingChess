package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;


public class LoginAct extends AppCompatActivity implements Target {
    Button login,home,register;
    EditText myName,pw,pw2;
    boolean bRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Game.activityManager.add(this);
        //init
        login = (Button)findViewById(R.id.loginButton);
        myName=(EditText)findViewById(R.id.name);
        pw = (EditText)findViewById(R.id.pw);
        register =(Button)findViewById(R.id.register);
        home=(Button)findViewById(R.id.home);
        pw2=(EditText)findViewById(R.id.pw2);
        bRegister =false;
        myName.setY(myName.getY()+50);
        pw.setY(pw.getY()+70);
        //trigger
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myName.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"please input you name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"please input you password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bRegister){// sign in
                    if(pw2.getText().length()==0){
                        Toast.makeText(getApplicationContext(),"please confirm you password",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pw2.getText().toString().compareTo(pw2.getText().toString())!=0){
                        Toast.makeText(getApplicationContext(),"password you input is not same",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //sign in
                    LinkedList<String> msgList=new LinkedList<>();
                    msgList.addLast(myName.getText().toString());
                    msgList.addLast(pw.getText().toString());
                    DataPack dataPack=new DataPack(DataPack.R_REGISTER,msgList);
                    Game.socketManager.send(dataPack);
                }
                else{// login
                    LinkedList<String> msgList=new LinkedList<String>();
                    msgList.addLast(myName.getText().toString());
                    msgList.addLast(pw.getText().toString());
                    DataPack dataPack=new DataPack(DataPack.R_LOGIN,msgList);
                    Game.socketManager.send(dataPack);
                    Game.dataManager.setMyName(myName.getText().toString());
                    Game.dataManager.setPassword(pw.getText().toString());
                    Game.playerMapData.get("me").name=myName.getText().toString();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bRegister){
                    login.setText("Login");
                    register.setText("Register");
                    bRegister =false;
                    pw2.setVisibility(View.INVISIBLE);
                    myName.setY(myName.getY()+50);
                    pw.setY(pw.getY()+70);
                    pw2.setText("");
                }
                else{
                    myName.setText("");
                    pw.setText("");
                    pw2.setText("");
                    pw2.setVisibility(View.VISIBLE);
                    myName.setY(myName.getY()-50);
                    pw.setY(pw.getY()-70);
                    login.setText("Register");
                    register.setText("Back");
                    bRegister =true;
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
            }
        });
        Game.socketManager.registerActivity(DataPack.A_LOGIN,this);
        Game.socketManager.registerActivity(DataPack.A_REGISTER,this);
        //setting
        pw2.setVisibility(View.INVISIBLE);
        if(Game.dataManager.autoLogin()){
            myName.setText(Game.dataManager.getMyName());
            pw.setText(Game.dataManager.getPassword());
        }
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        if(dataPack.getCommand()== DataPack.A_LOGIN){
            if(dataPack.isSuccessful()){
                Game.playerMapData.get("me").id=dataPack.getMessage(0);
                Game.playerMapData.get("me").score=dataPack.getMessage(1);
                startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
                Game.dataManager.setAutoLogin(true);
            }
            else{
                myName.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"login failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(dataPack.getCommand()==DataPack.A_REGISTER){
            if(dataPack.isSuccessful()){
                myName.post(new Runnable() {
                    @Override
                    public void run() {
                        login.setText("Login");
                        register.setText("Register");
                        bRegister =false;
                        pw2.setVisibility(View.INVISIBLE);
                        myName.setY(myName.getY()+50);
                        pw.setY(pw.getY()+70);
                        pw2.setText("");
                        Toast.makeText(getApplicationContext(),"register successful!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                myName.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"register failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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
}
