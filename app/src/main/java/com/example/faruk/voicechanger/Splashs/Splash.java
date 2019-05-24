package com.example.faruk.voicechanger.Splashs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.faruk.voicechanger.Activitys.MainActivity;
import com.example.faruk.voicechanger.R;

/**
 *** Faruk_Altay 11.11.2018. ***
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}