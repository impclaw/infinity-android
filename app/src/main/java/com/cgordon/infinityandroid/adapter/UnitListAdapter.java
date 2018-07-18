/*
 * Copyright 2015-2016 by Chris Gordon
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.ListValidation;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.fragment.UnitListFragment;

import java.util.List;

public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.ViewHolder> {

    private static final String TAG = UnitListAdapter.class.getSimpleName();


    private final Resources m_resources;
    private final Context m_context;

    private boolean m_showAsList = false;

    private UnitListFragment.UnitSelectedListener m_listener;

    List<Unit> m_units = null;

    public UnitListAdapter(Context context) {
        m_context = context;
        m_resources = context.getResources();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(m_context);
        if (prefs.contains(UnitListFragment.ListAsListKey)) {
            m_showAsList = prefs.getBoolean(UnitListFragment.ListAsListKey, false);
        }

    }

    public void setListener(UnitListFragment.UnitSelectedListener listener) {
        m_listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v;
        if (m_showAsList) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_unit_list, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_unit, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Unit unit = m_units.get(position);

        holder.dbID = unit.id;
        if (m_showAsList) {
            holder.m_textView.setText(unit.isc);
        } else {
            holder.m_textView.setText(unit.name);
        }

        if (holder.m_type != null) {
            holder.m_type.setText(unit.profiles.get(0).type);
        }

        final int imageSize;
        if (m_showAsList) {
            imageSize = 24;
        } else {
            imageSize = 48;
        }

        int resourceId = getDrawableResource(unit, m_context, imageSize);
        holder.m_imageView.setImageResource(resourceId);

        if (m_showAsList) {
            holder.m_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (m_listener != null) {
                        m_listener.unitSelected(unit, -1);
                    }
                }
            });
        } else {
            holder.m_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (m_listener != null) {
                        m_listener.unitSelected(unit, -1);

                    }

                }
            });
        }


    }

    public static int getDrawableResource(Unit unit, Context context, int size) {
        String imageSize = "_" + Integer.toString(size);

        String resourceName;
        if (unit.image == -1) {
            resourceName = "image_" + unit.id + imageSize;
        } else {
            resourceName = "image_" + unit.image + imageSize;
        }

//        resourceName = UnitListAdapter.prepareDrawableResource(resourceName);

        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

        if (resourceId == 0) {
            String newResource = "image_" + unit.id / 1000 + "999" + imageSize;
            Log.d(TAG, "Missing resource: " + unit.id + " " + resourceName + ", using " + newResource);
            //String factionResource = UnitListAdapter.prepareDrawableResource(newResource + "_" + imageSize);
            resourceId = context.getResources().getIdentifier(newResource, "drawable", context.getPackageName());
        }

        return resourceId;
    }

    public static String prepareDrawableResource(String resourceName) {
        return resourceName.toLowerCase()
                .replace(" ", "_").replace("-", "_").replace(",", "").replace(":", "")
                .replace(".", "").replace("'", "").replace("(", "").replace(")", "")
                .replace('á', 'a').replace('ă', 'a').replace('à', 'a').replace('ā', 'a')
                .replace('é', 'e').replace('ē', 'e').replace('è', 'e')
                .replace('ǐ', 'i').replace('ì', 'i').replace('ī', 'i')
                .replace('ñ', 'n')
                .replace('ō', 'o').replace('ò', 'o')
                .replace('ú', 'u').replace('ù', 'u').replace('ŭ', 'u').replace('û', 'u').replace('ü', 'u');
    }

    @Override
    public int getItemCount() {
        if (m_units == null) {
            return 0;
        } else {
            return m_units.size();
        }
    }

    public void setUnits(List<Unit> units) {
        m_units = units;

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_textView;
        public ImageView m_imageView;
        public TextView m_type;

        public CardView m_cardView;

        public long dbID;

        public ViewHolder(View itemView) {
            super(itemView);
            m_imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_textView = (TextView) itemView.findViewById(R.id.text_name);
            m_type = (TextView) itemView.findViewById(R.id.type);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);
        }


    }
}
