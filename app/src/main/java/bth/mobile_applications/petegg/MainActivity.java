package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.CountDownTimer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    long id;

    /**
     * reminder: onCreate = main-function of the Activity window
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        animateBackground();
        bouningEgg();
        SQLQuerys querys = new SQLQuerys();
        id = querys.getIdLastLivedEgg(this);
        Boolean eggStatus = false;
        if(id != 0L){
            eggStatus = checkEggStatusDB();
        }
        configureHomeButton(eggStatus);
        changeStats(this);
    }



    private final Boolean checkEggStatusDB() {
        Boolean eggAlive = true;

        //calculate if the egg has survived till current date!


        return eggAlive;
    }


    /**
     * Switches to the next Activity
     */
    private void configureHomeButton(final Boolean eggAlive){
        Button continueToHome = (Button) findViewById(R.id.continueToHome);

        continueToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eggAlive == true) {
                    HomeScreen.setId(id);
                    startActivity(new Intent(MainActivity.this, HomeScreen.class));
                }else{
                    startActivity(new Intent(MainActivity.this, NameEgg.class));
                }
            }
        });
    }

    /**
     * Moves the backgroundimages like the pet is walking through woods
     */
    private void animateBackground(){
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
    private void bouningEgg(){
        final ImageView bouncingEgg = (ImageView) findViewById(R.id.bouncingEgg);

        float f[] = new float[]{0f, 100f, 0f};

        final ObjectAnimator animator = ObjectAnimator.ofFloat(bouncingEgg,"translationY",f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000L);
        animator.start();
    }

    public void changeStats(final Context activity){
        changeStatsOnStart();
        int z = 60*30;
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
                changeHappyness();
                changeHunger();
                changeHealth();
                int time = (int)System.currentTimeMillis();
                SQLQuerys.saveIntToDB(id, activity, "lastlogin", time);
                Log.i("TestStats", "time " + time);
            }
        };

        timer.schedule(timeTask, 0l, 1000*z);
    }

    private void changeHealth(){
        int health = SQLQuerys.loadIntFromDatabase(id, this, "health");
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        int hunger = SQLQuerys.loadIntFromDatabase(id, this, "hunger");
        if(health != 100){
            if(hunger > 90 && happyness > 90){
                health += 4;
            }
            if(health > 100){
                health = 100;
            }
        }

        if(hunger < 10){
            health -= 2;
        }
        if(happyness < 10){
            health -= 2;
        }

        if(health < 0){
            health = 0;
        }

        SQLQuerys.saveIntToDB(id,this, "health", health);
        Log.i("TestStats", "health " + health);
    }

    private void changeHappyness(){
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        happyness -= 2;
        if(happyness < 0){
            happyness = 0;
        }
        if(happyness > 100){
            happyness = 100;
        }
        SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
        Log.i("TestStats", "happyness " + happyness);
    }

    private void changeHunger(){
        int hunger = SQLQuerys.loadIntFromDatabase(id, this, "hunger");
        hunger -= 2;
        if(hunger < 0){
            hunger = 0;
        }
        if(hunger > 100){
            hunger = 100;
        }
        SQLQuerys.saveIntToDB(id,this, "hunger", hunger);
        Log.i("TestStats", "hunger " + hunger);
    }

    private void changeStatsOnStart(){
        int lastlogin = SQLQuerys.loadIntFromDatabase(id, this, "lastlogin");

        int time = (int)System.currentTimeMillis();
        int minutes = time/60000;
        // every 30min
        int z = 30;
        z = 1; //to test
        for(int i = 0; i < (minutes); i += z){
            Log.i("TestStats", "For-Loop");
            changeHappyness();
            changeHunger();
            changeHealth();
            int endtime = (int)System.currentTimeMillis();
            SQLQuerys.saveIntToDB(id,this, "lastlogin", endtime);
        }

    }
}
