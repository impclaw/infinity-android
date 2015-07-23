package com.cgordon.infinityandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.storage.WeaponsData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cgordon on 7/23/2015.
 */
public class WeaponsAdapter extends ArrayAdapter<String> {

    private final Map<String, Weapon> m_weaponsList;
    private final Context m_context;
    private final int m_resource;
    List<String> m_weapons;

    public WeaponsAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        m_weapons = new ArrayList<>();

        WeaponsData wd = new WeaponsData(context);
        wd.open();
        m_weaponsList = wd.getWeapons();
        wd.close();

        m_context = context;

        m_resource = resource;


        // sometimes weapons have alternate profiles and this means that there are essentially two
        // weapons in one, representing alternate firing modes.
        Iterator it = objects.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            // sometimes a weapon is listed twice.  The easiest way to record this is to look for it
            // and remove the text so that the actual weapon can be found in the weapons list.
            name = name.replace("(2)", "").trim();

            m_weapons.add(name);

            Weapon weapon = m_weaponsList.get(name);

            // check the alt_profile rabbit hole
            while (weapon.alt_profile != null) {
                weapon = m_weaponsList.get(weapon.alt_profile);
                m_weapons.add(weapon.name);
            }
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if (row == null) {
            LayoutInflater inflater = ((Activity)m_context).getLayoutInflater();
            row = inflater.inflate(m_resource, parent, false);
        }


        Weapon weaponData = m_weaponsList.get(m_weapons.get(position));

        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(weaponData.name);

        TextView burst = (TextView) row.findViewById(R.id.burst);
        burst.setText("B: " + weaponData.burst);

        TextView damage = (TextView) row.findViewById(R.id.damage);
        damage.setText("Dmg: " + weaponData.damage);

        TextView ammo = (TextView) row.findViewById(R.id.ammo);
        ammo.setText("Ammo: " + weaponData.ammo);

        TextView suppressive = (TextView) row.findViewById(R.id.suppressive);
        if (weaponData.suppressive == null || weaponData.suppressive.isEmpty()) {
            suppressive.setText("Suppressive: No");
        } else {
            suppressive.setText("Suppressive: " + weaponData.suppressive);
        }

        TextView cc = (TextView) row.findViewById(R.id.cc);
        cc.setText("CC: " + weaponData.cc);

        TextView ranges = (TextView) row.findViewById(R.id.ranges);
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
            sb.append(": ").append(weaponData.max_mod).append(", ");
        }
        ranges.setText(sb.toString());

        String noteText = "";
        TextView note = (TextView) row.findViewById(R.id.note);
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

        if (noteText.isEmpty()) {
            note.setVisibility(View.GONE);
        } else {
            note.setText(noteText);
        }

        return row;

    }

    @Override
    public int getCount() {
        return m_weapons.size();
    }
}
