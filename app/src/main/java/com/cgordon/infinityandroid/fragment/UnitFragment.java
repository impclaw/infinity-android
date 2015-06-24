package com.cgordon.infinityandroid.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Unit;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitFragment extends Fragment {

    private final static String TAG = UnitFragment.class.getSimpleName();

    private TextView m_textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, getActivity().toString());
        View v = inflater.inflate(R.layout.fragment_unit, container, false);
        m_textView = (TextView) v.findViewById(R.id.text_view);
        m_textView.setTypeface(Typeface.MONOSPACE);
        return v;
    }

    public void setUnit(Unit unit) {
        Log.d(TAG, "UnitFragment setId: " + unit.dbId);
        m_textView.setText(unit.toString());

    }

}
