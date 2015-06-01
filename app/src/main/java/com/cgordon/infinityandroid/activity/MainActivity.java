package com.cgordon.infinityandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.json.UnitParser;
import com.cgordon.infinityandroid.json.SectorialParser;
import com.cgordon.infinityandroid.json.WeaponParser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_load) {
            UnitParser up = new UnitParser(this);

            Log.d(TAG, "Tohaa " + up.parse(R.raw.toha_units).size());
            Log.d(TAG, "PanO " + up.parse(R.raw.pano_units).size());
            Log.d(TAG, "YuJing " + up.parse(R.raw.yuji_units).size());
            Log.d(TAG, "Ariadna " + up.parse(R.raw.aria_units).size());
            Log.d(TAG, "Haqqislam " + up.parse(R.raw.haqq_units).size());
            Log.d(TAG, "Nomads " + up.parse(R.raw.noma_units).size());
            Log.d(TAG, "CA " + up.parse(R.raw.comb_units).size());
            Log.d(TAG, "Aleph " + up.parse(R.raw.alep_units).size());
            Log.d(TAG, "merc " + up.parse(R.raw.merc_units).size());
            Log.d(TAG, "Other " + up.parse(R.raw.other_units).size());

            SectorialParser sp = new SectorialParser(this);
            Log.d(TAG, "Sectorials: " + sp.parse(R.raw.sectorials).size());

            WeaponParser wp = new WeaponParser(this);
            Log.d(TAG, "Weapons: " + wp.parse(R.raw.weapons).size());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
