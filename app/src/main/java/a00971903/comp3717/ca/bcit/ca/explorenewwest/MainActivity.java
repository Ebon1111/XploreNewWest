package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Location mCurrentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //walkPage(); // call walkPage
        //setupDb ();



    }

    /**private void setupDb () {
        helper = DatabaseHelper.getInstance (this);
        helper.openDatabaseForWriting(this);
        if (helper.getNumberOfParks() == 0) {
            helper.createPark("Simcoe Park", 49.202870, -122.917251);
            helper.createPark("Toronto Place Park", 49.203971, -122.914665);
            helper.createPark("Friendship Gardens", 49.207173, -122.909914);
            helper.createPark("Tipperary Park", 49.208014, -122.907189);
        }


    }*/

    /* at call: creates an intent for "what do you want to see" section*/
    public void walkPage(final View view) {
        startActivity(new Intent(this, WalkPlan.class));
    }
}
