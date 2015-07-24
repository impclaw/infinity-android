package com.cgordon.infinityandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.fragment.UnitListFragment;
import com.cgordon.infinityandroid.storage.ArmyData;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgordon on 6/24/2015.
 */
public class UnitListActivity extends AppCompatActivity implements UnitListFragment.ArmyProvider,
        UnitListFragment.UnitSelectedListener {

    public static final String TRANSITION_IMAGE = "Transition:image";
    public static final String TRANSITION_UNIT_NAME = "Transition:unit_name";


    private static final String TAG = UnitListActivity.class.getSimpleName();
    private static Army m_army;
    private Map<String, Weapon> m_weapons;

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

        WeaponsData weaponsData = new WeaponsData(this);
        weaponsData.open();
        m_weapons = weaponsData.getWeapons();
        weaponsData.close();

        Log.d(TAG, m_army.toString());

        setContentView(R.layout.activity_unit_list);

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
        getSupportActionBar().setTitle(m_army.name);


//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.unit_list);
//        fragment.setRetainInstance(true);
//        if (fragment instanceof UnitListFragment) {
//            ((UnitListFragment) fragment).setOnUnitSelectedListener(this);
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void unitSelected(Unit unit, UnitListAdapter.ViewHolder viewHolder) {
        Log.d(TAG, unit.toString());
        Intent intent = new Intent(this, UnitActivity.class);

        StringBuffer sb = new StringBuffer();
        sb.append(unit.toString()).append("\n");

        HashSet<String> bsw = new HashSet<>();
        HashSet<String> ccw = new HashSet<>();

        for (int i = 0; i < unit.profiles.size(); i++) {
            bsw.addAll(unit.profiles.get(i).bsw);
            ccw.addAll(unit.profiles.get(i).ccw);
        }

        for (int i = 0; i < unit.options.size(); i++) {
            bsw.addAll(unit.options.get(i).bsw);
            ccw.addAll(unit.options.get(i).ccw);
        }

        sb.append("BS Weapons\n");
        sb.append(weaponsToString(bsw));

        sb.append("\nCC Weapons\n");
        sb.append(weaponsToString(ccw));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                new Pair<View, String>(viewHolder.m_imageView, TRANSITION_IMAGE)
                //        ,new Pair<View, String>(viewHolder.m_textView, TRANSITION_UNIT_NAME)
        );

        intent.putExtra(MainActivity.UNIT, unit);
        ActivityCompat.startActivity(this, intent, options.toBundle());

        //startActivity(intent);

    }

    private String weaponsToString(Set<String> weapons) {
        StringBuffer sb = new StringBuffer();
        Iterator it = weapons.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();

            // sometimes a weapon is listed twice.  The easiest way to record this is to look for it
            // and remove the text so that the actual weapon can be found in the weapons list.
            name = name.replace("(2)", "").trim();

            Weapon weapon = m_weapons.get(name);
            if (weapon == null) {
                Log.d(TAG, "Weapon: " + name + " not found");
            }
            sb.append(weapon.toString()).append("\n");

            // check the alt_profile rabbit hole
            while (weapon.alt_profile != null) {
                weapon = m_weapons.get(weapon.alt_profile);
                sb.append(weapon.toString()).append("\n");
            }
        }
        return sb.toString();
    }

}
