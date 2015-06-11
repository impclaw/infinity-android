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

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.BrowsePagerAdapter;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitListFragment extends Fragment implements UnitListAdapter.OnUnitSelectedListener {

    private UnitSelectedListener m_listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        UnitListAdapter adapter = new UnitListAdapter();
        adapter.setOnUnitSelectedListener(this);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UnitSelectedListener) {
            m_listener = (UnitSelectedListener) activity;
        }
    }

    @Override
    public void unitSelected(long dbId) {
        if (m_listener != null) {
            m_listener.unitSelected(dbId);
        }
    }

    public interface UnitSelectedListener {

        /**
         * Returns the database id of the unit in the units table
         * @param dbId id of the element, -1 if not known
         */
        public void unitSelected(long dbId);
    }
}
