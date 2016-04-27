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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.fragment.ArmyListFragment;

public class MainActivity extends AppCompatActivity implements ArmyListFragment.ArmyListListener {

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

    private DrawerLayout m_drawerLayout;
    private ActionBarDrawerToggle m_drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.tohaa_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final View content = findViewById(R.id.content);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                m_drawerLayout.closeDrawers();
                return true;
            }
        });
        m_drawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout, toolbar
                , R.string.drawer_open, R.string.drawer_closed ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        m_drawerLayout.setDrawerListener(m_drawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        m_drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_about: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_about)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();

                return true;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case android.R.id.home:
                m_drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArmyClicked(Army army) {
        unitListScrollState = null;
        Intent i = new Intent(this, UnitListActivity.class);
        i.putExtra(MainActivity.ARMY, army);
        startActivity(i);
    }
}
