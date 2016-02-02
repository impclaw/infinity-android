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

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Unit;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ListConstructionAdapter extends RecyclerView.Adapter<ListConstructionAdapter.ViewHolder> {

    private static final String TAG = ListConstructionAdapter.class.getSimpleName();

    private List<Entry<Unit, Integer>> m_list;

    private Context m_context;

//    int m_totalCost = 0;
//    double m_totalSWC = 0;
    private OnListChanged m_listener;

    public interface OnListChanged {
        public void listStatus(int cost, double swc, int lieutenantCount, int regularCount, int irregularCount, int impetuousCount);
    }

    public void setListChangedListener(OnListChanged listener) {
        m_listener = listener;
    }


    public ListConstructionAdapter(Context context) {
        m_context = context;
        m_list = new ArrayList<>();
    }

    private void updateListener() {
        Iterator it = m_list.iterator();
        int costTotal = 0;
        double swcTotal = 0;
        int ltCount = 0;
        int regularCount = 0;
        int irregularCount = 0;
        int impetuousCount = 0;


        while (it.hasNext()) {
            Entry e = (Entry)it.next();
            Unit unit = (Unit) e.getKey();
            int option = (int) e.getValue();
            costTotal += unit.options.get(option).cost;
            swcTotal += unit.options.get(option).swc;

            if (unit.options.get(option).spec.contains("Lieutenant")) {
                ltCount++;
            }

            if (unit.profiles.get(0).irr) {
                irregularCount++;
            } else {
                regularCount++;
            }
            if (!unit.profiles.get(0).imp.isEmpty()) {
                impetuousCount++;
            }
        }


        m_listener.listStatus(costTotal, swcTotal, ltCount, regularCount, irregularCount, impetuousCount );
    }

    public void delete(int position) {
        m_list.remove(position);
        updateListener();
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Unit unit = m_list.get(position).getKey();
        Option option = unit.options.get(m_list.get(position).getValue());

        int resourceId = UnitListAdapter.getDrawableResource(unit, m_context, 48);
        holder.m_image.setImageResource(resourceId);

        holder.m_isc.setText(unit.isc);
        holder.m_option.setText(option.code);
        holder.m_points.setText(Integer.toString(option.cost));
        holder.m_swc.setText(Double.toString(option.swc));
    }

    public void addUnit(Unit unit, int option) {
        m_list.add(new AbstractMap.SimpleEntry<>(unit, option));

        updateListener();
        notifyItemRangeInserted(getItemCount() - 1, 1);
    }

    public int getItemCount() {
//        return m_armies.size();
        return m_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_unit_list, parent,
                false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView m_image;
        public TextView m_isc;
        public TextView m_option;
        public TextView m_points;
        public TextView m_swc;
        public ImageButton m_delete;

        public CardView m_cardView;



        public ViewHolder(final View itemView) {
            super(itemView);
            m_image = (ImageView) itemView.findViewById(R.id.image_view);
            m_isc = (TextView) itemView.findViewById(R.id.text_isc);
            m_option = (TextView) itemView.findViewById(R.id.text_option);
            m_points = (TextView) itemView.findViewById(R.id.text_points);
            m_swc = (TextView) itemView.findViewById(R.id.text_swc);
            m_delete = (ImageButton) itemView.findViewById(R.id.delete);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);

            m_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                }
            });

        }
    }
}
