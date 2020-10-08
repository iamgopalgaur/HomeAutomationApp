package com.example.led;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Animation animation1 = AnimationUtils.loadAnimation(this , R.anim.anim1);
        animation1.setInterpolator(new AccelerateInterpolator());
        animation1.setRepeatCount(Animation.INFINITE);
        animation1.setInterpolator(this, android.R.anim.accelerate_interpolator);
        animation1.setDuration(1000);

        final TextView splash = (TextView)findViewById(R.id.t1);
        splash.startAnimation(animation1);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);


                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
