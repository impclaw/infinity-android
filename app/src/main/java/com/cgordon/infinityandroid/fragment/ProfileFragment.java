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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.activity.UnitListActivity;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle arguments = getArguments();
        Unit unit = arguments.getParcelable(MainActivity.UNIT);
        int profileId = arguments.getInt(MainActivity.INDEX);
        Profile profile = unit.profiles.get(profileId);

        TextView stat = (TextView) view.findViewById(R.id.mov);
        stat.setText(profile.mov);

        stat = (TextView) view.findViewById(R.id.cc);
        stat.setText(profile.cc);

        stat = (TextView) view.findViewById(R.id.bs);
        stat.setText(profile.bs);

        stat = (TextView) view.findViewById(R.id.ph);
        stat.setText(profile.ph);

        stat = (TextView) view.findViewById(R.id.wip);
        stat.setText(profile.wip);

        stat = (TextView) view.findViewById(R.id.arm);
        stat.setText(profile.arm);

        stat = (TextView) view.findViewById(R.id.bts);
        stat.setText(profile.bts);

        stat = (TextView) view.findViewById(R.id.wounds);
        stat.setText(profile.wounds);

        stat = (TextView) view.findViewById(R.id.silhouette);
        stat.setText(profile.silhouette);

        stat = (TextView) view.findViewById(R.id.ava);
        if ((profile.ava != null) && (profile.ava.isEmpty())) {
            stat.setText(profile.ava);
        } else {
            stat.setText(unit.ava);
        }

        TextView isc = (TextView) view.findViewById(R.id.isc);
        String iscText = profile.name;
        if ((iscText == null) || (iscText.isEmpty())) {
            iscText = unit.isc;
        }
        isc.setText(iscText);

        TextView type = (TextView) view.findViewById(R.id.type);
        type.setText(profile.type);

        TextView note = (TextView) view.findViewById(R.id.note);
        if ((profile.note != null) && (!profile.note.isEmpty())) {
            note.setText("Note: " + profile.note);
        } else {
            note.setVisibility(View.GONE);
        }

        TextView spec = (TextView) view.findViewById(R.id.spec);
        // TODO: Get the expanded list of special options.
        if ((profile.spec == null) || (profile.spec.isEmpty())) {
            spec.setVisibility(View.GONE);
        } else {
            spec.setText("Spec: " + TextUtils.join(", ", profile.spec));
        }

        TextView bsw = (TextView) view.findViewById(R.id.bsw);
        if ((profile.bsw == null) || (profile.bsw.size() == 0)) {
            bsw.setVisibility(View.GONE);
        } else {
            bsw.setText("BS Weapons: " + TextUtils.join(", ", profile.bsw));
        }

        TextView ccw = (TextView) view.findViewById(R.id.ccw);
        if ((profile.ccw == null) || (profile.ccw.size() == 0)) {
            ccw.setVisibility(View.GONE);
        } else {
            ccw.setText("CC Weapons: " + TextUtils.join(", ", profile.ccw));
        }

        ImageView irr = (ImageView) view.findViewById(R.id.irr);
        if (profile.irr) {
            irr.setImageResource(R.drawable.irregular);
        } else {
            irr.setImageResource(R.drawable.regular);
        }

        ImageView imp = (ImageView) view.findViewById(R.id.imp);
        if (profile.imp != null) {
            if (profile.imp.equals("F")) {
                imp.setImageResource(R.drawable.frenzy);
            } else if (profile.imp.equals("X")) {
                imp.setImageResource(R.drawable.extreme_impetuous);
            } else if (profile.imp.equals("I")) {
                imp.setImageResource(R.drawable.impetuous);
            }
        }

        ImageView cube = (ImageView) view.findViewById(R.id.cube);
        if (profile.cube != null) {
            if (profile.cube.equals("X")) {
                cube.setImageResource(R.drawable.cube);
            } else if (profile.cube.equals("2")) {
                cube.setImageResource(R.drawable.cube2);
            }
        }

        if (profile.hackable) {
            ImageView hackable = (ImageView) view.findViewById(R.id.hackable);
            hackable.setImageResource(R.drawable.hackable);
        }

        /*
    public String woundType;

        public boolean irr;
    public String imp;
    public String cube;
    public String note;

    public boolean hackable;
         */

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        int resourceId = UnitListAdapter.getDrawableResource(unit, getActivity(), 24);
        imageView.setImageResource(resourceId);

        if (profileId == 0) {
            ViewCompat.setTransitionName(imageView, UnitListActivity.TRANSITION_IMAGE);
//            ViewCompat.setTransitionName(isc, UnitListActivity.TRANSITION_UNIT_NAME);
        }


        return view;
    }

}
