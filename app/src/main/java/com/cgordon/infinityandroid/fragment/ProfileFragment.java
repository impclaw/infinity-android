package com.cgordon.infinityandroid.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.activity.UnitListActivity;
import com.cgordon.infinityandroid.adapter.ArmyAdapter;
import com.cgordon.infinityandroid.adapter.UnitListAdapter;
import com.cgordon.infinityandroid.data.Unit;

/**
 * Created by cgordon on 7/4/2015.
 */
public class ProfileFragment extends Fragment {

    Unit m_unit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        Bundle arguments = getArguments();

        m_unit = arguments.getParcelable(MainActivity.UNIT);

        TextView m_textView = (TextView) view.findViewById(R.id.text_view);
        m_textView.setText(m_unit.toString());
        m_textView.setTypeface(Typeface.MONOSPACE);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

        int resourceId = UnitListAdapter.getDrawableResource(m_unit, getActivity(), 48);

        imageView.setImageResource(resourceId);

        ViewCompat.setTransitionName(imageView, UnitListActivity.TRANSITION_IMAGE);



        return view;
    }

}
