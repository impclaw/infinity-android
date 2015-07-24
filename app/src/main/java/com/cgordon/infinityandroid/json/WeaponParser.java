package com.cgordon.infinityandroid.json;

import android.content.Context;
import android.util.JsonReader;

import com.cgordon.infinityandroid.data.Weapon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cgordon on 5/30/2015.
 */
public class WeaponParser {

    private final String TAG = WeaponParser.class.getSimpleName();
    private final Context m_context;

    public WeaponParser(Context context) {
        m_context = context;
    }

    public ArrayList<Weapon> parse(int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        ArrayList<Weapon> weapons = new ArrayList<Weapon>();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                weapons.add(parseWeapon(reader));
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return weapons;
    }

    private Weapon parseWeapon(JsonReader reader) throws IOException {
        reader.beginObject();

        Weapon weapon = new Weapon();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("ammo")) {
                weapon.ammo = reader.nextString();
            } else if (name.equals("burst")) {
                weapon.burst = reader.nextString();
            } else if (name.equals("cc")) {
                weapon.cc = reader.nextString();
            } else if (name.equals("damage")) {
                weapon.damage = reader.nextString();
            } else if (name.equals("long_dist")) {
                weapon.long_dist = reader.nextString();
            } else if (name.equals("long_mod")) {
                weapon.long_mod = reader.nextString();
            } else if (name.equals("max_dist")) {
                weapon.max_dist = reader.nextString();
            } else if (name.equals("max_mod")) {
                weapon.max_mod = reader.nextString();
            } else if (name.equals("medium_dist")) {
                weapon.medium_dist = reader.nextString();
            } else if (name.equals("medium_mod")) {
                weapon.medium_mod = reader.nextString();
            } else if (name.equals("name")) {
                weapon.name = reader.nextString();
            } else if (name.equals("note")) {
                weapon.note = reader.nextString();
            } else if (name.equals("short_dist")) {
                weapon.short_dist = reader.nextString();
            } else if (name.equals("short_mod")) {
                weapon.short_mod = reader.nextString();
            } else if (name.equals("template")) {
                weapon.template = reader.nextString();
            } else if (name.equals("uses")) {
                weapon.uses = reader.nextString();
            } else if (name.equals("attr")) { // attribute
                weapon.attr = reader.nextString();
            } else if (name.equals("suppressive")) {
                weapon.suppressive = reader.nextString();
            } else if (name.equals("alt_profile")) {
                weapon.alt_profile = reader.nextString();
            } else if (name.equals("mode")) {
                weapon.mode = reader.nextString();
            } else {
                throw new IOException("unknown tag in parseWeapon: " + name);
            }
        }
        reader.endObject();

        return weapon;
    }
}
