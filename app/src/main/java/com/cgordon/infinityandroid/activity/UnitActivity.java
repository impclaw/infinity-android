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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.UnitFragment;
import com.cgordon.infinityandroid.interfaces.ChildSelectedListener;

public class UnitActivity extends AppCompatActivity
        implements ChildSelectedListener, UnitFragment.UnitChangeSource {

    private static final String TAG = UnitActivity.class.getSimpleName();

    public static final String UNIT = "unit";
    public final static String SELECTED_CHILD_ID = "selected_child_id";

    private Unit m_unit;

    private int m_selectedChildId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        UnitFragment unitFragment = (UnitFragment) getSupportFragmentManager().findFragmentById(R.id.unit);

        m_unit = getIntent().getParcelableExtra(UNIT);
        m_selectedChildId = getIntent().getIntExtra(SELECTED_CHILD_ID, -1);

        unitFragment.setUnit(m_unit, m_selectedChildId);

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
        getSupportActionBar().setTitle(m_unit.isc);
    }

    @Override
    public void onChildSelected(int option) {
        Log.d(TAG, "option selected: " + option);
    }

    @Override
    public Unit getUnit() {
        return m_unit;
    }
}
