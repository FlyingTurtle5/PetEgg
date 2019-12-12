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
import android.location.Criteria;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Outside extends AppCompatActivity {

    Location lastLocation = null;
    int allSteps = 0;
    int restSteps = 0;

    String bestProvider;
    Criteria criteria;
    LocationManager locManager;
    MyLocationListener myLocListener;


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

        //Location
        myLocListener = new MyLocationListener(this);
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        bestProvider = locManager.getBestProvider(criteria, false);

        try {
            locManager.requestLocationUpdates(bestProvider, 6000, 15, myLocListener);
        } catch (SecurityException unlikely) {
            Log.e("Request", "Lost location permission. Could not request updates. ");
        }

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

    /**
     * Increases Happyness
     */
    private void increaseHappyness(){
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        int stepCost = 100;
        if(MainActivity.devMode){
            stepCost = 25;
        }
        while(restSteps > stepCost){
            restSteps -= stepCost;
            happyness += 10;
            Log.i("TestSteps", "Added Happyness ");
        }
        SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
    }

    /**
     * Calculates the steps taken from last Locationchange.
     * @param newLocation
     */
    public void stepCounter(Location newLocation){
        int distance = 0;
        //Log.i("TestSteps", "NewLocation " + lastLocation);
        if(lastLocation == null){
            lastLocation = newLocation;
            distance = 0;
        }else{
            Log.i("TestSteps", "NewLocation " + newLocation);
            distance = (int)newLocation.distanceTo(lastLocation); // in meters
            lastLocation = newLocation;
        }

        //Log.i("TestSteps", "Steps " + distance/2);
        allSteps += distance/2;
        restSteps += distance/2;

        TextView happy = (TextView) findViewById(R.id.steps);
        happy.setText("Steps: " + allSteps);

        increaseHappyness();

    }

}
