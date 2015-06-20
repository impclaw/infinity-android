package com.cgordon.infinityandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.fragment.SettingsFragment;

/**
 * Created by cgordon on 6/19/2015.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment())
                .commit();

    }

}
