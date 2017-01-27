package a00971903.comp3717.ca.bcit.ca.explorenewwest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class WhatDoYouWantToSee extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_do_you_want_to_see);
        Toast.makeText(this, "What do you want to see?", Toast.LENGTH_LONG).show();
        finish();
    }
}
