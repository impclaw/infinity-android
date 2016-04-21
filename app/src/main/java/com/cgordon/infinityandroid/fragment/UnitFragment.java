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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.UnitAdapter;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.interfaces.ChildSelectedListener;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnitFragment extends Fragment implements ListConstructionActivity.UnitChangedListener {

    private final static String TAG = UnitFragment.class.getSimpleName();

    Unit m_unit;

    List<Fragment> m_fragments;
    private Map<String, Weapon> m_weaponsList;
    private RecyclerView m_recyclerView;
    private UnitAdapter m_adapter;
    private LinearLayoutManager m_layoutManager;

    public UnitFragment() {
        m_unit = null;
        m_fragments = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null) {
//            m_unit = savedInstanceState.getParcelable(MainActivity.UNIT);
//        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        if (savedInstanceState != null) {
            m_unit = savedInstanceState.getParcelable(MainActivity.UNIT);
        }


        setRetainInstance(true);

        m_recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        m_layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        m_recyclerView.setLayoutManager(m_layoutManager);

        boolean clickable = false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            clickable = arguments.getBoolean(MainActivity.CLICKABLE_CHILD, false);
        }

        if (m_adapter == null) {
            Activity activity = getActivity();
            ChildSelectedListener childSelectedListener = null;
            if (activity instanceof ChildSelectedListener) {
                childSelectedListener = (ChildSelectedListener) activity;
            }
            m_adapter = new UnitAdapter(getActivity(), childSelectedListener, clickable);
        }

        m_recyclerView.setAdapter(m_adapter);


        if (savedInstanceState == null) {
            setUnit(m_unit);
        }

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //profileFragment.setArguments(getIntent().getExtras());

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ListConstructionActivity) {
            ((ListConstructionActivity) activity).addUnitChangedListener(this);
        }

        if (activity instanceof UnitChangeSource) {
            m_unit = ((UnitChangeSource) activity).getUnit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.UNIT, m_unit);
    }

    public void setUnit(Unit unit) {
        if (unit != null) {
            m_adapter.setUnit(unit);
        }
        m_unit = unit;

        m_layoutManager.scrollToPositionWithOffset(0, 0);

    }

    @Override
    public void OnUnitChanged(Unit unit) {
        setUnit(unit);
    }

    public interface UnitChangeSource {
        public Unit getUnit();
    }

}
