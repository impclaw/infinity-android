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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.ListConstructionActivity;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.ArmyList;
import com.cgordon.infinityandroid.storage.ArmyData;
import com.cgordon.infinityandroid.storage.ListData;

import java.util.List;

public class SavedListsAdapter  extends RecyclerView.Adapter<SavedListsAdapter.ViewHolder> {

    private static final String TAG = SavedListsAdapter.class.getSimpleName();
    private final List<Army> m_armies;
    private List<ArmyList> m_lists;
    private final Context m_context;
    private final Resources m_resources;
    private SavedListsAdapterListener m_listener;

    public SavedListsAdapter(Context context) {
        m_context = context;
        m_resources = m_context.getResources();
        refresh();

        ArmyData armyData = new ArmyData(m_context);
        armyData.open();
        m_armies = armyData.getArmyList();
        armyData.close();
    }

    public void refresh() {
        ListData listData = new ListData(m_context);
        listData.open();
        m_lists = listData.getArmyLists();
        listData.close();

        // TODO - should use something more efficient if we can
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_army_list, parent, false);
        ViewHolder vh = new ViewHolder(v, m_context);
        return vh;
    }

    public void setListener(SavedListsAdapterListener listener) {
        m_listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArmyList list = m_lists.get(position);
        holder.armyList = list;
        holder.name.setText(list.name);
        holder.points.setText(Integer.toString(list.points));
        holder.dbId = list.dbId;

        Army army = getArmy(list.armyId);
        if (army != null) {
            String resourceName = "image_" + Integer.toString(army.jsonId);
            resourceName += "_48";

            int resourceId = m_resources.getIdentifier(resourceName, "drawable", m_context.getPackageName());
            holder.icon.setImageResource(resourceId);
        }
    }

    private Army getArmy(long armyId) {
        Army retval = null;
        for (Army army: m_armies) {
            if (army.dbId == armyId) {
                retval = army;
                break;
            }
        }
        return retval;
    }

    @Override
    public int getItemCount() {
        return m_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final CardView m_cardView;
        public ImageView icon;
        public TextView name;
        public TextView points;
        public ImageButton delete;
        public long dbId;

        private Context m_context;

        public ArmyList armyList;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            m_context = context;

//            itemView.setOnClickListener(this);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.text_name);
            points = (TextView) itemView.findViewById(R.id.text_points);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);

            m_cardView = (CardView) itemView.findViewById(R.id.card_view);
            m_cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v instanceof CardView) {
                Log.d(TAG, "Clicked saved list: " + getLayoutPosition());
                Intent i = new Intent(m_context, ListConstructionActivity.class);
                i.putExtra(MainActivity.LIST_ID, armyList);
                m_context.startActivity(i);
            } else if (v instanceof ImageButton) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.action_delete);
                builder.setMessage(R.string.delete_list_question);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListData listData = new ListData(m_context);
                        listData.open();
                        if (!listData.deleteList(dbId)) {
                            Toast.makeText(m_context, R.string.error_deleting_list + name.getText().toString(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Error deleting list: " + name.getText().toString());
                        }
                        listData.close();
                        refresh();
                        if (m_listener != null) {
                            m_listener.onDelete();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }

        }
    }

    public interface SavedListsAdapterListener {
        public void onDelete();
    }
}
