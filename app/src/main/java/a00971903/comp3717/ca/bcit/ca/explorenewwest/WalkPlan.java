package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.location.LocationListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WalkPlan extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ArrayList<String> spinnerArray;
    ArrayAdapter<String> spinnerArrayAdapter;
    String newPriceRange;
    ArrayList<String> foodPriceRanges;
    HashMap<Spinner, String> restaurantTypes;
    ArrayList<Spinner> spinners;
    TableLayout tableLayout;
    Button removeButton;
    private int counter = 1;
    Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_plan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // System.out.println(mCurrentLocation.toString());
        // UserLocation.setCurrentLocation(lm.getLastKnownLocation(provider));
        /**LocationListener locationListener = new LocationListener() {

        @Override public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        UserLocation.setCurrentLocation(location);
        }

        @Override public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override public void onProviderEnabled(String provider) {

        }

        @Override public void onProviderDisabled(String provider) {

        }
        };
         lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);**/

        spinnerArray = new ArrayList<>();
        spinnerArray.add("Parks");
        spinnerArray.add("Art");
        spinnerArray.add("Cafe");
        spinnerArray.add("Restaurant");
        spinnerArray.add("Bar/Club");
        spinnerArray.add("Movie");

        spinners = new ArrayList<>();
        foodPriceRanges = new ArrayList<>();
        restaurantTypes = new HashMap<>();
        tableLayout = (TableLayout) findViewById(R.id.walk_table);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);

        removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setEnabled(false);


        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        spinner.setAdapter(spinnerArrayAdapter);
        spinners.add(spinner);


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

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onItemSelected(final AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        if (parent.getItemAtPosition(pos).equals("Restaurant")) {
            AlertDialog dialog;
            //following code will be in your activity.java file
            final CharSequence[] items = {"Fast Food ", "Casual Dining ", "Fine Dining "};
            // arraylist to keep the selected items
            final ArrayList<Integer> selectedItems = new ArrayList<>();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Where would you like to dine?");
            builder.setMultiChoiceItems(items, null,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        // indexSelected contains the index of item (of which checkbox checked)
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected,
                                            boolean isChecked) {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                // write your code when user checked the checkbox
                                selectedItems.add(indexSelected);
                            } else if (selectedItems.contains(indexSelected)) {
                                // Else, if the item is already in the array, remove it
                                // write your code when user Uchecked the checkbox
                                selectedItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    })
                    // Set the action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            StringBuilder destinationString = new StringBuilder();
                            String prefix = "|";
                            for (Integer temp : selectedItems) {
                                destinationString.append(prefix);
                                //prefix = "|";
                                String type;
                                if (temp == 0) {
                                    type = "Fast food";
                                } else if (temp == 1) {
                                    type = "Casual";
                                } else {
                                    type = "Fine dining";
                                }
                                destinationString.append(type);
                            }
                            //destinationString.append("|");
                            newPriceRange = destinationString.toString();
                            //foodPriceRanges.add(newPriceRange);
                            restaurantTypes.put((Spinner) parent, destinationString.toString());
                            //dialog.dismiss();
                        }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //  Your code when user clicked on Cancel

                            parent.setSelection(0);
                            dialog.cancel();
                        }
                    });


            dialog = builder.create();//AlertDialog dialog; create like this outside onClick
            dialog.show();

            //setContentView(layout);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public void onClickAdd(View view) {

        counter++;
        removeButton.setEnabled(true);
        Spinner newSpinner = new Spinner(this);
        newSpinner.setAdapter(spinnerArrayAdapter);
        newSpinner.setOnItemSelectedListener(this);
        spinners.add(newSpinner);

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView val = new TextView(this);
        val.setText(counter + ".");
        val.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        val.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        row.addView(val);
        row.addView(newSpinner);

        tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }

    public void onClickRemove(final View view) {

        counter--;
        deleteRow(tableLayout);

    }

    private void deleteRow(TableLayout table) {

        int childCount = table.getChildCount();

        if (childCount == 2) {
            table.removeViews(1, 1);
            removeButton.setEnabled(false);
        }
        // Remove all rows except the first one
        if (childCount > 2) {
            table.removeViews(childCount - 1, 1);
        }
    }

    public void onClickGo(final View view) {

        StringBuilder destinationString = new StringBuilder();
        String prefix = "";
        for (Spinner temp : spinners) {
            destinationString.append(prefix);
            prefix = ",";
            if (temp.getSelectedItem().toString().equals("Bar/Club")) {
                destinationString.append("Bar");
                continue;
            }
            destinationString.append(temp.getSelectedItem().toString().toLowerCase());
            if (temp.getSelectedItem().toString().equals("Restaurant")) {
                destinationString.append(restaurantTypes.get(temp));
            }
        }

        //System.out.println(destinationString.toString());
        UserLocation.setCurrentLocation(mCurrentLocation);


        Intent intent = new Intent(this, CSVMapsActivity.class);
        intent.putExtra("Arguments", destinationString.toString());


        startActivity(intent);

    }


    private int getRestaurantCount() {
        int count = 0;
        for (Spinner temp : spinners) {
            if (temp.getSelectedItem().equals("Restaurant")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        /**if (mCurrentLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }**/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /** private class MyLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
           // pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            //String longitude = "Longitude: " + loc.getLongitude();
            //String latitude = "Latitude: " + loc.getLatitude();
            UserLocation.setCurrentLocation(loc);

        /*------- To get city name from coordinates -------- */
           /** String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;**/
        /**}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

**/

}
