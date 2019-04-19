package com.nepalpolice.circle073;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;




/**
 * Created by Sagar on 2017/09/25.
 */

public class Splash extends Activity {


    ProgressBar pbar;
    private Activity context;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            launchHome();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }
    @Override
    public void onStart() {
        launchHome();
        super.onStart();

    }

    public void launchHome() {


            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);


                    }
                }
            };
            timerThread.start();
        }



    }




