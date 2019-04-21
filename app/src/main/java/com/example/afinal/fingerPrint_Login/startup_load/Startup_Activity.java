package com.example.afinal.fingerPrint_Login.startup_load;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.fingerprint_login.FingerPrint_LogIn_Final_Activity;

import java.util.Timer;
import java.util.TimerTask;

public class Startup_Activity extends AppCompatActivity {

    private Timer timer;
    private int countHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_);

        ConstraintLayout constraintLayout = findViewById(R.id.layoutStartupID);
        AnimationDrawable animationDrawable

                = (AnimationDrawable) constraintLayout.getBackground();

        countHere=0;

        animationDrawable.setEnterFadeDuration(1200);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countHere++;

                if(countHere==8){
                    timer.cancel();
                    Intent intent = new Intent(Startup_Activity.this, FingerPrint_LogIn_Final_Activity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },0,1200);

    }
}
