/*
 * Copyright 2015 by Chris Gordon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cgordon.infinityandroid.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;

public class ArmyListAdapter extends RecyclerView.Adapter<ArmyListAdapter.ViewHolder> {

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        final Army army = m_armies.get(position);
//        String name;
//        if (army.abbr != null) {
//            name = army.abbr;
//        } else {
//            name = army.name;
//        }
//        holder.m_textView.setText(name);
//
//        String resourceName = army.faction;
//        if (!army.faction.equals(army.name)) {
//            resourceName += "_" + army.name;
//        }
//        resourceName += "_48";
//
//        resourceName = resourceName.toLowerCase().replace(" ", "_");
//
//        int resourceId = m_resources.getIdentifier(resourceName, "drawable", m_context.getPackageName());
//        holder.m_imageView.setImageResource(resourceId);
//
//        holder.m_cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (m_listener != null) {
//                    m_listener.onArmyClicked(army);
//                }
//            }
//        });
    }

    public int getItemCount() {
//        return m_armies.size();
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_army, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_textView;
        public ImageView m_imageView;
        public CardView m_cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            m_imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_textView = (TextView) itemView.findViewById(R.id.text_name);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
