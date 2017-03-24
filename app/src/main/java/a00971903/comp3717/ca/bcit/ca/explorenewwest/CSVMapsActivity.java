package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.DirectionFinder;
import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.DirectionFinderListener;
import a00971903.comp3717.ca.bcit.ca.explorenewwest.Modules.Route;

import static java.lang.Float.MAX_VALUE;

public class CSVMapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;


    private static ArrayList<VisitLocation> spots;
    private static ArrayList<VisitLocation> parks;
    private static ArrayList<VisitLocation> art;
    private static ArrayList<VisitLocation> bars;
    private static ArrayList<VisitLocation> cafes;
    //private static ArrayList<VisitLocation> parks;

    boolean mRequestingLocationUpdates;

    private static HashSet<String> visitedSpots;


    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String waypoints;



    private static final long INTERVAL = 1000 * 10;
    private static final long FATEST_INTERVAL = 1000 * 5;


    //LatLng park1 = new LatLng(49.209145, -122.908693);
    LatLng skytrain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRequestingLocationUpdates=true;
        mCurrentLocation = UserLocation.getLocation();

        spots = new ArrayList<>();

        spots.add(new VisitLocation("Start", mCurrentLocation));


        parks = new ArrayList<>();
        bars = new ArrayList<>();
        cafes = new ArrayList<>();
        art = new ArrayList<>();
       // visitedSpots = new HashSet<>();

        skytrain = new LatLng(49.2014242, -122.9149144);
        String argString = getIntent().getStringExtra("Arguments");

        argString = argString.toLowerCase();

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
            originMarkers.add(mMap.addMarker(new MarkerOptions()
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
             .title(route.startAddress)
             .position(route.startLocation)));
             /**destinationMarkers.add(mMap.addMarker(new MarkerOptions()
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
             .title(route.endAddress)
             .position(route.endLocation)));*/


            for (int i = 1; i < spots.size(); i++) {
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

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        //MAD TESTY BWOI
        UserLocation.setCurrentLocation(location);
        spots.set(0,new VisitLocation("Start",location));
       /** if(waypoints==null) {
            makeWaypointString();
        }**/
        float zoomLevel = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(location.getLatitude(),location.getLongitude())), zoomLevel));


        // sendRequest(spots.get(0).formatLatLng(), spots.get(spots.size() - 1).formatLatLng(), waypoints);
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Sleeping...");

            //System.out.println(params[0]);
            String[] args = params[0].split(",");
           // Location currPos = UserLocation.getLocation();
            //userPosition = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            // spots.add(new VisitLocation("Start", mCurrentLocation));
            // Log.d (spots.get (spots.size () - 1).getLocation().toString() + " ", "HERE");
            AssetManager am = getAssets();
            String destinationName = "Final Spot";

            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
                String[] restSplit = args[i].split("\\|");

                ArrayList<VisitLocation> tempList = new ArrayList<>();
                if(args[i].equals("parks") && parks.size()!=0){
                    tempList = parks;
                }
                else if(args[i].equals("art") && art.size()!=0){
                    tempList = art;
                }
                else if (args[i].equals("bars") && bars.size()!=0){
                    tempList = bars;
                }
                else if (args[i].equals("cafe") && cafes.size()!=0){
                    tempList = cafes;
                }
                else {
                    try {

                        InputStream is = am.open(restSplit[0] + ".csv");
                        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String line;
                        HashSet<String> restCategories = new HashSet<>();
                        if (restSplit[0].equals("restaurant")) {

                            for (int j = 1; j < restSplit.length; j++) {
                                restCategories.add(restSplit[j]);
                            }
                            if (restCategories.size()==0){
                                restCategories.add("casual");
                                restCategories.add("fast food");
                                restCategories.add("fine dining");
                            }
                        }

                        for(String temp:restCategories){
                            System.out.println(temp);
                        }
                        System.out.println(restCategories.size());
                        float minDistance = MAX_VALUE;
                        String minKey = "";
                        Location minLocation = new Location("");
                        while ((line = br.readLine()) != null) {
                            String[] entry = line.split(",");
                            if (restSplit[0].equals("restaurant")) {
                                System.out.println(entry[3]);
                                System.out.println(restCategories.contains(entry[3]));
                                if (!(restCategories.contains(entry[3]))) {
                                   // System.out.println("not in categories");
                                    continue;
                                }
                            }
                            /**if (visitedSpots.contains(entry[0])) {
                                continue;
                            }**/
                            Location location = new Location("test");
                            location.setLatitude(Double.parseDouble(entry[1]));
                            location.setLongitude(Double.parseDouble(entry[2]));
                            tempList.add(new VisitLocation(entry[0], location));


                            //LatLng latlng = new LatLng(Double.parseDouble(entry[1]), Double.parseDouble(entry[2]));
                        }
                        //currPos = tempMap.get(minKey);
                        // spots.add(new VisitLocation(minKey,minLocation));
                        //
                        // currPos = spots.get(spots.size()-1).getLocation();
                        // visitedSpots.add(minKey);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(args[i].equals("parks")){
                        parks = tempList;
                    }
                    else if(args[i].equals("art")){
                        art = tempList;
                    }
                    else if(args[i].equals("bar")){
                        bars = tempList;
                    }
                    else if(args[i].equals("cafe")){
                        cafes = tempList;
                    }
                }
                findRandomLocation(tempList);
            }

            makeWaypointString();

            sendRequest(spots.get(0).formatLatLng(),spots.get(spots.size()-1).formatLatLng(),waypoints);

            return null;

        }

        private void findRandomLocation(ArrayList<VisitLocation> list){

            if(list.size()<5){
                findClosestLocation(list);
                return;
            }
            MaxComparator pqMaxComparetor = new MaxComparator();

            PriorityQueue<VisitLocation> pq = new PriorityQueue<>(5, pqMaxComparetor);
            Location currPos = spots.get(spots.size()-1).getLocation();
            int i;
            for(i=0;i<5;i++) {
                pq.offer(list.get(i));
            }

            while(i<list.size()){
               if(currPos.distanceTo(pq.peek().getLocation())>currPos.distanceTo(list.get(i).getLocation())){
                   pq.poll();
                   pq.offer(list.get(i));
               }

            }

            int randomNum = (int)(Math.random() * 4);
            VisitLocation[] visitLocations = pq.toArray(new VisitLocation[5]);

            spots.add(visitLocations[randomNum]);
            list.remove(visitLocations[randomNum]);
           // visitedSpots.add(visitLocations[randomNum].getName());

        }

        private void findClosestLocation(ArrayList<VisitLocation> list){
            Location currPos = spots.get(spots.size()-1).getLocation();
            Log.d (spots.get(spots.size()-1).getName(), "HERE");
            float minDistance = MAX_VALUE;
            VisitLocation minLocation = list.get(0);
            float distance = minDistance;
            for(VisitLocation temp : list){
                if((distance = currPos.distanceTo(temp.getLocation()))<minDistance){
                    minDistance = distance;
                    minLocation = temp;
                }
            }

            spots.add(minLocation);
            list.remove(minLocation);


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

         class MaxComparator implements Comparator<VisitLocation> {

            public int compare(VisitLocation one , VisitLocation two) {
                Location currPos = spots.get(spots.size()-1).getLocation();
                return (int) (currPos.distanceTo(two.getLocation()) - currPos.distanceTo(one.getLocation()));
            }
        }
    }

    public void makeWaypointString(){
        StringBuilder waypointString = new StringBuilder();
        String prefix = "";
        for (int i=1;i<spots.size()-1;i++) {
            waypointString.append(prefix);
            prefix = "|";
            waypointString.append(spots.get(i).formatLatLng());
        }

        waypoints = waypointString.toString();

    }

}



