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
import com.cgordon.infinityandroid.data.Option;
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

        Bundle arguments = getArguments();
        Unit unit = arguments.getParcelable(MainActivity.UNIT);
        int index = arguments.getInt(MainActivity.INDEX);

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
            ccw.setText(TextUtils.join(", ", option.ccw));
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
