package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WalkConfig extends AppCompatActivity {

    private static final String TAG = WalkConfig.class.getName();

    public static boolean visitParks = false;
    public static boolean visitArt = false;
    public static boolean visitSchools = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_config);

        //ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    @Override
    protected void onPause()
    {
        Log.d(TAG, "enter onPause");
        super.onPause();
        Log.d(TAG, "exit onPause");
    }

    @Override
    protected void onStop()
    {
        Log.d(TAG, "enter onStop");
        super.onStop();
        Log.d(TAG, "exit onStop");
    }

    @Override
    protected void onDestroy()
    {
        Log.d(TAG, "enter onDestroy");
        super.onDestroy();
        Log.d(TAG, "exit onDestroy");
    }

    @Override
    protected void onRestart()
    {
        Log.d(TAG, "enter onRestart");
        super.onRestart();
        Log.d(TAG, "exit onRestart");
    }

    public void mapPage(final View view){
        startActivity(new Intent(this, MapsActivity.class));
    }

}
