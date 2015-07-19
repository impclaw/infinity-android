package com.cgordon.infinityandroid.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Unit;

import java.util.List;

/**
 * Created by cgordon on 7/12/2015.
 */
public class OptionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        TableLayout table = (TableLayout) view.findViewById(R.id.table);

        Bundle arguments = getArguments();
        Unit unit = arguments.getParcelable(MainActivity.UNIT);

        TableRow tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//
//        TextView columnName = new TextView(getActivity());
//        columnName.setTypeface(null, Typeface.BOLD);
//        columnName.setText("Name");
//        tr.addView(columnName);
//
//        columnName = new TextView(getActivity());
//        columnName.setText("BS Weapons");
//        tr.addView(columnName);
//
//        columnName = new TextView(getActivity());
//        columnName.setText("CC Weapons");
//        tr.addView(columnName);
//
//        columnName = new TextView(getActivity());
//        columnName.setTypeface(null, Typeface.BOLD);
//        columnName.setText("SWC");
//        tr.addView(columnName);
//
//        columnName = new TextView(getActivity());
//        columnName.setText("C");
//        tr.addView(columnName);
//
//        table.addView(tr);


        for (int i = 0; i < unit.options.size(); i++) {

            tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView name = new TextView(getActivity());
            name.setTextAppearance(getActivity(), R.style.AppTheme_Stats);
            name.setText(unit.options.get(i).code);
            tr.addView(name);

            TextView bsWeapons= new TextView(getActivity());
            bsWeapons.setTextAppearance(getActivity(), R.style.AppTheme_Stats);
            bsWeapons.setText(TextUtils.join(", ", unit.options.get(i).bsw));
            tr.addView(bsWeapons);

            TextView ccWeapons = new TextView(getActivity());
            ccWeapons.setTextAppearance(getActivity(), R.style.AppTheme_Stats);
            ccWeapons.setText(TextUtils.join(", ", unit.options.get(i).ccw));
            tr.addView(ccWeapons);

            TextView swc = new TextView(getActivity());
            swc.setTextAppearance(getActivity(), R.style.AppTheme_Stats);
            swc.setText(Double.toString(unit.options.get(i).swc));
            tr.addView(swc);

            TextView cost = new TextView(getActivity());
            cost.setTextAppearance(getActivity(), R.style.AppTheme_Stats);
            cost.setText(Integer.toString(unit.options.get(i).cost));
            tr.addView(cost);

            table.addView(tr);

        }

        return view;
    }
}
