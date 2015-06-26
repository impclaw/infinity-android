package com.cgordon.infinityandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.UnitListFragment;
import com.cgordon.infinityandroid.storage.ArmyData;

/**
 * Created by cgordon on 6/24/2015.
 */
public class UnitListActivity extends AppCompatActivity implements UnitListFragment.ArmyProvider, UnitListFragment.UnitSelectedListener {

    private static final String TAG = UnitListActivity.class.getSimpleName();
    private static Army m_army;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            m_army = savedInstanceState.getParcelable(MainActivity.ARMY);
        } else {
            m_army = getIntent().getParcelableExtra(MainActivity.ARMY);
        }
        if (m_army == null) {
            onResume();
        }

        Log.d(TAG, m_army.toString());

        setContentView(R.layout.activity_unit_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(m_army.name);

//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.unit_list);
//        fragment.setRetainInstance(true);
//        if (fragment instanceof UnitListFragment) {
//            ((UnitListFragment) fragment).setOnUnitSelectedListener(this);
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences p = getSharedPreferences(MainActivity.UNIT, MODE_PRIVATE);
        p.edit().putLong(MainActivity.ID, m_army.dbId).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (m_army == null) {
            SharedPreferences p = getSharedPreferences(MainActivity.UNIT, MODE_PRIVATE);
            long dbId = p.getLong(MainActivity.ID, -1);
            ArmyData armyData = new ArmyData(this);
            armyData.open();
            m_army = armyData.getArmy(dbId);
            armyData.close();
        }
    }

    @Override
    public Army getArmy() {
        return m_army;
    }

    @Override
    public void unitSelected(Unit unit) {
        Log.d(TAG, unit.toString());
        Intent i = new Intent(this, UnitActivity.class);
        i.putExtra(MainActivity.UNIT, unit.toString());
        startActivity(i);


    }
}
