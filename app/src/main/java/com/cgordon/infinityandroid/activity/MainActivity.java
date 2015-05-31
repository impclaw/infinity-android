package com.cgordon.infinityandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.json.ArmyParser;
import com.cgordon.infinityandroid.json.SectorialParser;
import com.cgordon.infinityandroid.json.WeaponParser;

public class MainActivity extends AppCompatActivity {

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
            ArmyParser ap = new ArmyParser(this);

            ap.parseArmy(R.raw.toha_units);
            ap.parseArmy(R.raw.pano_units);
            ap.parseArmy(R.raw.yuji_units);
            ap.parseArmy(R.raw.aria_units);
            ap.parseArmy(R.raw.haqq_units);
            ap.parseArmy(R.raw.noma_units);
            ap.parseArmy(R.raw.comb_units);
            ap.parseArmy(R.raw.alep_units);
            ap.parseArmy(R.raw.merc_units);
            ap.parseArmy(R.raw.other_units);

            SectorialParser sp = new SectorialParser(this);
            sp.parse(R.raw.sectorials);

            WeaponParser wp = new WeaponParser(this);
            wp.parse(R.raw.weapons);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
