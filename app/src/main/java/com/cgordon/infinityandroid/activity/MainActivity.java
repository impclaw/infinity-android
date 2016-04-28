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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.fragment.ArmyListFragment;
import com.cgordon.infinityandroid.fragment.SavedListsFragment;

public class MainActivity extends AppCompatActivity
        implements ArmyListFragment.ArmyListListener,
        NavigationView.OnNavigationItemSelectedListener {

    // this is a big hack, but I don't want to put any more time into figuring out the Fragment
    // lifecycle
    public static Parcelable unitListScrollState = null;

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String FACTION = "faction";
    public static final String ARMY = "army";
    public static final String ID = "id";
    public static final String ABBR = "abbr";
    public static final String INDEX = "profile";
    public static final String WEAPON = "weapon";
    public static final String LIST_ID = "list_id";
    public static final String CLICKABLE_CHILD = "clickable_child";

    private ArmyListFragment m_armyListFragment = new ArmyListFragment();
    private SavedListsFragment m_savedListsFragment = new SavedListsFragment();

    private DrawerLayout m_drawerLayout;
    private ActionBarDrawerToggle m_drawerToggle;
    private final String NAVIGATION_ITEM = "navigation_item";
    private int m_navigationItem;
    private NavigationView m_navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_navigationItem = R.id.navigation_browse;
        if (savedInstanceState != null) {
            m_navigationItem = savedInstanceState.getInt(NAVIGATION_ITEM);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        m_drawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);

        m_drawerLayout.addDrawerListener(m_drawerToggle);
        m_drawerToggle.syncState();

        m_navigationView = (NavigationView) findViewById(R.id.navigation);
        m_navigationView.setNavigationItemSelectedListener(this);

        navigate(m_navigationItem);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (m_navigationItem == R.id.navigation_saved_lists) {
            m_navigationView.getMenu().getItem(0).setChecked(true);
            navigate(R.id.navigation_browse);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        m_drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(NAVIGATION_ITEM, m_navigationItem);

    }

    @Override
    public void onArmyClicked(Army army) {
        unitListScrollState = null;
        Intent i = new Intent(this, UnitListActivity.class);
        i.putExtra(MainActivity.ARMY, army);
        startActivity(i);
    }

    private void navigate(int itemId) {
        m_navigationItem = itemId;
        switch (itemId) {
            case R.id.navigation_browse:
                getSupportActionBar().setTitle(R.string.app_name);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, m_armyListFragment).commit();
                break;
            case R.id.navigation_saved_lists:
                getSupportActionBar().setTitle(R.string.navigation_saved_lists);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, m_savedListsFragment).commit();
                break;
            case R.id.navigation_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.navigation_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_about)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();

                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        navigate(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
