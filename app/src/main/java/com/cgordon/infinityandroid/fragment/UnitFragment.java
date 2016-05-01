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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.activity.UnitActivity;
import com.cgordon.infinityandroid.adapter.UnitAdapter;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.interfaces.ChildSelectedListener;

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
    private GridLayoutManager m_layoutManager;
    private int m_selectedChildId;

    public UnitFragment() {
        m_unit = null;
        m_fragments = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        if (savedInstanceState != null) {
            m_unit = savedInstanceState.getParcelable(UnitActivity.UNIT);
            m_selectedChildId = savedInstanceState.getInt(UnitActivity.SELECTED_CHILD_ID);
        }

        m_recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        m_layoutManager = new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.wide_card_column_count));

        m_layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (m_unit != null) {
                    // profiles are full width.  Everything else gets gridded.
                    if (m_unit.profiles.size() > position) {
                        return getActivity().getResources().getInteger(R.integer.wide_card_column_count);
                    }
                }
                return 1;
            }
        });

        m_recyclerView.setLayoutManager(m_layoutManager);

        boolean clickable = false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            clickable = arguments.getBoolean(MainActivity.CLICKABLE_CHILD, false);
        }

        Activity activity = getActivity();
        ChildSelectedListener childSelectedListener = null;
        if (activity instanceof ChildSelectedListener) {
            childSelectedListener = (ChildSelectedListener) activity;
        }
        m_adapter = new UnitAdapter(getActivity(), childSelectedListener, clickable);

        m_recyclerView.setAdapter(m_adapter);


        setUnit(m_unit);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ListConstructionActivity) {
            ((ListConstructionActivity) activity).addUnitChangedListener(this);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(UnitActivity.UNIT, m_unit);
        outState.putInt(UnitActivity.SELECTED_CHILD_ID, m_selectedChildId);
    }

    public void setUnit(Unit unit) {
        setUnit(unit, -1);
    }

    public void setUnit(Unit unit, int selectedChildId) {
        if (unit != null) {
            m_adapter.setUnit(unit, selectedChildId);
        }
        m_unit = unit;
        m_selectedChildId = selectedChildId;

        m_layoutManager.scrollToPosition(0); //scrollToPositionWithOffset(0, 0);

    }

    @Override
    public void OnUnitChanged(Unit unit) {
        setUnit(unit);
    }

    public interface UnitChangeSource {
        public Unit getUnit();
    }

}
