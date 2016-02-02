/*
 * Copyright 2016 by Chris Gordon
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;

public class ArmyStatusFragment extends Fragment implements ListConstructionActivity.ArmyStatusListener{

    private TextView m_lieutenant;
    private TextView m_cost;
    private TextView m_swc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_army_status, container, false);

        m_lieutenant = (TextView) v.findViewById(R.id.text_lieutenant);
        m_lieutenant.setText("Missing");
        m_cost = (TextView) v.findViewById(R.id.text_cost);
        m_swc = (TextView) v.findViewById(R.id.text_swc);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ListConstructionActivity) {
            ((ListConstructionActivity)activity).setArmyStatusListener(this);
        }

    }

    @Override
    public void OnArmyStatusChanged(int cost, double swc, int lieutenantCount) {
        m_cost.setText(Integer.toString(cost));
        m_swc.setText(Double.toString(swc));

        String ltStatus = "Ok";
        if (lieutenantCount <1) {
            ltStatus = "Missing";
        } else if (lieutenantCount >1) {
            ltStatus = "Too many";
        }
        m_lieutenant.setText(ltStatus);

    }

}
