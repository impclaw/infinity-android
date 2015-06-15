package com.cgordon.infinityandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;

/**
 * Created by cgordon on 6/10/2015.
 */
public class UnitFragment extends Fragment {

    private final static String TAG = UnitFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, getActivity().toString());
        return inflater.inflate(R.layout.fragment_unit, container, false);
    }

    public void setId(long id) {
        Log.d(TAG, "UnitFragment setId: " + id);
    }

}
