package bth.mobile_applications.petegg;

import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;

public class LightSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor light;
    /*private long lastTime;
    private long firstTime;*/
    private final HomeScreen ma;

    static long id;


    public LightSensor(SensorManager sm, HomeScreen ma){
        sensorManager = sm;
        this.ma = ma;
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        id = ma.id;
    }

    /*@Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lastTime = 0;
    }*/

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    public final void onSensorChanged(SensorEvent event) {
        /*Log.i("TestLight", "SensorChanged");
        float currentLux = event.values[0];
        if(currentLux > 50){
            lastTime = System.currentTimeMillis();
        }else{
            if(lastTime == 0){
                lastTime = System.currentTimeMillis();
                firstTime = System.currentTimeMillis();
            }else{
                long currentTime = System.currentTimeMillis();
                int z = 10;
                z = 1; // to test
                int happyness = 0;
                if((currentTime - firstTime) > 60000*z){
                    //Pet is sleeping (after z minutes)
                    Log.i("TestLight", "Pet is sleeping");
                    long time = (currentTime - lastTime)*60000; //in minutes

                    //every z*3 minutes happyness will increase
                    for(int i = 0; i < time; i += z*3){
                        happyness = SQLQuerys.loadIntFromDatabase(id, this, "happyness");
                        happyness += 4;
                    }

                    SQLQuerys.saveIntToDB(id,this, "happyness", happyness);

                }
            }
        }*/
        ma.useLightSensor(event.values);
    }

    protected void onResume() {
        // Register a listener for the sensor.
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }


    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        sensorManager.unregisterListener(this);
    }
}
