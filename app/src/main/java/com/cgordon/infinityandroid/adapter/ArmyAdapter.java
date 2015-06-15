package com.cgordon.infinityandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.BrowseActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.storage.ArmyData;

import java.util.Date;
import java.util.List;

/**
 * Created by cgordon on 5/25/2015.
 */
public class ArmyAdapter extends RecyclerView.Adapter <ArmyAdapter.ViewHolder> {

    private final static String TAG = ArmyAdapter.class.getSimpleName();

    List<Army> m_armies;
    Context m_context;
    Resources m_resources;
//
//    public int[][] m_data = {
//            {R.string.pano_main, R.drawable.pano_main, R.color.pano},
//            {R.string.yujing_main, R.drawable.yujing_main, R.color.yujing},
//            {R.string.ariadna_main, R.drawable.ariadna_main, R.color.ariadna},
//            {R.string.haqqislam_main, R.drawable.haqqislam_main, R.color.haqqislam},
//            {R.string.nomads_main, R.drawable.nomads_main, R.color.nomads},
//            {R.string.ca_main, R.drawable.ca_main, R.color.ca},
//            {R.string.aleph_main, R.drawable.aleph_main, R.color.aleph},
//            {R.string.tohaa_main, R.drawable.tohaa_main, R.color.tohaa},
//            {R.string.merc_main, R.drawable.merc_main, R.color.mercs}
//    };

    public ArmyAdapter(Context context) {
        m_context = context;
        m_resources = m_context.getResources();

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
                Intent i = new Intent(m_context, BrowseActivity.class);
                i.putExtra(MainActivity.ARMY, army.name);
                i.putExtra(MainActivity.FACTION, army.faction);
                i.putExtra(MainActivity.ID, army.dbId);
                i.putExtra(MainActivity.ABBR, army.abbr);
                m_context.startActivity(i);

//                ((Activity) v.getContext()).findViewById(R.id.toolbar).setBackgroundResource(m_data[position][2]);
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
