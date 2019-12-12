package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.LongToIntFunction;

public class HomeScreen extends AppCompatActivity {

    static long id;
    private LightSensor lightSensor;
    private long lastTime;
    private long firstTime;
    private long lastSwipe = 0;
    public static int changeHealth;
    public static boolean toChange;

    public static void setId(long id) {
        HomeScreen.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        SQLQuerys querys = new SQLQuerys();
        changeName(querys.loadStringFromDatabase(id, this, "petname"));
        changeStats(this);
        displayStats();
        lightSensor = new LightSensor((SensorManager) getSystemService(SENSOR_SERVICE), this);
        goOutside();
        goSettings();
        feed(this);
        calculateAge();

        if(toChange){
            SQLQuerys.saveIntToDB(id,this, "health", changeHealth);
            toChange = false;
        }

        //swipe
        ImageView view = (ImageView) findViewById(R.id.homescreenEgg);
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipe() {
                Log.i("TestSwipe", "Swiped");
                changeHappyness();
                //animationThread();
            }
        });

    }

    private void animationThread(){
        Runnable runnable = new Runnable(){
            public void run() {
                try {
                    setStarsVisible(true);
                    happyStars();
                    wait(3);
                    setStarsVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private void setStarsVisible(Boolean visible) {
        ImageView star = (ImageView) findViewById(R.id.happyStar);
        ImageView star1 = (ImageView) findViewById(R.id.happyStar1);
        ImageView star2 = (ImageView) findViewById(R.id.happyStar2);

        if(visible == true){
            star.setVisibility(View.VISIBLE);
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
        }else{
            star.setVisibility(View.INVISIBLE);
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
        }
    }

    private void happyStars(){
        final ImageView star = (ImageView)findViewById(R.id.happyStar);
        final ImageView star1 = (ImageView)findViewById(R.id.happyStar1);
        final ImageView star2 = (ImageView)findViewById(R.id.happyStar2);

        final ValueAnimator starrain = TimeAnimator.ofFloat(0.0f, 1.0f);
        starrain.setDuration(1000L);
        starrain.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = (float) animation.getAnimatedValue();
                        float width = star1.getWidth();
                        float translationX = width * progress;
                        star.setTranslationX(translationX);
                        star.setTranslationX(translationX - width);
                        float progress1 = (float) animation.getAnimatedValue();
                        float height = star1.getHeight();
                        float translationY = height * progress1;
                        star1.setTranslationY(translationY);
                        star1.setTranslationY(translationY - height);
                        star2.setTranslationX(translationX);
                        star2.setTranslationX(translationX + width);
                        star2.setTranslationY(translationY);
                        star2.setTranslationY(translationY + width);

                }
        });
        starrain.start();

    }

    /**
     * Updates the stats on the Homescreen
     */
    private void displayStats(){
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        if(happyness > 100){
            happyness = 100;
            SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
        }
        TextView happy = (TextView) findViewById(R.id.joy);
        happy.setText("Joy: " + happyness);

        int health = SQLQuerys.loadIntFromDatabase(id, this, "health");
        TextView h = (TextView) findViewById(R.id.health);
        h.setText("Health: " + health);

        int hunger = SQLQuerys.loadIntFromDatabase(id, this, "hunger");
        TextView hu = (TextView) findViewById(R.id.hunger);
        hu.setText("Saturation: " + hunger);

    }

    /**
     * Changes the window image depending on if the pet is sleeping or not
     * @param awake true=Pet is not sleeping
     */
    private void windowChange(boolean awake){
        ImageView window = (ImageView) findViewById(R.id.window);
        if(awake){
            window.setImageResource(R.drawable.window);
        }else{
            window.setImageResource(R.drawable.windownight);
        }
    }

    /**
     * Calculates the Age of the Pet
     */
    private void calculateAge(){
        int birthday = SQLQuerys.loadIntFromDatabase(id, this, "birthday");
        int currentTime = (int) System.currentTimeMillis();
        int ms = currentTime - birthday;
        int days = (int) (ms / (1000*60*60*24));
        Log.i("TestAge", "Days: " + days);
        int third = days / 100;
        int second = (days - 100*third) / 10;
        int first = days % 10;
        Log.i("TestAge", "" + third + " " + second + " " + first);
        ImageView ones = (ImageView) findViewById(R.id.ones);
        ImageView tens = (ImageView) findViewById(R.id.tens);
        ImageView hundreds = (ImageView) findViewById(R.id.hundreds);
        setNumberPic(ones, first);
        setNumberPic(tens, second);
        setNumberPic(hundreds, third);
    }

    /**
     * Finds the right pictures for the Age numbers
     * @param view The ImageView that has to change
     * @param number The number the ImageView has to change to
     */
    private void setNumberPic(ImageView view, int number){
        switch (number){
            case 0:
                view.setImageResource(R.drawable.number10);
                break;
            case 1:
                view.setImageResource(R.drawable.number1);
                break;
            case 2:
                view.setImageResource(R.drawable.number2);
                break;
            case 3:
                view.setImageResource(R.drawable.number3);
                break;
            case 4:
                view.setImageResource(R.drawable.number4);
                break;
            case 5:
                view.setImageResource(R.drawable.number5);
                break;
            case 6:
                view.setImageResource(R.drawable.number6);
                break;
            case 7:
                view.setImageResource(R.drawable.number7);
                break;
            case 8:
                view.setImageResource(R.drawable.number8);
                break;
            case 9:
                view.setImageResource(R.drawable.number9);
                break;
            default:
                view.setImageResource(R.drawable.number10);
        }
    }

    /**
     * Changes Name of the pet
     * @param name
     */
    private void changeName(String name){
        TextView nameField = (TextView) findViewById(R.id.nameOfPet);
        if(name != null) {
            nameField.setText(name);
        }else{
            nameField.setText("No name found in database.");
        }
    }

    /**
     * Goes to the Outside Activity
     */
    private void goOutside(){
        if(MainActivity.locOn) {
            ImageView goOutsideButton = (ImageView) findViewById(R.id.doorButton);

            goOutsideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeScreen.this, Outside.class));
                }
            });
        }
    }

    /**
     * Go to Menue Activity
     */
    private void goSettings() {
        ImageView saveAndContinue = (ImageView) findViewById(R.id.settingsButton);
        SettingsMenue.setId(id);

        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, SettingsMenue.class));
            }
        });
    }

    /**
     * Increases Happyness
     */
    private void changeHappyness(){
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        long currentSwipe = System.currentTimeMillis();

        if((currentSwipe - lastSwipe) < 6000*60*30 ){
            happyness += 3;
            if(happyness > 100){
                happyness = 100;
            }
            lastSwipe = currentSwipe;
        }

        SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
        displayStats();
    }

    /**
     * Feeds the pet
     * @param activity
     */
    private void feed(final Context activity){
        ImageView feedButton = (ImageView) findViewById(R.id.feedButton);

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLQuerys.saveIntToDB(id, activity, "hunger", 100);
                SQLQuerys.saveIntToDB(id, activity, "lastfed", (int) System.currentTimeMillis());
                displayStats();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        lightSensor.onResume();
        displayStats();
    }

    protected void onPause() {
        super.onPause();
        lightSensor.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("TestLight", "Pet is no longer sleeping");
        windowChange(true);
        super.onDestroy();
    }

    /**
     * When LightSensor picks up on change. Methode decides if Pet is sleeping or not.
     * @param values
     */
    protected void useLightSensor(float[] values){
        //Log.i("TestLight", "SensorChanged");
        float currentLux = values[0];
        if(currentLux > 100){
            lastTime = System.currentTimeMillis();
            //Log.i("TestLight", "Pet is no longer sleeping");
            windowChange(true);
        }else{
            if(lastTime == 0){
                lastTime = System.currentTimeMillis();
                firstTime = System.currentTimeMillis();
            }else{
                long currentTime = System.currentTimeMillis();
                int z = 5;
                if(MainActivity.devMode){
                    z = 2;
                }
                int happyness = 0;
                if((currentTime - firstTime) > 6000*60*z){
                    //Pet is sleeping (after z minutes)
                    //Log.i("TestLight", "Pet is sleeping");
                    windowChange(false);
                    long time = (currentTime - lastTime)/6000*60; //in minutes

                    //every z*3 minutes happyness will increase
                    for(int i = 0; i < time; i += z*3){
                        happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
                        happyness += 6;
                    }

                    SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
                    displayStats();

                }
            }
        }
    }

    /**
     * Changes the Stats of the pet in regular intervals
     * @param activity
     */
    public void changeStats(final Context activity){
        int z = 60*30;
        if(MainActivity.devMode){
            z = 60;
        }
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
                changeHappynessd();
                changeHunger();
                changeHealth();
                int time = (int)System.currentTimeMillis();
                SQLQuerys.saveIntToDB(id, activity, "lastlogin", time);
                //Log.i("TestStats", "time " + time);
            }
        };

        timer.schedule(timeTask, 0l, 1000*z);
    }

    /**
     * Decreases or increses Health depending on the other stats
     */
    private void changeHealth(){
        int health = SQLQuerys.loadIntFromDatabase(id, this, "health");
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        int hunger = SQLQuerys.loadIntFromDatabase(id, this, "hunger");
        if(health != 100){
            if(hunger > 80 && happyness > 80){
                health += 2;
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
            Log.i("TestStats", "Pet is dead");
            SQLQuerys.saveIntToDB(id,this, "status", 1);
        }

        SQLQuerys.saveIntToDB(id,this, "health", health);
        Log.i("TestStats", "health " + health);
    }

    /**
     * Decreases Happyness
     */
    private void changeHappynessd(){
        int happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
        happyness -= 5;
        if(happyness < 0){
            happyness = 0;
        }
        if(happyness > 100){
            happyness = 100;
        }
        SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
        Log.i("TestStats", "happyness " + happyness);
    }

    /**
     * Decreases Hunger
     */
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



}
