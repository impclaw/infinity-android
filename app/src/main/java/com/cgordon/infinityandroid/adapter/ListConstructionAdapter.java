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
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.CombatGroupElement;
import com.cgordon.infinityandroid.data.ListElement;
import com.cgordon.infinityandroid.data.Child;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.UnitElement;
import com.cgordon.infinityandroid.fragment.UnitListFragment;
import com.cgordon.infinityandroid.interfaces.ItemTouchHelperListener;
import com.cgordon.infinityandroid.interfaces.UnitSource;
import com.cgordon.infinityandroid.storage.ListData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListConstructionAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements ItemTouchHelperListener

{
    private static final String TAG = ListConstructionAdapter.class.getSimpleName();
    private final UnitSource m_unitSource;
    private UnitListFragment.UnitSelectedListener m_unitSelectedListener = null;

    private final int TYPE_UNIT = 0;
    private final int TYPE_GROUP = 1;

    private Context m_context;

    private List<ListChangedListener> m_listeners;

    // list of unit, option selection for that unit.
    private List<ListElement> m_list;

    public List<ListElement> getList() {
        return new ArrayList<>(m_list);
    }

    public ListConstructionAdapter(Context context, UnitSource source) {
        m_context = context;
        m_unitSource = source;
        m_list = new ArrayList<>();

        m_list.add(new CombatGroupElement(1));
        m_list.add(new CombatGroupElement(2));
        m_list.add(new CombatGroupElement(3));
        m_list.add(new CombatGroupElement(4));


        m_listeners = new ArrayList<>();

        if (context instanceof UnitListFragment.UnitSelectedListener) {
            m_unitSelectedListener = (UnitListFragment.UnitSelectedListener) context;
        }
    }

    public void loadSavedList(long id) {
        if (id != -1) {
            Log.d(TAG, "Loading...");
            ListData listData = new ListData(m_context);
            listData.open();
            m_list = listData.getList(id);
            listData.close();

            // probably not strictly necessary here...
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean onItemMove(int fromIndex, int toIndex) {
        // you can't replace the first item in the list because that's the Group 1 header.
        if (toIndex == 0) {
            return false;
        }
        Log.d(TAG, "Move from: " + fromIndex + " to: " + toIndex);
        if (fromIndex < toIndex) {
            for (int i = fromIndex; i < toIndex; i++) {
                Collections.swap(m_list, i, i + 1);
            }
        } else {
            for (int i = fromIndex; i > toIndex; i--) {
                Collections.swap(m_list, i, i - 1);
            }
        }
        notifyItemMoved(fromIndex, toIndex);
        updateListener();

        for (ListChangedListener l: m_listeners
                ) {
            l.onOrderChanged(fromIndex, toIndex);

        }

        return true;
    }

    @Override
    public boolean onItemSwipe(int index) {
        return true;
    }

    public interface ListChangedListener {
        void onListChanged(int cost, double swc, int lieutenantCount);
        void onOrderChanged(int oldPosition, int newPosition);
    }

    public boolean addListChangedListener(ListChangedListener listener) {
        if (listener != null) {
            boolean retval = true;

            if (!m_listeners.contains(listener)) {
                retval = m_listeners.add(listener);
            }

            return retval;

        }
        return false;
    }

    public boolean removeListChangedListener(ListChangedListener listener) {
        return m_listeners.remove(listener);
    }

    public void updateListener() {
        CombatGroupElement currentCombatGroupElement = null;
        int currentCombatGroupIndex = 0;

        int costTotal = 0;
        double swcTotal = 0;
        int ltCount = 0;
        int regularCount = 0;
        int irregularCount = 0;
        int impetuousCount = 0;


        for (int i = 0; i < m_list.size(); i++) {
            ListElement le = m_list.get(i);
            if (le instanceof UnitElement) {
                UnitElement unitElement = (UnitElement) le;

                Unit unit = m_unitSource.getUnit(unitElement.unitId);
                Child child = unit.getChild(unitElement.child);

                costTotal += child.cost;
                swcTotal += child.swc;

                if (child.spec.contains("Lieutenant")) {
                    ltCount++;
                }

                if (unit.profiles.get(0).irr) {
                    irregularCount++;
                } else {
                    if (!unit.profiles.get(0).spec.contains("G: Servant")) {
                        regularCount++;
                    }
                }

                // models with Frenzy don't start the game with an impetuous order like regular
                // impetuous models do
                if ((unit.profiles.get(0).imp.compareToIgnoreCase("F") != 0)
                        && (!unit.profiles.get(0).imp.isEmpty())) {
                    impetuousCount++;
                }
            }
            else if (le instanceof CombatGroupElement) {
                if (currentCombatGroupElement != null) {
                    currentCombatGroupElement.m_regularOrders = regularCount;
                    currentCombatGroupElement.m_irregularOrders = irregularCount;
                    currentCombatGroupElement.m_impetuousOrders = impetuousCount;
                    notifyItemChanged(currentCombatGroupIndex);
                }
                regularCount = 0;
                irregularCount = 0;
                impetuousCount = 0;
                currentCombatGroupElement = (CombatGroupElement) le;
                currentCombatGroupIndex = i;
            }
        }
        // update the last combat group since we're out of the loop.
        if (currentCombatGroupElement != null) {
            currentCombatGroupElement.m_regularOrders = regularCount;
            currentCombatGroupElement.m_irregularOrders = irregularCount;
            currentCombatGroupElement.m_impetuousOrders = impetuousCount;
            notifyItemChanged(currentCombatGroupIndex);
        }

        for (ListChangedListener l: m_listeners
             ) {
            l.onListChanged(costTotal, swcTotal, ltCount);
            
        }

        //m_listener.onListChanged(costTotal, swcTotal, ltCount, regularCount, irregularCount, impetuousCount);
    }

    public void delete(int position) {
        if (position > -1) {
            m_list.remove(position);
            updateListener();
            notifyItemRemoved(position);
        } else {
            Toast toast = Toast.makeText(m_context, "Invalid delete index", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private int determineGroup(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof UnitViewHolder) {
            UnitViewHolder unitViewHolder = (UnitViewHolder)holder;

            UnitElement unitElement = (UnitElement) m_list.get(position);

            Unit unit = m_unitSource.getUnit(unitElement.unitId);
            Child child = unit.getChild(unitElement.child);

            int resourceId = UnitListAdapter.getDrawableResource(unit, m_context, 48);
            unitViewHolder.m_image.setImageResource(resourceId);

            unitViewHolder.m_isc.setText(unit.isc);
            unitViewHolder.m_child.setText(child.name);
            unitViewHolder.m_points.setText(Integer.toString(child.cost));
            unitViewHolder.m_swc.setText(Double.toString(child.swc));
        } else {
            GroupViewHolder groupViewHolder = (GroupViewHolder)holder;
            CombatGroupElement combatGroupElement = (CombatGroupElement) m_list.get(position);
            groupViewHolder.m_group.setText("Group " + combatGroupElement.m_id);
            groupViewHolder.m_regular.setText(Integer.toString(combatGroupElement.m_regularOrders));
            groupViewHolder.m_irregular.setText(Integer.toString(combatGroupElement.m_irregularOrders));
            groupViewHolder.m_impetuous.setText(Integer.toString(combatGroupElement.m_impetuousOrders));
        }
    }

    public void addUnit(Unit unit, int child) {
        // find the index of Combat Group 2 and insert it before that.
        int insertIndex = 1;
        for (int i = 0; i < m_list.size(); i++) {
            ListElement listElement = m_list.get(i);
            if (listElement instanceof CombatGroupElement) {
                CombatGroupElement combatGroupElement = (CombatGroupElement) listElement;
                if (combatGroupElement.m_id == 2)
                {
                    insertIndex = i;
                    break;
                }
            }
        }

        m_list.add(insertIndex, new UnitElement(-1, 1, (int)unit.id, child));
        updateListener();
        notifyItemRangeInserted(insertIndex, 1);
    }

    public int getItemCount() {
        return m_list.size();  // group headers
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder vh = null;
        if (viewType == TYPE_UNIT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_unit_list, parent, false);
            vh = new UnitViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group_header, parent, false);
            vh = new GroupViewHolder(v);

        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        ListElement le = m_list.get(position);
        if (le instanceof UnitElement) {
            return TYPE_UNIT;
        } else {
            return TYPE_GROUP;
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        public TextView m_group;
        public TextView m_regular;
        public TextView m_irregular;
        public TextView m_impetuous;
        public int m_type;

        public GroupViewHolder(View itemView) {
            super(itemView);
            m_group = (TextView) itemView.findViewById(R.id.text_group);
            m_regular = (TextView) itemView.findViewById(R.id.text_orders_regular);
            m_irregular = (TextView) itemView.findViewById(R.id.text_orders_irregular);
            m_impetuous = (TextView) itemView.findViewById(R.id.text_orders_impetuous);

        }
    }
    public class UnitViewHolder extends RecyclerView.ViewHolder {

        public ImageView m_image;
        public TextView m_isc;
        public TextView m_child;
        public TextView m_points;
        public TextView m_swc;
        public ImageButton m_delete;

        public CardView m_cardView;



        public UnitViewHolder(final View itemView) {
            super(itemView);
            m_image = (ImageView) itemView.findViewById(R.id.image_view);
            m_isc = (TextView) itemView.findViewById(R.id.text_isc);
            m_child = (TextView) itemView.findViewById(R.id.text_option);
            m_points = (TextView) itemView.findViewById(R.id.text_points);
            m_swc = (TextView) itemView.findViewById(R.id.text_swc);
            m_delete = (ImageButton) itemView.findViewById(R.id.delete);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);

            m_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListElement temp = m_list.get(getAdapterPosition());
                    if (temp instanceof UnitElement) {
                        m_unitSelectedListener.unitSelected(m_unitSource.getUnit(((UnitElement)temp).unitId),
                                UnitViewHolder.this);
                    }
                }
            });

            m_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                }
            });

        }
    }

    public static class ListConstructionTouchHelper extends ItemTouchHelper.Callback {

        private final ItemTouchHelperListener m_listener;

        public ListConstructionTouchHelper(ItemTouchHelperListener listener) {
            m_listener = listener;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof UnitViewHolder) {
                int drag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipe = 0; //ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(drag, swipe);
            }
            return makeMovementFlags(0, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder destination) {
            m_listener.onItemMove(viewHolder.getAdapterPosition(), destination.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            m_listener.onItemSwipe(i);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }
    }

 }
