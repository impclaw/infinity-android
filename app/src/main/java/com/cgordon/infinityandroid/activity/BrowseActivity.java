package com.cgordon.infinityandroid.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.BrowsePagerAdapter;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.UnitFragment;
import com.cgordon.infinityandroid.fragment.UnitListFragment;

public class BrowseActivity extends AppCompatActivity
        implements
            UnitListFragment.OnUnitSelectedListener,
            UnitListFragment.ArmyProvider

{
    private static final String TAG = BrowseActivity.class.getSimpleName();

    private ViewPager m_viewPager;
    private Army m_army;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        m_army = new Army();
        m_army.name = getIntent().getStringExtra(MainActivity.ARMY);
        m_army.faction = getIntent().getStringExtra(MainActivity.FACTION);
        m_army.dbId = getIntent().getLongExtra(MainActivity.ID, -1);
        m_army.abbr = getIntent().getStringExtra(MainActivity.ABBR);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(m_army.name);

        m_viewPager = (ViewPager) findViewById(R.id.pager);
        m_viewPager.setAdapter(new BrowsePagerAdapter(getSupportFragmentManager()));

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_browse, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void unitSelected(Unit unit) {
        Log.d(TAG, "Received listener id: " + unit.dbId);

        Fragment unitFragment = (Fragment) m_viewPager.getAdapter().instantiateItem(m_viewPager, 1);
        if (unitFragment instanceof UnitFragment) {
            ((UnitFragment) unitFragment).setUnit(unit);
            m_viewPager.setCurrentItem(1);
        }
    }

    @Override
    public Army getArmy() {
        return m_army;
    }

}
