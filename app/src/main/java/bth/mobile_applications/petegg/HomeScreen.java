package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.function.LongToIntFunction;

public class HomeScreen extends AppCompatActivity {

    static long id;
    private LightSensor lightSensor;
    private long lastTime;
    private long firstTime;
    private long lastSwipe = 0;

    public static void setId(long id) {
        HomeScreen.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        SQLQuerys querys = new SQLQuerys();
        changeName(querys.loadStringFromDatabase(id,this,"petname"));
        lightSensor = new LightSensor((SensorManager)getSystemService(SENSOR_SERVICE), this);
        goOutside();
        feed(this);

        //swipe
        ImageView view = (ImageView)findViewById(R.id.homescreenEgg);
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipe() {
                Log.i("TestSwipe", "Swiped");
                changeHappyness();
            }
        });

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
        ImageView goOutsideButton = (ImageView) findViewById(R.id.doorButton);

        goOutsideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Outside.class));
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
            happyness += 1;
            lastSwipe = currentSwipe;
        }

        SQLQuerys.saveIntToDB(id,this, "happyness", happyness);
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
            }
        });
    }

    protected void onResume() {
        super.onResume();
        lightSensor.onResume();
    }

    protected void onPause() {
        super.onPause();
        lightSensor.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("TestLight", "Pet is no longer sleeping");
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
            Log.i("TestLight", "Pet is no longer sleeping");
        }else{
            if(lastTime == 0){
                lastTime = System.currentTimeMillis();
                firstTime = System.currentTimeMillis();
            }else{
                long currentTime = System.currentTimeMillis();
                int z = 10;
                if(MainActivity.devMode){
                    z = 1;
                }
                int happyness = 0;
                if((currentTime - firstTime) > 6000*z){
                    //Pet is sleeping (after z minutes)
                    //Log.i("TestLight", "Pet is sleeping");
                    long time = (currentTime - lastTime)/6000; //in minutes

                    //every z*3 minutes happyness will increase
                    for(int i = 0; i < time; i += z*3){
                        happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
                        happyness += 6;
                    }

                    SQLQuerys.saveIntToDB(id,this, "happyness", happyness);

                }
            }
        }
    }



}
