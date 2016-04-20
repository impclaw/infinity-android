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
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.activity.UnitListActivity;
import com.cgordon.infinityandroid.data.Child;
import com.cgordon.infinityandroid.data.ListElement;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.UnitElement;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.Map;

public class UnitAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_PROFILE = 0;
    private final int TYPE_CHILD = 1;

    private final Map<String, Weapon> m_weaponsList;
    private final Context m_context;
    private Unit m_unit;

    public UnitAdapter(Context context) {
        m_context = context;
        WeaponsData wd = new WeaponsData(context);
        wd.open();
        m_weaponsList = wd.getWeapons();
        wd.close();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder vh = null;
        if (viewType == TYPE_PROFILE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_profile, parent, false);
            vh = new ProfileViewHolder(v);
        } else if (viewType == TYPE_CHILD) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_child, parent, false);
            vh = new ChildViewHolder(v);
//        } else {
//            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group_header, parent, false);
//            vh = new GroupViewHolder(v);

        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProfileViewHolder) {
            ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;

            // we can just use position because this is the first section in the page.  We'll need
            // to do some math to figure out the position after this.
            Profile profile = m_unit.profiles.get(position);
            profileViewHolder.setProfile(m_unit, profile, m_context);
        } else if (holder instanceof  ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;

            // The position includes all the profile entries listed above the child entries.
            // Subtract the number of profiles from the position to get the actual child index
            Child child = m_unit.children.get(position - m_unit.profiles.size());
            childViewHolder.setChild(m_unit, child);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position < m_unit.profiles.size())
            return TYPE_PROFILE;
        else
            return TYPE_CHILD;
    }

    @Override
    public int getItemCount() {
        return
                m_unit.profiles.size() + m_unit.children.size();
    }

    public void setUnit(Unit unit) {
        m_unit = unit;
        notifyDataSetChanged();
    }


    static public class ProfileViewHolder extends RecyclerView.ViewHolder {

        private final TextView mov;
        private final TextView cc;
        private final TextView bs;
        private final TextView ph;
        private final TextView wip;
        private final TextView arm;
        private final TextView bts;
        private final TextView wounds;
        private final TextView silhouette;
        private final TextView ava;
        private final TextView isc;
        private final TextView type;
        private final TextView note;
        private final TextView spec;
        private final TextView bsw;
        private final TextView ccw;
        private final ImageView irr;
        private final ImageView imp;
        private final ImageView cube;
        private final ImageView hackable;
        private final ImageView imageView;

        public CardView m_cardView;

        public ProfileViewHolder(final View itemView) {
            super(itemView);

            mov = (TextView) itemView.findViewById(R.id.mov);
            cc = (TextView) itemView.findViewById(R.id.cc);
            bs = (TextView) itemView.findViewById(R.id.bs);
            ph = (TextView) itemView.findViewById(R.id.ph);
            wip = (TextView) itemView.findViewById(R.id.wip);
            arm = (TextView) itemView.findViewById(R.id.arm);
            bts = (TextView) itemView.findViewById(R.id.bts);
            wounds = (TextView) itemView.findViewById(R.id.wounds);
            silhouette = (TextView) itemView.findViewById(R.id.silhouette);
            ava = (TextView) itemView.findViewById(R.id.ava);
            isc = (TextView) itemView.findViewById(R.id.isc);
            type = (TextView) itemView.findViewById(R.id.type);
            note = (TextView) itemView.findViewById(R.id.note);
            spec = (TextView) itemView.findViewById(R.id.spec);
            bsw = (TextView) itemView.findViewById(R.id.bsw);
            ccw = (TextView) itemView.findViewById(R.id.ccw);
            irr = (ImageView) itemView.findViewById(R.id.irr);
            imp = (ImageView) itemView.findViewById(R.id.imp);
            cube = (ImageView) itemView.findViewById(R.id.cube);
            hackable = (ImageView) itemView.findViewById(R.id.hackable);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            m_cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

        public void setProfile(Unit unit, Profile profile, Context context) {
            mov.setText(profile.mov);
            cc.setText(profile.cc);
            bs.setText(profile.bs);
            ph.setText(profile.ph);
            wip.setText(profile.wip);
            arm.setText(profile.arm);
            bts.setText(profile.bts);
            wounds.setText(profile.wounds);
            silhouette.setText(profile.silhouette);

            if ((profile.ava != null) && (!profile.ava.isEmpty())) {
                ava.setText(profile.ava);
            } else {
                ava.setText(unit.ava);
            }

            String iscText = profile.name;
            if ((iscText == null) || (iscText.isEmpty())) {
                iscText = unit.isc;
            }
            isc.setText(iscText);

            type.setText(profile.type);

            if ((profile.note != null) && (!profile.note.isEmpty())) {
                note.setText("Note: " + profile.note);
            } else {
                note.setVisibility(View.GONE);
            }

            // TODO: Get the expanded list of special options.
            if ((profile.spec == null) || (profile.spec.isEmpty())) {
                spec.setVisibility(View.GONE);
            } else {
                spec.setText("Spec: " + TextUtils.join(", ", profile.spec));
            }

            if ((profile.bsw == null) || (profile.bsw.size() == 0)) {
                bsw.setVisibility(View.GONE);
            } else {
                bsw.setText("BS Weapons: " + TextUtils.join(", ", profile.bsw));
            }

            if ((profile.ccw == null) || (profile.ccw.size() == 0)) {
                ccw.setVisibility(View.GONE);
            } else {
                ccw.setText("CC Weapons: " + TextUtils.join(", ", profile.ccw));
            }

            if (profile.irr) {
                irr.setImageResource(R.drawable.irregular);
            } else {
                irr.setImageResource(R.drawable.regular);
            }

            if (profile.imp != null) {
                if (profile.imp.equals("F")) {
                    imp.setImageResource(R.drawable.frenzy);
                } else if (profile.imp.equals("X")) {
                    imp.setImageResource(R.drawable.extreme_impetuous);
                } else if (profile.imp.equals("I")) {
                    imp.setImageResource(R.drawable.impetuous);
                }
            }

            if (profile.cube != null) {
                if (profile.cube.equals("X")) {
                    cube.setImageResource(R.drawable.cube);
                } else if (profile.cube.equals("2")) {
                    cube.setImageResource(R.drawable.cube2);
                }
            }

            if (profile.hackable) {
                hackable.setImageResource(R.drawable.hackable);
            }

            int resourceId = UnitListAdapter.getDrawableResource(unit, context, 24);
            imageView.setImageResource(resourceId);

            if (profile.id == 1) {
                ViewCompat.setTransitionName(imageView, UnitListActivity.TRANSITION_IMAGE );
//            ViewCompat.setTransitionName(isc, UnitListActivity.TRANSITION_UNIT_NAME);
            }


        }
    }

    static public class ChildViewHolder extends RecyclerView.ViewHolder {

        private final CardView card;
        private final TextView name;
        private final TextView swc;
        private final TextView cost;
        private final TextView bsw;
        private final TextView ccw;
        private final TextView spec;
        private final TextView note;

        public ChildViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.name);
            swc = (TextView) itemView.findViewById(R.id.swc);
            cost = (TextView) itemView.findViewById(R.id.cost);
            bsw = (TextView) itemView.findViewById(R.id.bsw);
            ccw = (TextView) itemView.findViewById(R.id.ccw);
            spec = (TextView) itemView.findViewById(R.id.spec);
            note = (TextView) itemView.findViewById(R.id.note);
        }

        public void setChild(Unit unit, Child child) {
//            card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "Option Clicked");
//                    m_callback.onOptionSelected(child.id);
//                }
//            });

            name.setText(child.name);

            swc.setText("SWC: " + Double.toString(child.swc));

            cost.setText("C: " + Integer.toString(child.cost));

            if (child.bsw != null && child.bsw.size() > 0) {
                bsw.setText("BSW: " + TextUtils.join(", ", child.bsw));
            } else {
                bsw.setVisibility(View.GONE);
            }

            if (child.ccw != null && child.ccw.size() > 0) {
                ccw.setText("CCW: " + TextUtils.join(", ", child.ccw));
            } else {
                ccw.setVisibility(View.GONE);
            }

            if (child.profile != 0) {
                child.spec.add(unit.profiles.get(child.profile).name);
            }

            if (child.spec != null && child.spec.size() > 0) {
                spec.setText("Spec: " + TextUtils.join(", ", child.spec));
            } else {
                spec.setVisibility(View.GONE);
            }

            if (child.note != null && !child.note.isEmpty()) {
                note.setText("Note: " + child.note);
            } else {
                note.setVisibility(View.GONE);
            }
        }

    }
}
