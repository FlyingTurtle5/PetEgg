package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        loadPetName();
        goOutside();
    }



    private void loadPetName(){

        String result = "";

        //fix loading with NAME="string"
        //ERROR OCCURS: STRING FROM FILE IS NULL!!! FIND Cause -.-
        //Think of permission to read and write to phone storage -> will check that later on o.o


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
