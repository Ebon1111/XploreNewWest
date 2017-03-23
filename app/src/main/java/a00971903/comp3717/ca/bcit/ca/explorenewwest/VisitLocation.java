package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by chand on 2017-03-16.
 */

public class VisitLocation {
    private String locationName;
    private Location location;

    public VisitLocation(String locationName, Location location){
        this.locationName = locationName;
        this.location = location;
    }

    public String formatLatLng(){
        return location.getLatitude() + "," + location.getLongitude();
    }
    public String getName(){
        return locationName;
    }
    public Location getLocation(){
        return location;
    }
    public LatLng getLatLng(){
        return new LatLng(location.getLatitude(),location.getLongitude());
    }

}
