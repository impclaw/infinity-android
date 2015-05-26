package com.cgordon.infinityandroid.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.adapter.ArmyAdapter;

/**
 * Created by cgordon on 5/24/2015.
 */
public class ArmyListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_army_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float density  = getResources().getDisplayMetrics().density;
//        float dpWidth  = outMetrics.widthPixels / density;
//
//        dpWidth--;
//
//        int columns = (int) Math.floor( dpWidth / 96);

        int columns = 3;  //portrait version
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 5;
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ArmyAdapter();
        recyclerView.setAdapter(adapter);




        return view;
    }
}
