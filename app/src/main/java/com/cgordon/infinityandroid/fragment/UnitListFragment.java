package com.cgordon.infinityandroid.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.storage.UnitsData;

import java.util.List;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitListFragment extends Fragment {

    public static final String ListAsListKey = "list_as_list";


    UnitListAdapter m_adapter;
    RecyclerView m_recyclerView;
    Army m_army;

    boolean m_showAsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        m_recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (prefs.contains(UnitListFragment.ListAsListKey)) {
            m_showAsList = prefs.getBoolean(UnitListFragment.ListAsListKey, false);
        }

        RecyclerView.LayoutManager layoutManager;
        if (m_showAsList) {
            layoutManager = new LinearLayoutManager(getActivity());
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        }
        m_recyclerView.setLayoutManager(layoutManager);

        UnitsData unitsData = new UnitsData(getActivity());
        unitsData.open();
        List<Unit> units = unitsData.getUnits(m_army);
        unitsData.close();

        m_adapter = new UnitListAdapter(getActivity(), units);
        m_recyclerView.setAdapter(m_adapter);


        if (getActivity() instanceof OnUnitSelectedListener) {
            m_adapter.setOnUnitSelectedListener( (OnUnitSelectedListener) getActivity() );
        }

        return view;
    }

    public interface ArmyProvider {
        public Army getArmy();
    }

    public interface OnUnitSelectedListener {
        public void unitSelected(long dbId);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ArmyProvider) {
            ArmyProvider provider = (ArmyProvider) activity;
            m_army = provider.getArmy();
        }
    }




}
