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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Unit;

public class OptionsFragment extends Fragment {

    private static final String TAG = OptionsFragment.class.getSimpleName();

    private OnOptionSelectedListener m_callback;

    public interface OnOptionSelectedListener {
        public void onOptionSelected(int option);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            m_callback = (OnOptionSelectedListener) activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + " must implement OnOptionSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        Bundle arguments = getArguments();
        Unit unit = arguments.getParcelable(MainActivity.UNIT);
        final int index = arguments.getInt(MainActivity.INDEX);


        CardView card = (CardView) view.findViewById(R.id.card_view);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Option Clicked");
                m_callback.onOptionSelected(index);
            }
        });



        Option option = unit.options.get(index);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(option.code);

        TextView swc = (TextView) view.findViewById(R.id.swc);
        swc.setText("SWC: " + Double.toString(option.swc));

        TextView cost = (TextView) view.findViewById(R.id.cost);
        cost.setText("C: " + Integer.toString(option.cost));

        TextView bsw = (TextView) view.findViewById(R.id.bsw);
        if (option.bsw != null && option.bsw.size() > 0) {
            bsw.setText("BSW: " + TextUtils.join(", ", option.bsw));
        } else {
            bsw.setVisibility(View.GONE);
        }

        TextView ccw = (TextView) view.findViewById(R.id.ccw);
        if (option.ccw != null && option.ccw.size() > 0) {
            ccw.setText("CCW: " + TextUtils.join(", ", option.ccw));
        } else {
            ccw.setVisibility(View.GONE);
        }

        if (option.profile != 0) {
            option.spec.add(unit.profiles.get(option.profile).name);
        }
        TextView spec = (TextView) view.findViewById(R.id.spec);
        if (option.spec != null && option.spec.size() > 0) {
            spec.setText("Spec: " + TextUtils.join(", ", option.spec));
        } else {
            spec.setVisibility(View.GONE);
        }

        TextView note = (TextView) view.findViewById(R.id.note);
        if (option.note != null && !option.note.isEmpty()) {
            note.setText("Note: " + option.note);
        } else {
            note.setVisibility(View.GONE);
        }

        return view;
    }
}
