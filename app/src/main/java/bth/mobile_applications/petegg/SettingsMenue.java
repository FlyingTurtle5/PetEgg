package bth.mobile_applications.petegg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

public class SettingsMenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menue);
        saveAndContinueButton();
        toggle_dev_mode();
        setNotifications();
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

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    //do something
                }else{
                    //do something else
                }
            }
        });
    }

    /**
     * Toggle DevMode
     */
    private void toggle_dev_mode(){

        Switch dev_mode_on = (Switch) findViewById(R.id.developer_mode);

        dev_mode_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FrameLayout dev_frame = findViewById(R.id.dev_frame);
                if(isChecked==true) {
                    dev_frame.setVisibility(View.VISIBLE);
                }else{
                    dev_frame.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
