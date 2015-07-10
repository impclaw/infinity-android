package com.cgordon.infinityandroid.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.UnitFragment;

/**
 * Created by cgordon on 6/24/2015.
 */
public class UnitActivity extends AppCompatActivity {

    private static final String TAG = UnitActivity.class.getSimpleName();
    private Unit m_unit;

    private UnitFragment m_unitFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_unit = getIntent().getParcelableExtra(MainActivity.UNIT);
        Log.d(TAG, m_unit.toString());

        setContentView(R.layout.activity_unit);

        Fragment unit = getSupportFragmentManager().findFragmentById(R.id.unit);
        if (unit instanceof UnitFragment) {
            m_unitFragment = (UnitFragment) unit;
            m_unitFragment.setUnit(m_unit);
        }

        ImageView image = (ImageView) findViewById(R.id.image_view);

        String imageSize = "_48";

        String resourceName;
        if (m_unit.image == null) {
            resourceName = m_unit.faction + "_" + m_unit.isc;
        } else {
            resourceName = m_unit.faction + "_" + m_unit.image;
        }

        resourceName += imageSize;

        resourceName = UnitListAdapter.prepareDrawableResource(resourceName);

        int resourceId = getResources().getIdentifier(resourceName, "drawable", this.getPackageName());

//        image.setImageResource(resourceId);
//
//        ViewCompat.setTransitionName(image, UnitListActivity.TRANSITION_IMAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(m_unit.isc);


        //getSupportActionBar().setIcon(R.drawable.panoceania_24);

//        ViewCompat.setTransitionName(toolbar, UnitListActivity.TRANSITION_UNIT_NAME);

        TextView textView = (TextView) findViewById(R.id.text_view);
//        textView.setText(m_unit.toString());

    }

}
