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

        SQLQuerys querys = new SQLQuerys();
        changeName(querys.loadStringFromDatabase(id,this,"petname"));
        goOutside();
        feed();
        health();
    }

    private void changeName(String name){
        TextView nameField = (TextView) findViewById(R.id.nameOfPet);
        if(name != null) {
            nameField.setText(name);
        }else{
            nameField.setText("No name found in database.");
        }
    }

    private void goOutside(){
        ImageView goOutsideButton = (ImageView) findViewById(R.id.doorButton);

        goOutsideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Outside.class));
            }
        });
    }

    private void feed(){
        ImageView feedButton = (ImageView) findViewById(R.id.feedButton);

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIntToDatabase("hunger", 100);
                health();
            }
        });
    }


}
