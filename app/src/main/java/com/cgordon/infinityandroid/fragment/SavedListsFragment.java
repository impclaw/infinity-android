/*
 * Copyright 2016 by Chris Gordon
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


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.ArmyAdapter;
import com.cgordon.infinityandroid.adapter.SavedListsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedListsFragment extends Fragment {


    private static final String TAG = SavedListsFragment.class.getSimpleName();
    private RecyclerView m_recyclerView;
    private SavedListsAdapter m_adapter;

    public SavedListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_lists, container, false);

        m_recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        m_recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        m_recyclerView.setLayoutManager(layoutManager);
        m_adapter= new SavedListsAdapter(getActivity());
        m_recyclerView.setAdapter(m_adapter);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        m_adapter.refresh();
    }
}
