package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;

import android.content.Context;

public class SettingsMenue extends AppCompatActivity {

    static long id;

    public static void setId(long id) {
        HomeScreen.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menue);
        saveAndContinueButton();
        toggle_dev_mode();
        setNotifications();
        setLocation();
        changeHealth();
    }


    /**
     * Switches to the next Activity
     */
    private void saveAndContinueButton() {
        Button saveAndContinue = (Button) findViewById(R.id.back_to_homescreen);

        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsMenue.this, HomeScreen.class));
            }
        });
    }

    /**
     * Toggle Notifications.
     */
    private void setNotifications(){

        CheckBox notification = (CheckBox) findViewById(R.id.notifications_toggle);
        if(MainActivity.notOn){
            notification.setChecked(true);
        }else{
            notification.setChecked(false);
        }

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    MainActivity.notOn = true;
                }else{
                    MainActivity.notOn = false;
                }
            }
        });
    }

    /**
     * Toggle Location.
     */
    private void setLocation(){

        CheckBox location = (CheckBox) findViewById(R.id.location_toggle);

        if(MainActivity.locOn){
            location.setChecked(true);
        }else{
            location.setChecked(false);
        }

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    MainActivity.locOn = true;
                }else{
                    MainActivity.locOn = false;
                }
            }
        });
    }

    /**
     * Toggle DevMode
     */
    private void toggle_dev_mode(){

        Switch dev_mode_on = (Switch) findViewById(R.id.developer_mode);
        FrameLayout dev_frame = findViewById(R.id.dev_frame);
        if(MainActivity.devMode){
            dev_frame.setVisibility(View.VISIBLE);
            dev_mode_on.setChecked(true);
        }else{
            dev_frame.setVisibility(View.INVISIBLE);
            dev_mode_on.setChecked(false);
        }

        dev_mode_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FrameLayout dev_frame = findViewById(R.id.dev_frame);
                if(isChecked==true) {
                    dev_frame.setVisibility(View.VISIBLE);
                    MainActivity.devMode = true;
                }else{
                    dev_frame.setVisibility(View.INVISIBLE);
                    MainActivity.devMode = false;
                }
            }
        });
    }

    private void changeHealth(){
        Button changeHealth = (Button) findViewById(R.id.change_health);

        changeHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amount = (EditText)findViewById(R.id.enter_health);
                int health = Integer.parseInt(amount.getText().toString());

                SQLQuerys.saveIntToDB(id,SettingsMenue.this, "health", health);
            }
        });
    }
}
