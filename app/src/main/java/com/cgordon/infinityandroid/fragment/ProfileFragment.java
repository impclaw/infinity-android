package com.cgordon.infinityandroid.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;

import org.w3c.dom.Text;

/**
 * Created by cgordon on 7/4/2015.
 */
public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle arguments = getArguments();
        Profile profile = arguments.getParcelable(MainActivity.PROFILE);

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
        stat.setText(profile.ava);

        TextView isc = (TextView) view.findViewById(R.id.isc);
        isc.setText(profile.isc);

        TextView type = (TextView) view.findViewById(R.id.type);
        type.setText(profile.type);

        TextView note = (TextView) view.findViewById(R.id.note);
        if ((profile.note != null) && (!profile.note.isEmpty())) {
            note.setText("Note: " + profile.note);
        } else {
            note.setVisibility(View.GONE);
        }

        TextView spec = (TextView) view.findViewById(R.id.spec);
        spec.setText(TextUtils.join(", ", profile.spec));

        ImageView irr = (ImageView) view.findViewById(R.id.irr);
        if (profile.irr) {
            irr.setImageResource(R.drawable.irregular);
        } else {
            irr.setImageResource(R.drawable.regular);
        }

        ImageView imp = (ImageView) view.findViewById(R.id.imp);
        if (profile.imp.equals("F")) {
            imp.setImageResource(R.drawable.frenzy);
        } else if (profile.imp.equals("X")) {
            imp.setImageResource(R.drawable.extreme_impetuous);
        } else if (profile.imp.equals("I")) {
            imp.setImageResource(R.drawable.impetuous);
        }

        ImageView cube = (ImageView) view.findViewById(R.id.cube);
        if (profile.cube.equals("X")) {
            cube.setImageResource(R.drawable.cube);
        }
        else if (profile.cube.equals("2")) {
            cube.setImageResource(R.drawable.cube2);
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
//        int resourceId = UnitListAdapter.getDrawableResource(m_unit, getActivity(), 24);
//        imageView.setImageResource(resourceId);

        ViewCompat.setTransitionName(imageView, UnitListActivity.TRANSITION_IMAGE);



        return view;
    }

}
