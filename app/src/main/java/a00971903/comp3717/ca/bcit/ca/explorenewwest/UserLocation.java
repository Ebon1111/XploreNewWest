package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by chand on 2017-03-17.
 */

public class UserLocation  {

    private static Location mCurrentLocation;
    private UserLocation(){}

    public static void setCurrentLocation(Location location){
            mCurrentLocation = location;
    }


    public static Location getLocation(){
        return mCurrentLocation;
    }

    public static String print(){
        return mCurrentLocation.toString();
    }


}
