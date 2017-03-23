package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.PolylineOptions;

public class WalkConfig extends AppCompatActivity {

    private static final String TAG = WalkConfig.class.getName();

    public static boolean visitParks = false;
    public static boolean visitArt = false;
    public static boolean visitSchools = false;

    CheckBox checkParks;
    CheckBox checkCafe ;
    CheckBox checkOffDog ;
    CheckBox checkArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_config);

        //ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         checkParks = (CheckBox)findViewById(R.id.Parks);
         checkCafe = (CheckBox)findViewById(R.id.Cafe);
         checkOffDog = (CheckBox)findViewById(R.id.OffLeash);
         checkArt = (CheckBox)findViewById(R.id.Art);


    }

    public void mapPage(final View view){

        StringBuilder args = new StringBuilder(100);

        if(checkParks.isChecked()){
            args.append("Parks,");
        }
        if(checkArt.isChecked()){
            args.append("Art,");
        }
        if(checkCafe.isChecked()){
            args.append("Cafe,");
        }
        if(checkOffDog.isChecked()){
            args.append("OffLeash,");
        }
        args.deleteCharAt(args.length()-1);
        Intent intent = new Intent(this,CSVMapsActivity.class);
        intent.putExtra("Arguments",args.toString());
        startActivity(intent);
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



    public void planRoute(final View view){
     //   Intent intent = new Intent(WalkConfig.this,MapsActivity.class);
     //   startActivity(intent);
    }
    @Override
    protected void onStart()
    {
        Log.d(TAG, "enter onStart");
        super.onStart();
        Log.d(TAG, "exit onStart");
    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "enter onResume");
        super.onResume();
        Log.d(TAG, "exit onResume");
    }





}
