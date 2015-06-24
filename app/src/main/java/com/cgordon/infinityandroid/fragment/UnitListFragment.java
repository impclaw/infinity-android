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
    private static final String TAG = UnitListFragment.class.getSimpleName();

    UnitListAdapter m_adapter;
    RecyclerView m_recyclerView;
    Army m_army;

    boolean m_showAsList;
    private UnitSelectedListener m_unitSelectedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        FragmentActivity activity = getActivity();
        if (activity instanceof ArmyProvider) {
            m_army = ((ArmyProvider) activity).getArmy();
        }

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
            layoutManager = new GridLayoutManager(getActivity(),
                    getActivity().getResources().getInteger(R.integer.card_column_count));
        }
        m_recyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            m_army = savedInstanceState.getParcelable(MainActivity.ARMY);
        }

        UnitsData unitsData = new UnitsData(getActivity());
        unitsData.open();
        List<Unit> units = unitsData.getUnits(m_army);
        unitsData.close();

        m_adapter = new UnitListAdapter(getActivity(), units);
        m_recyclerView.setAdapter(m_adapter);


        return view;
    }

    public void setOnUnitSelectedListener(UnitSelectedListener unitSelectedListener) {
        m_unitSelectedListener = unitSelectedListener;
    }

    public interface ArmyProvider {
        public Army getArmy();
    }

    public interface UnitSelectedListener {
        public void unitSelected(Unit unit);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.ARMY, m_army);
    }

}
