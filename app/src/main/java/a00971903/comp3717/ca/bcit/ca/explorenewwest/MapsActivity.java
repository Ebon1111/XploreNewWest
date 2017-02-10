package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Set;

public class MapsActivity extends AppCompatActivity /*FragmentActivity*/ implements OnMapReadyCallback {

    private GoogleMap mMap;

    protected static HashMap<String,LatLng> parks= new HashMap<>();
    protected static HashMap<String,LatLng> schools= new HashMap<>();
    protected static HashMap<String,LatLng> art= new HashMap<>();
    protected static HashMap<String,LatLng> dogs= new HashMap<>();
    LatLng park1 = new LatLng(49.209145, -122.908693);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        float zoomLevel = 16; //This goes up to 21
        // Add a marker in Sydney and move the camera
        LatLng skytrain = new LatLng(49.2014242, -122.9149144);
        mMap.addMarker(new MarkerOptions().position(skytrain).title("New West Skytrain"));

        // Fountains
        mMap.addMarker(new MarkerOptions().position(new LatLng(49.201901, -122.914949
        )).title("Drinking Fountain").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        parks.put("Tipperary Park", park1);
        Set<String> keys = parks.keySet();
        for(String key : keys ){

            mMap.addMarker(new MarkerOptions().position(parks.get(key)).title(key));

        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(skytrain, zoomLevel));
    }
}
