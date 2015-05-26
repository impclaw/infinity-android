package com.cgordon.infinityandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;

/**
 * Created by cgordon on 5/25/2015.
 */
public class ArmyAdapter extends RecyclerView.Adapter <ArmyAdapter.ViewHolder> {

    public int[][] m_data = {
            {R.string.pano_main, R.drawable.pano_main, R.color.pano},
            {R.string.pano_acontecimento, R.drawable.pano_acontecimento, R.color.pano},
            {R.string.pano_military_orders, R.drawable.pano_military_orders, R.color.pano},
            {R.string.pano_neoterra, R.drawable.pano_neoterra, R.color.pano},
            {R.string.yujing_main, R.drawable.yujing_main, R.color.yujing},
            {R.string.yujing_iss, R.drawable.yujing_iss, R.color.yujing},
            {R.string.yujing_jsa, R.drawable.yujing_jsa, R.color.yujing},
            {R.string.ariadna_main, R.drawable.ariadna_main, R.color.ariadna},
            {R.string.ariadna_caledonian, R.drawable.ariadna_caledonian, R.color.ariadna},
            {R.string.ariadna_merovingian, R.drawable.ariadna_merovingian, R.color.ariadna},
            {R.string.ariadna_usariadna, R.drawable.ariadna_usariadna, R.color.ariadna},
            {R.string.haqqislam_main, R.drawable.haqqislam_main, R.color.haqqislam},
            {R.string.haqqislam_hassassin, R.drawable.haqqislam_hassassin, R.color.haqqislam},
            {R.string.haqqislam_qapu_khalqi, R.drawable.haqqislam_qapu_khalqi, R.color.haqqislam},
            {R.string.nomads_main, R.drawable.nomads_main, R.color.nomads},
            {R.string.nomads_bakunin, R.drawable.nomads_bakunin, R.color.nomads},
            {R.string.nomads_corregidor, R.drawable.nomads_corregidor, R.color.nomads},
            {R.string.ca_main, R.drawable.ca_main, R.color.ca},
            {R.string.ca_morat, R.drawable.ca_morat, R.color.ca},
            {R.string.ca_shasvastii, R.drawable.ca_shasvastii, R.color.ca},
            {R.string.aleph_main, R.drawable.aleph_main, R.color.aleph},
            {R.string.aleph_ass, R.drawable.aleph_ass, R.color.aleph},
            {R.string.tohaa_main, R.drawable.tohaa_main, R.color.tohaa},
            {R.string.merc_main, R.drawable.merc_main, R.color.mercs}
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_army, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.m_textView.setText(m_data[position][0]);
        holder.m_imageView.setImageResource(m_data[position][1]);
        holder.m_imageView.setBackgroundResource(m_data[position][2]);
    }


    @Override
    public int getItemCount() {
        Log.d(ArmyAdapter.class.getSimpleName(), ""+ m_data.length);
        return m_data.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_textView;
        public ImageView m_imageView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            m_imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }



}
