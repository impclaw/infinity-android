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
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Unit;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitFragment extends Fragment {

    private final static String TAG = UnitFragment.class.getSimpleName();

    Unit m_unit = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, getActivity().toString());
        View v = inflater.inflate(R.layout.fragment_unit, container, false);


        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //profileFragment.setArguments(getIntent().getExtras());

        return v;
    }

    public void setUnit(Unit unit) {
        Log.d(TAG, "UnitFragment setId: " + unit.dbId);
        m_unit = unit;



        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.PROFILE, m_unit.profiles.get(0));
        profileFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()

                .add(R.id.fragment_container, profileFragment)
                        //.add(R.id.fragment_container, profileFragment2)
                .commit();


    }

}
