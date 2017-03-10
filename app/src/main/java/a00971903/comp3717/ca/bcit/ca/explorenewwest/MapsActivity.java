package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.DirectionFinder;
import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.DirectionFinderListener;
import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.Route;

public class MapsActivity extends AppCompatActivity /*FragmentActivity*/ implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;

    protected static HashMap<String, LatLng> parks = new HashMap<>();
    protected static HashMap<String, LatLng> schools = new HashMap<>();
    protected static HashMap<String, LatLng> art = new HashMap<>();
    protected static HashMap<String, LatLng> dogs = new HashMap<>();
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    LatLng park1 = new LatLng(49.209145, -122.908693);
    LatLng skytrain;
    int minutes = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        skytrain = new LatLng(49.2014242, -122.9149144);

        String argString = getIntent().getStringExtra("Arguments");
        argString = argString.toLowerCase();
        String[] args = argString.split(",");

        AssetManager am = getAssets();

        for (int i = 0; i < args.length; i++) {

            try {
                InputStream is = am.open(args[i] + ".csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    String[] entry = line.split(",");
                    LatLng latlng = new LatLng(Double.parseDouble(entry[1]), Double.parseDouble(entry[2]));
                    if (args[i].equals("parks")) {
                        parks.put(entry[0], latlng);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Set<String> keys = parks.keySet();

            String waypoint = parks.get("Tipperary Park").latitude + ","+ parks.get("Tipperary Park").longitude;
            sendRequest(skytrain.toString(),parks.get("Tipperary Park").toString(),parks.get("Simcoe Park").toString());

        }
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        float zoomLevel = 16; //This goes up to 21
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(skytrain).title("New West Skytrain"));

        // Fountains
        mMap.addMarker(new MarkerOptions().position(new LatLng(49.201901, -122.914949
        )).title("Drinking Fountain").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //parks.put("Tipperary Park", park1);
        Set<String> keys = parks.keySet();
        for(String key : keys ){

            mMap.addMarker(new MarkerOptions().position(parks.get(key)).title(key).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(skytrain, zoomLevel));
    }

    private void sendRequest(String origin,String destination, String waypoints) {
        try {
            new DirectionFinder(this, origin, destination,waypoints).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        //progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
          //  ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
           // ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            Toast.makeText(getApplicationContext(),route.duration.text,Toast.LENGTH_LONG).show();
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

}
