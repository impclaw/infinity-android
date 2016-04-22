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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Child;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.interfaces.ChildSelectedListener;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UnitAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String FORWARD_OBSERVER = "Forward Observer";

    private final int TYPE_PROFILE = 0;
    private final int TYPE_CHILD = 1;
    private final int TYPE_WEAPON = 2;

    private final Map<String, Weapon> m_masterWeaponsList;
    private final Context m_context;
    private final boolean m_clickableChild;
    private final ChildSelectedListener m_listener;

    private Unit m_unit;
    private int m_selectedChildId;

    private ArrayList<Weapon> m_unitWeapons;
    private ArrayList<Child> m_children;

    public UnitAdapter(Context context, ChildSelectedListener listener, boolean clickableChild) {
        m_context = context;
        m_listener = listener;
        WeaponsData wd = new WeaponsData(context);
        wd.open();
        m_masterWeaponsList = wd.getWeapons();
        wd.close();

        m_clickableChild = clickableChild;
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
            vh = new ChildViewHolder(v, m_listener, m_clickableChild);
        } else { // TYPE_WEAPON
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weapon, parent, false);
            vh = new WeaponViewHolder(v);

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
        } else if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;

            // The position includes all the profile entries listed above the child entries.
            // Subtract the number of profiles from the position to get the actual child index
            Child child = m_children.get(position - m_unit.profiles.size());
            childViewHolder.setChild(m_unit, child);
        } else if (holder instanceof WeaponViewHolder) {
            WeaponViewHolder weaponViewHolder = (WeaponViewHolder) holder;

            // the position includes all the profile/child entries above.  Subtract them to get the
            // real index
            Weapon weapon = m_unitWeapons.get(position - m_unit.profiles.size() - m_children.size());
            weaponViewHolder.setWeapon(weapon);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < m_unit.profiles.size())
            return TYPE_PROFILE;
        else if (position < m_unit.profiles.size() + m_children.size())
            return TYPE_CHILD;
        else
            return TYPE_WEAPON;
    }

    @Override
    public int getItemCount() {
        if (m_unit == null)
            return 0;
        else
            return m_unit.profiles.size() + m_children.size() + m_unitWeapons.size();

    }

    public void setUnit(Unit unit, int selectedChildId) {
        m_unit = unit;
        m_selectedChildId = selectedChildId;
        if (selectedChildId == -1) {
            m_children = unit.children;
        } else {
            m_children = new ArrayList<>();
            m_children.add(unit.getChild(selectedChildId));
        }

        setUnitWeaponsList();

        notifyDataSetChanged();
    }

    private void setUnitWeaponsList() {
        if (m_unit == null)
            return;

        ArrayList<String> bsw = new ArrayList<>();
        ArrayList<String> ccw = new ArrayList<>();

        boolean forwardObserver = false;
        //boolean impersonation = false;

        Iterator it = m_unit.profiles.iterator();
        while (it.hasNext()) {
            Profile profile = (Profile) it.next();
            bsw.addAll(profile.bsw);
            ccw.addAll(profile.ccw);

            if (profile.spec.indexOf(FORWARD_OBSERVER) != -1) {
                forwardObserver = true;
            }
        }

        it = m_children.iterator();
        while (it.hasNext()) {
            Child child = (Child) it.next();
            bsw.addAll(child.bsw);
            ccw.addAll(child.ccw);
            if (child.spec.indexOf(FORWARD_OBSERVER) != -1) {
                forwardObserver = true;
            }
        }

        if (forwardObserver) {
            bsw.add(FORWARD_OBSERVER);
            bsw.add("Flash Pulse");
        }

        // remove duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(bsw);
        bsw.clear();
        bsw.addAll(hs);

        hs.clear();
        hs.addAll(ccw);
        ccw.clear();
        ccw.addAll(hs);

        Collections.sort(bsw, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });

        Collections.sort(ccw, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });

        bsw.addAll(ccw);
        bsw.add("Discover");
        bsw.add("Suppressive Fire");

        m_unitWeapons = new ArrayList<>();
        for (int i = 0; i < bsw.size(); i++) {
            String name = bsw.get(i);
            if (name.endsWith(" (2)")) {
                name = name.substring(0, name.length() - 4);
            }
            m_unitWeapons.add(m_masterWeaponsList.get(name));
        }

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

//            if (profile.id == 1) {
//                ViewCompat.setTransitionName(imageView, UnitListActivity.TRANSITION_IMAGE);
////            ViewCompat.setTransitionName(isc, UnitListActivity.TRANSITION_UNIT_NAME);
//            }


        }
    }

    static public class ChildViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private int id;
        private final CardView card;
        private final TextView name;
        private final TextView swc;
        private final TextView cost;
        private final TextView bsw;
        private final TextView ccw;
        private final TextView spec;
        private final TextView note;
        private final ChildSelectedListener m_listener;

        public ChildViewHolder(View itemView, ChildSelectedListener listener, boolean clickable) {
            super(itemView);

            id = -1;

            m_listener = listener;

            card = (CardView) itemView.findViewById(R.id.card_view);
            card.setOnClickListener(this);
            card.setClickable(clickable);
            name = (TextView) itemView.findViewById(R.id.name);
            swc = (TextView) itemView.findViewById(R.id.swc);
            cost = (TextView) itemView.findViewById(R.id.cost);
            bsw = (TextView) itemView.findViewById(R.id.bsw);
            ccw = (TextView) itemView.findViewById(R.id.ccw);
            spec = (TextView) itemView.findViewById(R.id.spec);
            note = (TextView) itemView.findViewById(R.id.note);
        }

        public void setChild(Unit unit, Child child) {
            id = child.id;

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

        @Override
        public void onClick(View v) {
            if (m_listener != null)
                m_listener.onChildSelected(id);
        }
    }

    static public class WeaponViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView burst;
        private final TextView damage;
        private final TextView ammo;
        private final TextView suppressive;
        private final TextView cc;
        private final TextView ranges;
        private final TextView note;

        public WeaponViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            burst = (TextView) itemView.findViewById(R.id.burst);
            damage = (TextView) itemView.findViewById(R.id.damage);
            ammo = (TextView) itemView.findViewById(R.id.ammo);
            suppressive = (TextView) itemView.findViewById(R.id.suppressive);
            cc = (TextView) itemView.findViewById(R.id.cc);
            ranges = (TextView) itemView.findViewById(R.id.ranges);
            note = (TextView) itemView.findViewById(R.id.note);
        }

        public void setWeapon(Weapon weapon) {
            if (weapon == null) {
                weapon = new Weapon();
            }

            name.setText(weapon.name);
            burst.setText("B: " + weapon.burst);
            damage.setText("Dmg: " + weapon.damage);
            ammo.setText("Ammo: " + weapon.ammo);
            if (weapon.suppressive == null || weapon.suppressive.isEmpty()) {
                suppressive.setText("Suppressive: No, ");
            } else {
                suppressive.setText("Suppressive: " + weapon.suppressive + ", ");
            }
            cc.setText("CC: " + weapon.cc);
            StringBuffer sb = new StringBuffer();

            if (!weapon.short_dist.equals("--")) {
                sb.append("0-").append(weapon.short_dist);
                sb.append(": ").append(weapon.short_mod);
            }
            if (!weapon.medium_dist.equals("--")) {
                sb.append(", ").append(weapon.short_dist).append("-").append(weapon.medium_dist);
                sb.append(": ").append(weapon.medium_mod);
            }
            if (!weapon.long_dist.equals("--")) {
                sb.append(", ").append(weapon.medium_dist).append("-").append(weapon.long_dist);
                sb.append(": ").append(weapon.long_mod);
            }
            if (!weapon.max_dist.equals("--")) {
                sb.append(", ").append(weapon.long_dist).append("-").append(weapon.max_dist);
                sb.append(": ").append(weapon.max_mod);
            }
            ranges.setText(sb.toString());

            String noteText = "";
            if (weapon.note != null && !weapon.note.isEmpty()) {
                noteText = weapon.note;
            }

            if (weapon.template != null
                    && !weapon.template.isEmpty()
                    && !weapon.template.toLowerCase().equals("no")) {
                if (!noteText.isEmpty()) {
                    noteText += ", ";
                }
                noteText += weapon.template;
            }

            if (weapon.uses != null && !weapon.uses.isEmpty()) {
                if (!noteText.isEmpty()) {
                    noteText += ", ";
                }
                noteText += "Uses: " + weapon.uses;

            }

            if (noteText.isEmpty()) {
                note.setVisibility(View.GONE);
            } else {
                note.setText(noteText);
            }
        }
    }
}
