package bth.mobile_applications.petegg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.util.Log;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

/**
 * Notification Service, sends Notification every time the MainActivity is destroyed
 */
public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //stop timer
        if(timer != null){
            timer.cancel();
            timer = null;

        }
        Log.i("TestNotification", "NotificationService destroyed");
        super.onDestroy();
    }

    /**
     * Starts Timer, that will send notification after a specific amount of time
     */
    public void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask(){
          public void run(){
              handler.post(new Runnable(){
                  public void run(){
                      createNotification();
                  }
              });
          }
        };
        int z = 120; //alle 2 Stunden
        if(MainActivity.devMode){
            z = 20;
        }
        timer.schedule(timerTask, 60000, z*1000);
    }


    /**
     * Creates the notification that will be send
     */
    private void createNotification(){
        RunningAppProcessInfo myProcess = new RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        boolean isInBackground = myProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
        if(isInBackground) {
            NotificationManager notManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), "default");
            notBuilder.setContentTitle("PetEgg");
            notBuilder.setContentText("It's time to look after your Pet!");
            notBuilder.setTicker("It's time to look after your Pet!");
            notBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
            notBuilder.setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel("10001", "NOTIFICATION_CHANNEL_NAME", importance);
                notBuilder.setChannelId("10001");
                assert notManager != null;
                notManager.createNotificationChannel(notificationChannel);
            }
            assert notManager != null;
            notManager.notify((int) System.currentTimeMillis(), notBuilder.build());
            Log.i("TestNotification", "Notification send");
            this.onDestroy();
        }
    }

}
