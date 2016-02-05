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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.ListConstructionAdapter;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.ArmyStatusFragment;
import com.cgordon.infinityandroid.fragment.ListConstructionFragment;
import com.cgordon.infinityandroid.fragment.OptionsFragment;
import com.cgordon.infinityandroid.fragment.UnitFragment;
import com.cgordon.infinityandroid.fragment.UnitListFragment;
import com.cgordon.infinityandroid.widgets.SlidingTabLayout;

public class ListConstructionActivity extends AppCompatActivity
    implements UnitListFragment.UnitSelectedListener,
        OptionsFragment.OnOptionSelectedListener,
        ListConstructionAdapter.ListChangedListener
{

    UnitChangedListener m_unitChangedListener = null;

    private Unit m_currentSelectedUnit = null;

    private final static String TAG = AppCompatActivity.class.getSimpleName();
    private ViewPager m_pager;
    private Army m_army;
    private ListConstructionPagerAdapter m_adapter;
    private OptionSelectedListener m_optionListener;
    private ListStatusListener m_listStatusListener;
    private ArmyStatusListener m_armyListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_construction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
        //getSupportActionBar().setTitle(m_unit.isc);

        m_pager = (ViewPager) findViewById(R.id.pager);
        m_adapter = new ListConstructionPagerAdapter(getSupportFragmentManager());
        m_pager.setAdapter(m_adapter);
        m_pager.setOffscreenPageLimit(3);
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setViewPager(m_pager);
        m_army = getIntent().getParcelableExtra(MainActivity.ARMY);

        if (savedInstanceState != null) {
            m_currentSelectedUnit = savedInstanceState.getParcelable("unit");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //notifyUnitSelected(m_currentSelectedUnit, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("unit", m_currentSelectedUnit);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.cancel_list_title);
        alert.setMessage(R.string.cancel_list_message);

        alert.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        ListConstructionActivity.this.finish();
                    }
                });

        alert.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                    }
                });

        alert.show();
    }

    protected void notifyUnitSelected (Unit unit, UnitListAdapter.ViewHolder viewHolder) {
        if (m_unitChangedListener != null) {
            m_unitChangedListener.OnUnitChanged(unit);
        }
        m_currentSelectedUnit = unit;
        if (unit != null) {
            Log.d(TAG, "Unit Selected: " + unit.dbId);
        }
    }

    public void unitSelected(Unit unit, UnitListAdapter.ViewHolder viewHolder) {
        notifyUnitSelected(unit, viewHolder);
        m_pager.setCurrentItem(1);
    }

    public void addUnitChangedListener(UnitChangedListener listener) {
        m_unitChangedListener = listener;
        //m_unitChangedListener.OnUnitChanged(m_currentSelectedUnit);
    }

    public Unit getUnit() {
        return m_currentSelectedUnit;
    }

    @Override
    public void onOptionSelected(int option) {
        Log.d(TAG, "Option Selected: " + option);
        m_optionListener.OnOptionSelected(m_currentSelectedUnit, option);
        m_pager.setCurrentItem(2);
    }

    public void setOptionChangedListener(OptionSelectedListener optionListener) {
        m_optionListener = optionListener;
    }

    public void setListStatusChangedListener( ListStatusListener listStatusListener) {
        m_listStatusListener = listStatusListener;
    }

    @Override
    public void onListChanged(int cost, double swc, int lieutenantCount, int regularCount, int irregularCount, int impetuousCount) {
        Log.d(TAG, "Cost: " + cost + " SWC: " + swc + " LT.: " + lieutenantCount);
        m_armyListener.OnArmyStatusChanged(cost, swc, lieutenantCount);
    }

    class ListConstructionPagerAdapter extends FragmentPagerAdapter {

        String tabs[];
        public ListConstructionPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = new String[3];
            tabs[0] = getResources().getString(R.string.UnitList);
            tabs[1] = getResources().getString(R.string.Unit);
            tabs[2] = getResources().getString(R.string.group1);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new UnitListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MainActivity.ARMY, m_army);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = new UnitFragment();
                    break;
                case 2:
                    fragment = new ListConstructionFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

    public interface UnitChangedListener {
        public void OnUnitChanged(Unit unit);
    }

    public interface OptionSelectedListener {
        public void OnOptionSelected(Unit unitDBid, int option);
    }

    public void setArmyStatusListener( ArmyStatusListener listener) {
        m_armyListener = listener;
    }

    public interface ArmyStatusListener {
        public void OnArmyStatusChanged(int cost, double swc, int lieutenantCount);
    }

    public interface ListStatusListener {
        public void OnListStatusChanged(int regularCount, int irregularCount, int impetuousCount, int group);
    }

}
