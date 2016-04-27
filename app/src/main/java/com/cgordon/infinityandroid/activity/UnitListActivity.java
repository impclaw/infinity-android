/*
 * Copyright 2015-2016 by Chris Gordon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cgordon.infinityandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cgordon.infinityandroid.R;
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

public class UnitListActivity extends AppCompatActivity
        implements
        UnitListFragment.UnitSelectedListener,
        View.OnClickListener {

    private static final String TAG = UnitListActivity.class.getSimpleName();
    private static Army m_army;
    private Map<String, Weapon> m_weapons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);

        if (savedInstanceState != null) {
            m_army = savedInstanceState.getParcelable(MainActivity.ARMY);
        } else {
            m_army = getIntent().getParcelableExtra(MainActivity.ARMY);
        }
        if (m_army == null) {
            onResume();
        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        UnitListFragment unitListFragment = new UnitListFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.ARMY, m_army);
        unitListFragment.setArguments(bundle);

        transaction.add(R.id.fragment_container, unitListFragment);

        transaction.commit();

        WeaponsData weaponsData = new WeaponsData(this);
        weaponsData.open();
        m_weapons = weaponsData.getWeapons();
        weaponsData.close();

        //Log.d(TAG, m_army.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(m_army.name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setClickable(true);
            fab.setOnClickListener(this);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences p = getSharedPreferences(UnitActivity.UNIT, MODE_PRIVATE);
        p.edit().putLong(MainActivity.ID, m_army.dbId).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (m_army == null) {
            SharedPreferences p = getSharedPreferences(UnitActivity.UNIT, MODE_PRIVATE);
            long dbId = p.getLong(MainActivity.ID, -1);
            ArmyData armyData = new ArmyData(this);
            armyData.open();
            m_army = armyData.getArmy(dbId);
            armyData.close();
        }
    }

    @Override
    public void unitSelected(Unit unit, int childId) {
        //Log.d(TAG, unit.toString());
        Intent intent = new Intent(this, UnitActivity.class);

        StringBuffer sb = new StringBuffer();
        sb.append(unit.toString()).append("\n");

        HashSet<String> bsw = new HashSet<>();
        HashSet<String> ccw = new HashSet<>();

        for (int i = 0; i < unit.profiles.size(); i++) {
            bsw.addAll(unit.profiles.get(i).bsw);
            ccw.addAll(unit.profiles.get(i).ccw);
        }

        for (int i = 0; i < unit.children.size(); i++) {
            bsw.addAll(unit.children.get(i).bsw);
            ccw.addAll(unit.children.get(i).ccw);
        }

        sb.append("BS Weapons\n");
        sb.append(weaponsToString(bsw));

        sb.append("\nCC Weapons\n");
        sb.append(weaponsToString(ccw));

        intent.putExtra(UnitActivity.UNIT, unit);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, ListConstructionActivity.class);
        i.putExtra(MainActivity.ARMY, m_army);
        startActivity(i);
    }
}
