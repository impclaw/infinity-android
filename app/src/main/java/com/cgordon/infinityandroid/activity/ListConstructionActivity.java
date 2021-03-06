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
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.ListConstructionAdapter;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.ArmyList;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.ListConstructionFragment;
import com.cgordon.infinityandroid.fragment.UnitFragment;
import com.cgordon.infinityandroid.fragment.UnitListFragment;
import com.cgordon.infinityandroid.interfaces.ChildSelectedListener;
import com.cgordon.infinityandroid.interfaces.UnitSource;
import com.cgordon.infinityandroid.storage.ArmyData;
import com.cgordon.infinityandroid.storage.ListData;
import com.cgordon.infinityandroid.storage.UnitsData;

import java.util.Iterator;
import java.util.List;

public class ListConstructionActivity extends AppCompatActivity
        implements UnitListFragment.UnitSelectedListener,
        ChildSelectedListener,
        ListConstructionAdapter.ListChangedListener,
        UnitSource {

    private final static String TAG = AppCompatActivity.class.getSimpleName();

    private static final String CURRENT_SELECTED_UNIT = "current_selected_unit";
    private static final String LIST_DIRTY = "list_dirty";
    public static final String POINTS = "points";

    UnitChangedListener m_unitChangedListener = null;

    private Unit m_currentSelectedUnit = null;

    private ViewPager m_pager;
    private Army m_army;
    private ListConstructionPagerAdapter m_adapter;
    private OptionSelectedListener m_optionListener;
    private ArmyStatusListener m_armyListener;
    private ListConstructionFragment m_listConstructionFragment;
    private long m_listDbId = -1;
    private boolean m_listDirty = false;
    private String m_listName = null;
    private List<Unit> m_units;
    private int m_points;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_construction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        m_pager = (ViewPager) findViewById(R.id.pager);
        m_adapter = new ListConstructionPagerAdapter(getSupportFragmentManager());
        m_pager.setAdapter(m_adapter);
        m_pager.setOffscreenPageLimit(3);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(m_pager);

        m_army = getIntent().getParcelableExtra(MainActivity.ARMY);
        if (m_army == null) {
            ArmyList armyList = getIntent().getParcelableExtra(MainActivity.LIST_ID);
            if (armyList != null) {

                ArmyData armyData = new ArmyData(getBaseContext());
                armyData.open();
                m_army = armyData.getArmy(armyList.armyId);
                armyData.close();

                m_listName = armyList.name;
                m_listDbId = armyList.dbId;
                m_points = armyList.points;
                m_pager.setCurrentItem(2);
            }
        }


        UnitsData unitsData = new UnitsData(this);
        unitsData.open();
        m_units = unitsData.getUnits(m_army);
        unitsData.close();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //notifyUnitSelected(m_currentSelectedUnit, null);
        FragmentPagerAdapter fragmentPagerAdapter = m_adapter;
        m_listConstructionFragment = (ListConstructionFragment) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + m_pager.getId() + ":"
                        + fragmentPagerAdapter.getItemId(2));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_SELECTED_UNIT, m_currentSelectedUnit);
        outState.putBoolean(LIST_DIRTY, m_listDirty);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            m_listDirty = savedInstanceState.getBoolean(LIST_DIRTY);
            m_currentSelectedUnit = savedInstanceState.getParcelable(CURRENT_SELECTED_UNIT);
        }
    }

    @Override
    public void onBackPressed() {

        if (m_listDirty) {
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
        } else {
            ListConstructionActivity.this.finish();
        }
    }

    protected void notifyUnitSelected(Unit unit) {
        if (m_unitChangedListener != null) {
            m_unitChangedListener.OnUnitChanged(unit);
        }
        m_currentSelectedUnit = unit;
        if (unit != null) {
            Log.d(TAG, "Unit Selected: " + unit.id);
        }
    }

    public void unitSelected(Unit unit, int child) {
        notifyUnitSelected(unit);
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
    public void onChildSelected(int id) {
        Log.d(TAG, "Option Selected: " + id);
        m_listDirty = true;
        m_optionListener.OnOptionSelected(m_currentSelectedUnit, id);
        Snackbar.make(m_pager, "Added " + m_currentSelectedUnit.getChild(id).name, Snackbar.LENGTH_SHORT).show();
        //m_pager.setCurrentItem(2);
    }

    public void setOptionChangedListener(OptionSelectedListener optionListener) {
        m_optionListener = optionListener;
    }

    @Override
    public void onListChanged(int cost, double swc, int lieutenantCount) {
        Log.d(TAG, "Cost: " + cost + " SWC: " + swc + " LT.: " + lieutenantCount);
        m_armyListener.OnArmyStatusChanged(cost, swc, lieutenantCount);
    }

    @Override
    public void onOrderChanged(int oldPosition, int newPosition) {
        m_listDirty = true;
    }

    @Override
    public void onUnitClicked(int unitId, int childId) {
        Intent i = new Intent(this, UnitActivity.class);
        i.putExtra(UnitActivity.UNIT, getUnit(unitId));
        i.putExtra(UnitActivity.SELECTED_CHILD_ID, childId);
        startActivity(i);
    }

    @Override
    public Unit getUnit(int id) {
        Iterator it = m_units.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (unit.id == id) {
                return unit;
            }
        }
        return null;
    }

    class ListConstructionPagerAdapter extends FragmentPagerAdapter {

        String tabs[];

        public ListConstructionPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = new String[3];
            tabs[0] = getResources().getString(R.string.UnitList);
            tabs[1] = getResources().getString(R.string.Unit);
            tabs[2] = getResources().getString(R.string.list);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle b;

            switch (position) {
                case 0:
                    fragment = new UnitListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MainActivity.ARMY, m_army);
                    fragment.setArguments(bundle);

                    break;
                case 1:
                    b = new Bundle();
                    b.putBoolean(MainActivity.CLICKABLE_CHILD, true);
                    fragment = new UnitFragment();
                    fragment.setArguments(b);
                    break;
                case 2:
                    m_listConstructionFragment = new ListConstructionFragment();
                    b = new Bundle();
                    b.putLong(MainActivity.ID, m_listDbId);
                    b.putInt(POINTS, m_points);

                    m_listConstructionFragment.setArguments(b);
                    fragment = m_listConstructionFragment;
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

    public void setArmyStatusListener(ArmyStatusListener listener) {
        m_armyListener = listener;
    }

    public interface ArmyStatusListener {
        public void OnArmyStatusChanged(int cost, double swc, int lieutenantCount);
    }

    public interface ListStatusListener {
        public void OnListStatusChanged(int regularCount, int irregularCount, int impetuousCount, int group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_construction, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                save();
                return true;

            case R.id.action_rename:
                rename();
                return true;

            case R.id.action_save_as:
                saveAs();
                return true;

            case R.id.action_delete:
                deleteList();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_set_points:
                setPoints();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(
                new String[] {"50", "100", "150", "200", "250", "300", "350", "400", "450", "500"});
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(m_points / 50);
        numberPicker.clearFocus();
        final FrameLayout parent = new FrameLayout(this);
        parent.addView(numberPicker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        builder.setView(parent);
        builder.setTitle(R.string.action_set_points);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_points = numberPicker.getValue() * 50;
                m_listConstructionFragment.setPoints(m_points);
                m_listDirty = true;
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void save() {

        if (m_listName == null) {
            saveAs();
        } else {
            saveList();
        }
    }

    private void saveAs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        if (m_listName != null) {
            editText.setText(m_listName);
        }
        builder.setView(editText);
        builder.setTitle(R.string.action_save_as);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable value = editText.getText();
                String filename = value.toString();
                if (!filename.isEmpty()) {
                    m_listName = filename;
                    // reset this so we don't delete the old one
                    m_listDbId = -1;
                    saveList();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void rename() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        if (m_listName != null) {
            editText.setText(m_listName);
        }
        builder.setView(editText);
        builder.setTitle(R.string.action_rename);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editable value = editText.getText();
                String filename = value.toString();
                if (!filename.isEmpty()) {
                    m_listName = filename;
                    m_listDirty = true;

                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void saveList() {
        // see if the name has been set
        if (m_listName != null) {

            // see if the save works:
            m_listDbId = m_listConstructionFragment.saveList(m_listName, m_army.dbId, m_points, m_listDbId);
            if (m_listDbId != -1) {
                m_listDirty = false;
                Snackbar.make(m_pager, "List Saved", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(m_pager, "Save failed!", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteList() {
        if (m_listName != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.action_delete);
            builder.setMessage(R.string.delete_list_question);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListData listData = new ListData(getBaseContext());
                    listData.open();
                    if (!listData.deleteList(m_listDbId)) {
                        Toast.makeText(getBaseContext(), R.string.error_deleting_list + m_listName, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Error deleting list: " + m_listName);
                    }
                    listData.close();
                    finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();

        } else {
            Snackbar.make(m_pager, R.string.list_not_saved, Snackbar.LENGTH_LONG).show();
        }
    }
}
