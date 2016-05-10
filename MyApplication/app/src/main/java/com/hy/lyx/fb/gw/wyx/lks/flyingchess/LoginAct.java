package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
    EditText _myName,_pw,_pw2;
    TextInputLayout myName,pw,pw2;
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
        myName=(TextInputLayout)findViewById(R.id.myName);
        _myName=(EditText)findViewById(R.id._myName);
        pw = (TextInputLayout)findViewById(R.id.pw);
        _pw=(EditText)findViewById(R.id._pw);
        register =(Button)findViewById(R.id.register);
        home=(Button)findViewById(R.id.home);
        pw2=(TextInputLayout)findViewById(R.id.pw2);
        _pw2=(EditText)findViewById(R.id._pw2);
        bRegister =false;
        //trigger
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.soundManager.playSound(SoundManager.BUTTON);
                if(_myName.getText().length()==0){
                    _myName.setError("Please input you name");
                    _myName.requestFocus();
                    return;
                }
                if(_pw.getText().length()==0){
                    _pw.setError("Please input you password");
                    _pw.requestFocus();
                    return;
                }
                if(bRegister){// sign in
                    if(_pw2.getText().length()==0){
                        _pw2.setError("Please confirm you password");
                        _pw2.requestFocus();
                        return;
                    }
                    if(_pw2.getText().toString().compareTo(_pw2.getText().toString())!=0){
                        _pw2.setError("Password you input is not same");
                        _pw2.requestFocus();
                        return;
                    }
                    //sign in
                    Game.socketManager.send(DataPack.R_REGISTER,_myName.getText().toString(),_pw.getText().toString());
                    Intent intent = new Intent(getApplicationContext(),WaitingAct.class);
                    intent.putExtra("tipe","Register..");
                    startActivity(intent);
                }
                else{// login
                    Game.socketManager.send(DataPack.R_LOGIN,_myName.getText().toString(),_pw.getText().toString());
                    Intent intent = new Intent(getApplicationContext(),WaitingAct.class);
                    intent.putExtra("tipe","login..");
                    startActivity(intent);
                    Game.dataManager.setMyName(_myName.getText().toString());
                    Game.dataManager.setPassword(_pw.getText().toString());
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.soundManager.playSound(SoundManager.BUTTON);
                if(bRegister){
                    login.setText("Login");
                    register.setText("Register");
                    bRegister =false;
                    pw2.setVisibility(View.GONE);
                    _pw2.setText("");
                }
                else{
                    _myName.setText("");
                    _pw.setText("");
                    _pw2.setText("");
                    pw2.setVisibility(View.VISIBLE);
                    login.setText("Register");
                    register.setText("Back");
                    _myName.requestFocus();
                    bRegister =true;
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.soundManager.playSound(SoundManager.BACK);
                startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
            }
        });
        Game.socketManager.registerActivity(DataPack.A_LOGIN,this);
        Game.socketManager.registerActivity(DataPack.A_REGISTER,this);
        //setting
        pw2.setVisibility(View.GONE);
        if(Game.dataManager.autoLogin()){
            _myName.setText(Game.dataManager.getMyName());
            _pw.setText(Game.dataManager.getPassword());
        }
    }

    @Override
    public void processDataPack(DataPack dataPack) {
        if(dataPack.getCommand()== DataPack.A_LOGIN){
            if(dataPack.isSuccessful()){
                Game.dataManager.setMyId(dataPack.getMessage(0));
                Game.dataManager.setOnlineScore(dataPack.getMessage(1));
                startActivity(new Intent(getApplicationContext(),GameInfoAct.class));
                Game.dataManager.setAutoLogin(true);
            }
            else{
                myName.post(new Runnable() {
                    @Override
                    public void run() {
                        Game.activityManager.back();
                        Toast.makeText(getApplicationContext(),"Login failed!",Toast.LENGTH_SHORT).show();
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
                        pw2.setVisibility(View.GONE);
                        _pw2.setText("");
                        Toast.makeText(getApplicationContext(),"Register successful!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                myName.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Register failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Game.activityManager.back();
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
