package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //walkPage(); // call walkPage
    }

    /* at call: creates an intent for "what do you want to see" section*/
    public void walkPage(final View view) {
        startActivity(new Intent(this, WalkConfig.class));
    }
}
