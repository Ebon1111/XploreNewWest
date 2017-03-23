package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.DirectionFinder;
import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.DirectionFinderListener;
import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.Route;

import static java.lang.Float.MAX_VALUE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;


    private static ArrayList<VisitLocation> spots;
    private static HashSet<String> visitedSpots;


    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;


    private static final long INTERVAL = 1000 * 10;
    private static final long FATEST_INTERVAL = 1000 * 5;


    //LatLng park1 = new LatLng(49.209145, -122.908693);
    LatLng skytrain;
    LatLng userPosition;
    Location loc;
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        spots = new ArrayList<>();
        visitedSpots = new HashSet<>();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider

        String provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
       // mCurrentLocation = locationManager.getLastKnownLocation(provider);
        mCurrentLocation = UserLocation.getLocation();

        skytrain = new LatLng(49.2014242, -122.9149144);
        String argString = getIntent().getStringExtra("Arguments");

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(argString);

    }



    /**  private String makeWaypointUrl(String unformatted) {
     return unformatted.replace("(", "").replace(")", "").replace("lat/lng: ", "");
     }**/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        float zoomLevel = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(skytrain, zoomLevel));
    }

    private void sendRequest(String origin, String destination, String waypoints) {
        try {
            new DirectionFinder(this, origin, destination, waypoints).execute();
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
            for (Polyline polyline : polylinePaths) {
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

            Toast.makeText(getApplicationContext(), route.duration.text, Toast.LENGTH_LONG).show();
            /**originMarkers.add(mMap.addMarker(new MarkerOptions()
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
             .title(route.startAddress)
             .position(route.startLocation)));
             destinationMarkers.add(mMap.addMarker(new MarkerOptions()
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
             .title(route.endAddress)
             .position(route.endLocation)));*/


            for (int i=0;i<spots.size();i++) {
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title(spots.get(i).getName())
                        .position(spots.get(i).getLatLng()));
            }
            spots.clear();

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }




    private class AsyncTaskRunner extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
            //System.out.println(params[0]);
            String[] args = params[0].split(",");
            Location currPos = mCurrentLocation;
            //userPosition = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            spots.add(new VisitLocation("Start",mCurrentLocation));
            AssetManager am = getAssets();
            String destinationName = "Final Spot";

            for (int i = 0; i < args.length; i++) {
                String[] restSplit = args[i].split("\\|");
                HashMap<String,Location> tempMap = new HashMap<>();
                try {

                    InputStream is = am.open(restSplit[0] + ".csv");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    HashSet<String> restCategories = new HashSet<>();
                    if (restSplit[0].equals("restaurant")){
                        //restCategories = new HashSet<>();
                        for (int j=1;j<restSplit.length;j++){
                            restCategories.add(restSplit[j]);
                        }
                    }

                    float minDistance = MAX_VALUE;
                    String minKey = "";
                    Location minLocation = new Location("");
                    while ((line = br.readLine()) != null) {
                        String[] entry = line.split(",");
                        if (restSplit[0].equals("restaurant")){
                            if(!(restCategories.contains(entry[3]))){
                                continue;
                            }
                        }
                        if(visitedSpots.contains(entry[0])){
                            continue;
                        }
                        Location location = new Location("test");
                        location.setLatitude(Double.parseDouble(entry[1]));
                        location.setLongitude(Double.parseDouble(entry[2]));
                        float distance = currPos.distanceTo(location);
                        if(distance<minDistance && distance!=0f){
                            minDistance = distance;
                            minKey = entry[0];
                            minLocation = location;
                        }
                        //LatLng latlng = new LatLng(Double.parseDouble(entry[1]), Double.parseDouble(entry[2]));
                    }
                    //currPos = tempMap.get(minKey);
                    spots.add(new VisitLocation(minKey,minLocation));
                    currPos = minLocation;
                    visitedSpots.add(minKey);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                /**  System.out.println(minKey);
                 currPos = tempMap.get(minKey);

                 //System.out.println(minKey);


                 Location loc = tempMap.get(minKey);

                 // Log.d ("HERE", loc.toString ());

                 spots.add(new VisitLocation(minKey,tempMap.get(minKey)));
                 tempMap.remove(minKey);**/
                //System.out.println("adding " + minKey + "to spots");
                //spots.put(minKey,currPos);
            }
            StringBuilder waypointString = new StringBuilder();
            String prefix = "";
            for (int i=1;i<spots.size()-1;i++) {
                waypointString.append(prefix);
                prefix = "|";
                waypointString.append(spots.get(i).formatLatLng());
            }



            System.out.println(spots.get(0).formatLatLng());
            System.out.println(spots.get(spots.size()-1).formatLatLng());



            sendRequest(spots.get(0).formatLatLng(),spots.get(spots.size()-1).formatLatLng(),waypointString.toString());

            return null;

        }



        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

}



