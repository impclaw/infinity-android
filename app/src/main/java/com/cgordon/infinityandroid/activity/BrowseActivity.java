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
import com.cgordon.infinityandroid.fragment.UnitFragment;
import com.cgordon.infinityandroid.fragment.UnitListFragment;

public class BrowseActivity extends AppCompatActivity implements UnitListFragment.UnitSelectedListener {

    private static final String TAG = BrowseActivity.class.getSimpleName();

    private ViewPager m_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        String army = getIntent().getStringExtra(MainActivity.ARMY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(army);

        m_viewPager = (ViewPager) findViewById(R.id.pager);
        m_viewPager.setAdapter(new BrowsePagerAdapter(getSupportFragmentManager(), army));

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
    public void unitSelected(long dbId) {
        Log.d(TAG, "Received listener id: " + dbId);

        Fragment unit = (Fragment) m_viewPager.getAdapter().instantiateItem(m_viewPager, 1);
        if (unit instanceof UnitFragment) {
            ((UnitFragment) unit).setId(dbId);
        }
    }
}
