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

package com.cgordon.infinityandroid.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.ComparatorName;
import com.cgordon.infinityandroid.data.ComparatorType;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.storage.UnitsData;

import java.util.Collections;
import java.util.List;

public class UnitListFragment extends Fragment {

    public static final String ListAsListKey = "list_as_list";
    public static final String AlphaUnitListKey = "alpha_unit_list";

    private static final String TAG = UnitListFragment.class.getSimpleName();

    UnitListAdapter m_adapter;
    RecyclerView m_recyclerView;
    Army m_army;

    private UnitSelectedListener m_unitSelectedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        m_army = getArguments().getParcelable(MainActivity.ARMY);

        Log.d(TAG, "m_army: " + m_army);

//        FragmentActivity activity = getActivity();
//        if (activity instanceof ArmyProvider) {
//            m_army = ((ArmyProvider) activity).getArmy();
//        }

        m_recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean showAsList = false;
        if (prefs.contains(UnitListFragment.ListAsListKey)) {
            showAsList = prefs.getBoolean(UnitListFragment.ListAsListKey, false);
        }

        RecyclerView.LayoutManager layoutManager;
        if (showAsList) {
            layoutManager = new LinearLayoutManager(getActivity());
        } else {
            layoutManager = new GridLayoutManager(getActivity(),
                    getActivity().getResources().getInteger(R.integer.card_column_count));
        }
        m_recyclerView.setLayoutManager(layoutManager);

        // changed orientation
        if (savedInstanceState != null) {
            m_army = savedInstanceState.getParcelable(MainActivity.ARMY);
        }

        UnitsData unitsData = new UnitsData(getActivity());
        unitsData.open();
        List<Unit> units = unitsData.getUnits(m_army);
        unitsData.close();

        boolean alphaList = false;
        if (prefs.contains(UnitListFragment.AlphaUnitListKey)) {
            alphaList = prefs.getBoolean(UnitListFragment.AlphaUnitListKey, false);
        }

        if (units != null) {
            if (alphaList) {
                Collections.sort(units, new ComparatorName(showAsList));
            } else {
                Collections.sort(units, new ComparatorType(showAsList));
            }
        }

        m_adapter = new UnitListAdapter(getActivity(), units);
        m_recyclerView.setAdapter(m_adapter);

        return view;
    }

    public interface UnitSelectedListener {
        public void unitSelected(Unit unit, final RecyclerView.ViewHolder viewHolder);
    }

    // For screen orientation change
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.ARMY, m_army);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.unitListScrollState = m_recyclerView.getLayoutManager().onSaveInstanceState();

    }

    @Override
    public void onResume() {
        super.onResume();
        m_recyclerView.getLayoutManager().onRestoreInstanceState(MainActivity.unitListScrollState);
    }

}
