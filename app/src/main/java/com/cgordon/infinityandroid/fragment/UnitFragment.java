/*
 * Copyright 2015 by Chris Gordon
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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.WeaponsAdapter;
import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UnitFragment extends Fragment implements ListConstructionActivity.UnitChangedListener {

    private final static String TAG = UnitFragment.class.getSimpleName();

    private final static String FORWARD_OBSERVER = "Forward Observer";

    Unit m_unit;

    List<Fragment> m_fragments;
    private LinearLayout m_linearLayout;
    private ScrollView m_scrollview;

    public UnitFragment() {
        m_unit = null;
        m_fragments = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unit, container, false);

        m_linearLayout = (LinearLayout) v.findViewById(R.id.weapon_container);
        m_scrollview = (ScrollView) v.findViewById(R.id.scrollView);

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
    }

    public void setUnit(Unit unit) {
        m_scrollview.scrollTo(0,0);
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

        for (int i = 0; i < m_unit.options.size(); i++) {
            OptionsFragment optionsFragment = new OptionsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MainActivity.UNIT, m_unit);
            bundle.putInt(MainActivity.INDEX, i);
            optionsFragment.setArguments(bundle);


            m_fragments.add(0, optionsFragment);

            transaction.add(R.id.fragment_container, optionsFragment);
        }


        transaction.commit();


        ArrayList<String> bsw = new ArrayList<>();
        ArrayList<String> ccw = new ArrayList<>();

        boolean forwardObserver = false;
        boolean impersonation = false;

        Iterator it = unit.profiles.iterator();
        while (it.hasNext()) {
            Profile profile = (Profile) it.next();
            bsw.addAll(profile.bsw);
            ccw.addAll(profile.ccw);
            if (profile.spec.indexOf(FORWARD_OBSERVER) != -1) {
                forwardObserver = true;
            }
        }

        it = unit.options.iterator();
        while (it.hasNext()) {
            Option option = (Option) it.next();
            bsw.addAll(option.bsw);
            ccw.addAll(option.ccw);
            if (option.spec.indexOf(FORWARD_OBSERVER) != -1) {
                forwardObserver = true;
            }
        }

        if (forwardObserver) {
            bsw.add(FORWARD_OBSERVER);
            bsw.add("Flash Pulse");
        }


        // remove duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(bsw);
        bsw.clear();
        bsw.addAll(hs);

        hs.clear();
        hs.addAll(ccw);
        ccw.clear();
        ccw.addAll(hs);

        Collections.sort(bsw, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });

        Collections.sort(ccw, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });

        bsw.addAll(ccw);

        m_linearLayout.removeAllViews();

        WeaponsAdapter adapter = new WeaponsAdapter(getActivity(), R.layout.row_weapon, bsw);
        for (int i = 0; i < adapter.getCount(); i++) {
            m_linearLayout.addView(adapter.getView(i, null, null));
        }

    }

    @Override
    public void OnUnitChanged(Unit unit) {
        setUnit(unit);
    }

    public void optionClicked(View view) {
        Log.d(TAG, "option clicked: " + view);

    }

}
