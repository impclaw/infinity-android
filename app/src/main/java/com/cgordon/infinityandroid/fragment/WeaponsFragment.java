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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.WeaponsAdapter;
import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WeaponsFragment extends Fragment {

    private WeaponsData wd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weapons, container, false);

        Unit unit = getArguments().getParcelable(MainActivity.UNIT);

        ListView listView = (ListView) view.findViewById(R.id.list_view);


        ArrayList<String> bsw = new ArrayList<>();
        ArrayList<String> ccw = new ArrayList<>();

        Iterator it = unit.profiles.iterator();
        while (it.hasNext()) {
            Profile profile = (Profile) it.next();
            bsw.addAll(profile.bsw);
            ccw.addAll(profile.ccw);
        }

        it = unit.options.iterator();
        while (it.hasNext()) {
            Option option = (Option) it.next();
            bsw.addAll(option.bsw);
            ccw.addAll(option.ccw);
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

        listView.setAdapter(new WeaponsAdapter(getActivity(), R.layout.row_weapon, bsw));

        return view;
    }
}
