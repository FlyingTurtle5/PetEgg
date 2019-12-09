package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import android.location.Location;

import java.util.Timer;
import java.util.TimerTask;

public class Outside extends AppCompatActivity {

    Location lastLocation = null;
    int allSteps = 0;
    int restSteps = 0;

    static long id;

    public static void setId(long id) {
        Outside.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside);

        animateBackground();
        bouningEgg();
        configureHomeButton();
        updateCounter(this);
    }

    /**
     * Switches to the next Activity
     */
    private void configureHomeButton() {
        Button continueToHome = (Button) findViewById(R.id.continueToHome);

        continueToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Outside.this, HomeScreen.class));
                //finish(); //?
            }
        });
    }

    /**
     * Moves the backgroundimages like the pet is walking through woods
     */
    private void animateBackground() {
        final ImageView backgroundOne = (ImageView) findViewById(R.id.backgroundTreeOne);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.backgroundTreeTwo);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();
    }

    /**
     * Lets our egg bounce!
     */
    private void bouningEgg() {
        final ImageView bouncingEgg = (ImageView) findViewById(R.id.outsidePet);

        float f[] = new float[]{0f, 50f, 0f};

        final ObjectAnimator animator = ObjectAnimator.ofFloat(bouncingEgg, "translationY", f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000L);
        animator.start();
    }

    private int stepCounter(){
        int distance = 0;
        Log.i("TestSteps", "NewLocation " + lastLocation);
        if(lastLocation == null){
           lastLocation = new Location(LocationManager.GPS_PROVIDER);
           distance = 0;
        }else{
            Location newLocation = new Location(LocationManager.GPS_PROVIDER);
            Log.i("TestSteps", "NewLocation " + newLocation);
            distance = (int)newLocation.distanceTo(lastLocation); // in meters
            lastLocation = newLocation;
        }

        Log.i("TestSteps", "Steps " + distance/2);
        return distance/2; //steps
    }

    private void increaseHappyness(int steps){
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");

        while(steps > 50){
            steps -= 50;
            happyness += 4;
        }
        SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
    }

    public void updateCounter(final Context activity){
        //check location every 3 minutes
        int z = 60*5;
        z = 60; //to test
        /*new CountDownTimer(1000*z, 1000) { //alle 30min

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                changeHappyness();
                changeHunger();
                changeHealth();
                int time = (int)System.currentTimeMillis();
                SQLQuerys.saveIntToDB(id, activity, "lastlogin", time);
            }
        }.start();*/
        Timer timer = new Timer ();
        TimerTask timeTask = new TimerTask() {
            @Override
            public void run () {
                int newSteps = stepCounter();
                allSteps += newSteps;
                Log.i("TestSteps", "AllSteps " + allSteps);
                restSteps += newSteps;
                increaseHappyness(restSteps);
            }
        };

        timer.schedule(timeTask, 0l, 1000*z);
    }

}
