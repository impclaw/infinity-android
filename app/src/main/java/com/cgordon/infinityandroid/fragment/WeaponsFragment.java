package com.cgordon.infinityandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.adapter.WeaponsAdapter;
import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgordon on 7/23/2015.
 */
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
