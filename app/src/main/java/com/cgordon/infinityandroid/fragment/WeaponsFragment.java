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

package com.cgordon.infinityandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.activity.MainActivity;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.storage.WeaponsData;

public class WeaponsFragment extends Fragment {

    private WeaponsData wd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weapon, container, false);

        Bundle arguments = getArguments();
        Weapon weaponData = arguments.getParcelable(MainActivity.WEAPON);

        if (weaponData == null) {
            weaponData = new Weapon();
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(weaponData.name);

        TextView burst = (TextView) view.findViewById(R.id.burst);
        burst.setText("B: " + weaponData.burst);

        TextView damage = (TextView) view.findViewById(R.id.damage);
        damage.setText("Dmg: " + weaponData.damage);

        TextView ammo = (TextView) view.findViewById(R.id.ammo);
        ammo.setText("Ammo: " + weaponData.ammo);

        TextView suppressive = (TextView) view.findViewById(R.id.suppressive);
        if (weaponData.suppressive == null || weaponData.suppressive.isEmpty()) {
            suppressive.setText("Suppressive: No, ");
        } else {
            suppressive.setText("Suppressive: " + weaponData.suppressive + ", ");
        }

        TextView cc = (TextView) view.findViewById(R.id.cc);
        cc.setText("CC: " + weaponData.cc);

        TextView ranges = (TextView) view.findViewById(R.id.ranges);
        StringBuffer sb = new StringBuffer();

        if (!weaponData.short_dist.equals("--")) {
            sb.append("0-").append(weaponData.short_dist);
            sb.append(": ").append(weaponData.short_mod);
        }
        if (!weaponData.medium_dist.equals("--")) {
            sb.append(", ").append(weaponData.short_dist).append("-").append(weaponData.medium_dist);
            sb.append(": ").append(weaponData.medium_mod);
        }
        if (!weaponData.long_dist.equals("--")) {
            sb.append(", ").append(weaponData.medium_dist).append("-").append(weaponData.long_dist);
            sb.append(": ").append(weaponData.long_mod);
        }
        if (!weaponData.max_dist.equals("--")) {
            sb.append(", ").append(weaponData.long_dist).append("-").append(weaponData.max_dist);
            sb.append(": ").append(weaponData.max_mod);
        }
        ranges.setText(sb.toString());

        String noteText = "";
        TextView note = (TextView) view.findViewById(R.id.note);
        if (weaponData.note != null && !weaponData.note.isEmpty()) {
            noteText = weaponData.note;
        }

        if (weaponData.template != null
                && !weaponData.template.isEmpty()
                && !weaponData.template.toLowerCase().equals("no")) {
            if (!noteText.isEmpty()) {
                noteText += ", ";
            }
            noteText += weaponData.template;
        }

        if (weaponData.uses != null && !weaponData.uses.isEmpty()) {
            if (!noteText.isEmpty()) {
                noteText += ", ";
            }
            noteText += "Uses: " + weaponData.uses;

        }

        if (noteText.isEmpty()) {
            note.setVisibility(View.GONE);
        } else {
            note.setText(noteText);
        }

        return view;
    }
}
