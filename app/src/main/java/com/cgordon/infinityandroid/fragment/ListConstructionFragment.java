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
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.ListConstructionAdapter;
import com.cgordon.infinityandroid.data.ListElement;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.interfaces.UnitSource;
import com.cgordon.infinityandroid.storage.ListData;

import java.util.ArrayList;

import static com.cgordon.infinityandroid.adapter.ListConstructionAdapter.ListChangedListener;
import static com.cgordon.infinityandroid.adapter.ListConstructionAdapter.ListConstructionTouchHelper;

public class ListConstructionFragment extends Fragment
        implements ListConstructionActivity.OptionSelectedListener {

    private static final String TAG = ListConstructionFragment.class.getSimpleName();

    private static final String CURRENT_LIST = "current_list";

    private static Parcelable m_scrollState = null;

    private RecyclerView m_recyclerView;

    private ListConstructionAdapter m_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        setRetainInstance(true);

        m_recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.wide_card_column_count));
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return m_adapter.getSpanSize(position);
            }
        });

        m_recyclerView.setLayoutManager(layoutManager);

        if (m_adapter == null) {
            m_adapter = new ListConstructionAdapter(getActivity(), (UnitSource) getActivity());
        }

        if (savedInstanceState != null) {
            ArrayList<ListElement> list = savedInstanceState.getParcelableArrayList(CURRENT_LIST);
            m_adapter.setList(list);
        } else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                m_adapter.loadSavedList(arguments.getLong(MainActivity.ID, -1));
                m_adapter.setPoints(arguments.getInt(ListConstructionActivity.POINTS));
            }
        }
        if (getActivity() instanceof ListChangedListener) {
            m_adapter.addListChangedListener((ListChangedListener) getActivity());
        }

        ItemTouchHelper.Callback callback = new ListConstructionTouchHelper(m_adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(m_recyclerView);

        m_recyclerView.setAdapter(m_adapter);

        return v;
    }

    public void setPoints(int points) {
        m_adapter.setPoints(points);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof ListChangedListener) {
            m_adapter.removeListChangedListener((ListChangedListener) getActivity());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CURRENT_LIST, m_adapter.getList());
    }

    @Override
    public void onPause() {
        super.onPause();
        m_scrollState = m_recyclerView.getLayoutManager().onSaveInstanceState();

    }

    @Override
    public void onResume() {
        super.onResume();
        m_recyclerView.getLayoutManager().onRestoreInstanceState(m_scrollState);
        m_adapter.updateListener();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ListConstructionActivity) {
            ((ListConstructionActivity) activity).setOptionChangedListener(this);

        }

    }

    @Override
    public void OnOptionSelected(Unit unit, int option) {
        m_adapter.addUnit(unit, option);
    }

    public long saveList(String listName, long armyDbId, int points, long dbId) {
        ListData savedLists = new ListData(getActivity());
        savedLists.open();
        long retval = savedLists.saveList(listName, armyDbId, points, m_adapter.getList(), dbId);
        savedLists.close();
        return retval;
    }


}
