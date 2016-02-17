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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.ListConstructionAdapter;

public class ListStatusFragment extends Fragment implements ListConstructionAdapter.ListChangedListener{

    private TextView m_regular;
    private TextView m_irregular;
    private TextView m_impetuous;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_status, container, false);

        m_regular = (TextView) v.findViewById(R.id.text_orders_regular);
        m_irregular = (TextView) v.findViewById(R.id.text_orders_irregular);
        m_impetuous = (TextView) v.findViewById(R.id.text_orders_impetuous);

//        Fragment parentFragment = getParentFragment();
//        if (parentFragment instanceof ListConstructionFragment) {
//            parentFragment
//            ((OnListChanged)parentFragment).setListChangedListener(this);
//        }

        return v;
    }

    @Override
    public void onListChanged(int cost, double swc, int lieutenantCount) {
//        m_regular.setText(Integer.toString(regularCount));
//        m_irregular.setText(Integer.toString(irregularCount));
//        m_impetuous.setText(Integer.toString(impetuousCount));

    }
}
