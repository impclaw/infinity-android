package com.cgordon.infinityandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitFragment extends Fragment {

    private final static String TAG = UnitFragment.class.getSimpleName();

    Unit m_unit;

    List<Fragment> m_fragments;

    public UnitFragment() {
        m_unit = null;
        m_fragments = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, getActivity().toString());
        View v = inflater.inflate(R.layout.fragment_unit, container, false);

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //profileFragment.setArguments(getIntent().getExtras());

        return v;
    }

    public void setUnit(Unit unit) {
        Log.d(TAG, "UnitFragment setId: " + unit.dbId);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (m_unit != null) {
            Iterator it = m_fragments.iterator();
            while (it.hasNext()) {
                transaction.remove((Fragment) it.next());
            }
        }

        m_unit = unit;


        for (int i = 0; i < m_unit.profiles.size(); i++) {

            ProfileFragment profileFragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MainActivity.UNIT, m_unit);
            bundle.putInt(MainActivity.INDEX, i);
            profileFragment.setArguments(bundle);

            m_fragments.add(0, profileFragment);
            transaction.add(R.id.fragment_container, profileFragment);

        }

        for (int i  = 0; i < m_unit.options.size(); i++) {
            OptionsFragment optionsFragment = new OptionsFragment();
            Bundle bundle  = new Bundle();
            bundle.putParcelable(MainActivity.UNIT, m_unit);
            bundle.putInt(MainActivity.INDEX, i);
            optionsFragment.setArguments(bundle);

            m_fragments.add(0, optionsFragment);

            transaction.add(R.id.fragment_container, optionsFragment);
        }

        transaction.commit();


    }

}
