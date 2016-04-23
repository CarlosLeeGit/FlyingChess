package com.hy.lyx.fb.gw.wyx.lks.flyingchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;


public class LoginAct extends AppCompatActivity {
    Button login,home,signin;
    EditText name,pw,pw2;
    boolean bSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Activity切换动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init
        login = (Button)findViewById(R.id.loginButton);
        name=(EditText)findViewById(R.id.name);
        pw = (EditText)findViewById(R.id.pw);
        signin=(Button)findViewById(R.id.signin);
        home=(Button)findViewById(R.id.home);
        pw2=(EditText)findViewById(R.id.pw2);
        bSignin=false;
        name.setY(name.getY()+50);
        pw.setY(pw.getY()+70);
        //trigger
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"please input you name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"please input you password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bSignin){// sign in
                    if(pw2.getText().length()==0){
                        Toast.makeText(getApplicationContext(),"please confirm you password",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pw2.getText().toString().compareTo(pw2.getText().toString())!=0){
                        Toast.makeText(getApplicationContext(),"password you input is not same",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{// login

                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bSignin){
                    login.setText("Login");
                    signin.setText("Signin");
                    bSignin=false;
                    pw2.setVisibility(View.INVISIBLE);
                    name.setY(name.getY()+50);
                    pw.setY(pw.getY()+70);
                    pw2.setText("");
                }
                else{
                    name.setText("");
                    pw.setText("");
                    pw2.setText("");
                    pw2.setVisibility(View.VISIBLE);
                    name.setY(name.getY()-50);
                    pw.setY(pw.getY()-70);
                    login.setText("Signin");
                    signin.setText("Back");
                    bSignin=true;
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChooseModeAct.class));
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        pw2.setVisibility(View.INVISIBLE);
        login.setText("Login");
        signin.setText("Signin");
        pw2.setText("");
        if(bSignin){
            name.setY(name.getY()+50);
            pw.setY(pw.getY()+70);
            bSignin=false;
        }
    }
}
