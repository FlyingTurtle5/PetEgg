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
        /*
        if(!fileExists("PetEggFile.txt")) {
            new File("PetEggFile.txt");
        }
        */
        animateBackground();
        bouningEgg();
        getIdLastLivedEgg();
        Boolean eggStatus = false;
        if(id != 0L){
            eggStatus = checkEggStatusDB();
        }
        configureHomeButton(eggStatus);
    }

    private void getIdLastLivedEgg(){
        SQLConnection dbHelper = new SQLConnection(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String column = "status";
        String[] projection = {
                BaseColumns._ID,
                column
        };

        String selection = column + " = ?";
        String[] selectionArgs = { Integer.toString(0) };

        Long result = 0L;
        try {
            Cursor cursor = db.query(
                    "petegg_data",   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null);


            while(cursor.moveToNext()) {
                result = cursor.getLong(cursor.getColumnIndex("_id"));
            }
            cursor.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        if(result != 0L){
            id = result;
        }

        dbHelper.close();

    }

    private final Boolean checkEggStatusDB() {
        Boolean eggAlive = true;

        //calculate if the egg has survived till current date!


        return eggAlive;
    }

    /*
    private Boolean checkEggStatus(){
        Boolean eggAliveStatus = false;
        String lines = "";

        try {
            FileInputStream fileInputStream = openFileInput("PetEggFile.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines + "\n");
            }

            fileInputStream.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(lines != null) {
            if (lines.equalsIgnoreCase("true")) {
                eggAliveStatus = true;
            } else {
                eggAliveStatus = false;
            }
        }
        return eggAliveStatus;
    }
    */
/*
    private void saveStringToDB(String column){
        SQLConnection dbHelper = new SQLConnection(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        String[] columns = {
                column
        };

        String selection = column + " = ?"; // where ...
        String[] selectionArgs = { Integer.toString(0) };  // columns where to add
            //FIX THIS!!!
        Long result = 0L;
        try {
            Cursor cursor = db.update("petegg_data",,selection,selectionArgs);


        } catch (SQLException e){
            e.printStackTrace();
        }

        if(result != 0L){
            id = result;
        }

        dbHelper.close();

    }

 */

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
    /*
        public boolean fileExists(String filename) {
            File file = this.getFileStreamPath(filename);
            if(file == null || !file.exists()) {
                return false;
            }
            return true;
        }
    *  /
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

    private void Timer(){
        new CountDownTimer(10000*6*10, 1000){
            public void onFinish() {
                //change happyness+hunger+health

            }
        }.start();
    }

}
