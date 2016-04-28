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

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.ArmyAdapter;
import com.cgordon.infinityandroid.data.Army;

public class ArmyListFragment extends Fragment {

    private static Parcelable m_scrollState = null;

    private RecyclerView m_recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        m_recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.narrow_card_column_count));
        m_recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ArmyAdapter(getActivity());
        m_recyclerView.setAdapter(adapter);

        return view;
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
    }

    public interface ArmyListListener {

        public void onArmyClicked(Army army);
    }

}
