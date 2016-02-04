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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.adapter.ListConstructionAdapter;
import com.cgordon.infinityandroid.data.Unit;

public class ListConstructionFragment extends Fragment
        implements ListConstructionActivity.OptionSelectedListener {

    private static final String TAG = ListConstructionFragment.class.getSimpleName();

    private static Parcelable m_scrollState = null;

    private RecyclerView m_recyclerView;
    private ListStatusFragment m_listStatus;

    private ListConstructionAdapter m_adapter;
    private TextView m_ordersRegular;
    private TextView m_ordersIrregular;
    private TextView m_ordersImpetuous;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_construction, container, false);

        setRetainInstance(true);

        m_recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        m_listStatus = (ListStatusFragment) getChildFragmentManager().findFragmentById(R.id.list_status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        m_recyclerView.setLayoutManager(layoutManager);
        if (m_adapter == null) {
            m_adapter = new ListConstructionAdapter(getActivity());
        }
        if (getActivity() instanceof ListConstructionAdapter.ListChangedListener) {
            m_adapter.addListChangedListener((ListConstructionAdapter.ListChangedListener) getActivity());
            m_adapter.addListChangedListener(m_listStatus);
        }

        m_recyclerView.setAdapter(m_adapter);

        return v;
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

}
