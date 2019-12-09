package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class NameEgg extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_egg);

        animateBackground();
        bouningEgg();
        saveAndContinueButton();
    }

    /**
     * Switches to the next Activity
     */
    private void saveAndContinueButton() {
        Button saveAndContinue = (Button) findViewById(R.id.saveButton);

        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //writeFile();
                long id = saveInitDatabase();
                HomeScreen.setId(id);
                startActivity(new Intent(NameEgg.this, HomeScreen.class));
            }
        });
    }


    /**
     * saves the name to the internal Storage
     */
    /*
    public void writeFile(){

        EditText nameToSave = (EditText) findViewById(R.id.nameField);
        String name = nameToSave.getText().toString();
        if(name==""){
            name = "Eggbert";
        }

        try {
            FileOutputStream fileOutputStream = openFileOutput("PetEggFile.txt", MODE_PRIVATE);
            fileOutputStream.write(name.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */

    /**
     * Creates a new row entry for the new pet and returns the primary key
     */
    public long saveInitDatabase(){
        EditText nameToSave = (EditText) findViewById(R.id.nameField);
        String name = nameToSave.getText().toString();
        if(name==""){
            name = "Eggbert";
        }

        SQLConnection dbHelper = new SQLConnection(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PETNAME", name);
        values.put("STATUS", 0);
        values.put("AGE", 0);
        values.put("BIRTHDAY", System.currentTimeMillis());
        values.put("HEALTH", 100);
        values.put("HAPPYNESS", 100);
        values.put("HUNGER", 100);
        values.put("LASTFED", System.currentTimeMillis());
        values.put("LASTLOGIN", System.currentTimeMillis());

        long row = db.insert("petegg_data", null, values);
        db.close();

        return row;
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
        final ImageView bouncingEgg = (ImageView) findViewById(R.id.bouncingEgg);

        float f[] = new float[]{0f, 100f, 0f};

        final ObjectAnimator animator = ObjectAnimator.ofFloat(bouncingEgg, "translationY", f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000L);
        animator.start();
    }
}