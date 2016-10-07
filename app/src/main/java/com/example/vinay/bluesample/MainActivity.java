package com.example.vinay.bluesample;
import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vinay.bluesample.R;

public class MainActivity extends AppCompatActivity {

    static int i=0;
    private float x1,x2;
    final Handler handler = new Handler();
    Runnable myRunnable;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView img2 = (ImageView) findViewById(R.id.imageView);


       myRunnable =new Runnable() {
            @Override
            public void run() {
                start_my_Animation();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(myRunnable, 500);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(MainActivity.this, DiscoveringActivity.class);
                startActivity(i);

                // close this activity
               // finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(myRunnable);

    }

    public void start_my_Animation(){

        final View myView = findViewById(R.id.revealview);
        final View myView2 = findViewById(R.id.imageView);
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;
        int cx2 = myView2.getWidth() / 2;
        int cy2 = myView2.getHeight() / 2;

        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
        int finalRadius2 = Math.max(myView2.getWidth(), myView2.getHeight());
        final Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 251f, finalRadius-1100);
        final Animator anim2 = ViewAnimationUtils.createCircularReveal(myView2, cx2, cy2, 250f, finalRadius-1100);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim2.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(2000);
        anim2.setDuration(2000);

        myView.setVisibility(View.VISIBLE);
        anim2.start();
        myView2.setVisibility(View.VISIBLE);
        anim.start();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)x1=event.getX();
        if(event.getAction()==MotionEvent.ACTION_UP){
            x2=event.getX();
        }
      /*  if(x1>x2)
        {  Intent i=new Intent(MainActivity.this,TrackActivity.class);
            startActivity(i);
        }*/
        if(x1<x2)
        {
            Intent i=new Intent(MainActivity.this,DiscoveringActivity.class);
            startActivity(i);
        }
        return super.onTouchEvent(event);
    }
}