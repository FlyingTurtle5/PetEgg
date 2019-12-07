package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

    public static void setId(long id) {
        HomeScreen.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //loadPetName();
        loadFromDatabase();
        goOutside();
    }


    private void loadFromDatabase(){
        SQLConnection dbHelper = new SQLConnection(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String column = "petname";
        String[] projection = {
                BaseColumns._ID,
                column
        };

        String selection = dbHelper._ID + " = ?";
        String[] selectionArgs = { (String) String.valueOf(id)};

        String result = null;
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
                result = cursor.getString(cursor.getColumnIndex("petname"));
            }
            cursor.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        TextView nameField = (TextView) findViewById(R.id.nameOfPet);
        if(result != null) {
            nameField.setText(result);
        }else{
            nameField.setText("null!?");
        }

        dbHelper.close();
    }

    /*
    private void loadPetName(){

        String result = "";

        try {
            FileInputStream fileInputStream = openFileInput("PetEggFile.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }

            fileInputStream.close();
            result = stringBuffer.toString();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView nameField = (TextView) findViewById(R.id.nameOfPet);
        if(result != null) {
            nameField.setText(result);
        }else{
            nameField.setText("null!?");
        }
    }
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
}
