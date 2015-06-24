package com.cgordon.infinityandroid.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.fragment.ArmyListFragment;
import com.cgordon.infinityandroid.storage.ArmyData;

import java.util.Date;
import java.util.List;

/**
 * Created by cgordon on 5/25/2015.
 */
public class ArmyAdapter extends RecyclerView.Adapter <ArmyAdapter.ViewHolder> {

    private final static String TAG = ArmyAdapter.class.getSimpleName();

    ArmyListFragment.ArmyListListener m_listener;

    List<Army> m_armies;
    Context m_context;
    Resources m_resources;

    public ArmyAdapter(Context context) {
        m_context = context;
        m_resources = m_context.getResources();

        if (m_context instanceof ArmyListFragment.ArmyListListener) {
            m_listener = (ArmyListFragment.ArmyListListener) m_context;
        }

        ArmyData armyData = new ArmyData(m_context);
        Date start = new Date();
        armyData.open();
        m_armies = armyData.getArmyList();
        if (m_armies == null) {
            Log.e(TAG, "No sectorials!  Very bad");
        }
        armyData.close();
        Date end = new Date();
        Log.d(TAG, "ArmyAdapter load data time: " + (end.getTime() - start.getTime()) + " ms");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_army, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Army army = m_armies.get(position);
        String name;
        if (army.abbr != null) {
            name = army.abbr;
        } else {
            name = army.name;
        }
        holder.m_textView.setText(name);

        String resourceName = army.faction;
        if (!army.faction.equals(army.name)) {
            resourceName += "_" + army.name;
        }
        resourceName += "_48";

        resourceName = resourceName.toLowerCase().replace(" ", "_");

        int resourceId = m_resources.getIdentifier(resourceName, "drawable", m_context.getPackageName());
            holder.m_imageView.setImageResource(resourceId);

        holder.m_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_listener != null) {
                    m_listener.onArmyClicked(army);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return m_armies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_textView;
        public ImageView m_imageView;
        public CardView m_cardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            m_imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_textView = (TextView) itemView.findViewById(R.id.text_view);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }



}
