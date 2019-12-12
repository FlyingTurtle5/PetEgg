package bth.mobile_applications.petegg;

import android.location.LocationListener;
import android.location.Location;
import android.os.Bundle;

/**
 * LocationListener sends Location information everytime the Location changes
 */
public class MyLocationListener implements LocationListener{

    private final Outside out;

    public MyLocationListener(Outside out){
        this.out = out;
    }

    @Override
    public void onLocationChanged(Location loc){
        if(loc != null){
            out.stepCounter(loc);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
