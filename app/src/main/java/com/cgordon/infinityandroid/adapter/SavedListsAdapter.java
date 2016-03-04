/*
 * Copyright 2016 by Chris Gordon
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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.ArmyList;
import com.cgordon.infinityandroid.storage.ListData;

import java.util.List;

public class SavedListsAdapter  extends RecyclerView.Adapter<SavedListsAdapter.ViewHolder> {

    private static final String TAG = SavedListsAdapter.class.getSimpleName();
    private final List<ArmyList> m_lists;
    private final Context m_context;

    public SavedListsAdapter(Context context) {
        m_context = context;
        ListData listData = new ListData(m_context);
        listData.open();
        m_lists = listData.getArmyLists();
        listData.close();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_army_list, parent, false);
        ViewHolder vh = new ViewHolder(v, m_context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArmyList list = m_lists.get(position);
        holder.armyList = list;
        holder.name.setText(list.name);
        holder.points.setText(Integer.toString(list.points));
    }

    @Override
    public int getItemCount() {
        return m_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView icon;
        public TextView name;
        public TextView points;

        private Context m_context;

        public ArmyList armyList;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            m_context = context;

            itemView.setOnClickListener(this);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.text_name);
            points = (TextView) itemView.findViewById(R.id.text_points);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Clicked saved list: " + getLayoutPosition());
            Intent i = new Intent(m_context, ListConstructionActivity.class);
            i.putExtra(MainActivity.LIST_ID, armyList);
            m_context.startActivity(i);

        }
    }
}
