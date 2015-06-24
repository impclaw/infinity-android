package com.cgordon.infinityandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;

/**
 * Created by cgordon on 6/24/2015.
 */
public class UnitActivity extends AppCompatActivity {

    private static final String TAG = UnitActivity.class.getSimpleName();
    private String m_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_unit = getIntent().getStringExtra(MainActivity.UNIT);
        Log.d(TAG, m_unit.toString());

        setContentView(R.layout.activity_unit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Unit");

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(m_unit);

    }

}
