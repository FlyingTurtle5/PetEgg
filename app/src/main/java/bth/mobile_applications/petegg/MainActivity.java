package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

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
        final Boolean eggStatus = checkEggStatus();
        configureHomeButton(eggStatus);
    }

    private Boolean checkEggStatus(){
        Boolean eggAliveStatus = false;
        String lines = "";

        //fix loading with STATUS="string"
        try {
            FileInputStream fileInputStream = openFileInput("PetEggFile.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines + "\n");
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //all in one file?! that doesnt work?? 1000 files dont work either... make sure what to save!!!
        if(lines != null) {
            if (lines.equalsIgnoreCase("true")) {
                eggAliveStatus = true;
            } else {
                eggAliveStatus = false;
            }
        }
        return eggAliveStatus;
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

}
