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

package com.cgordon.infinityandroid.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cgordon.infinityandroid.data.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WeaponsData {

    private final static String TAG = WeaponsData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;
    private String[] allColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_AMMO,
            InfinityDatabase.COLUMN_BURST,
            InfinityDatabase.COLUMN_CC,
            InfinityDatabase.COLUMN_DAMAGE,
            InfinityDatabase.COLUMN_EM_VUL,
            InfinityDatabase.COLUMN_LONG_DIST,
            InfinityDatabase.COLUMN_LONG_MOD,
            InfinityDatabase.COLUMN_MAX_DIST,
            InfinityDatabase.COLUMN_MAX_MOD,
            InfinityDatabase.COLUMN_MEDIUM_DIST,
            InfinityDatabase.COLUMN_MEDIUM_MOD,
            InfinityDatabase.COLUMN_NAME,
            InfinityDatabase.COLUMN_NOTE,
            InfinityDatabase.COLUMN_SHORT_DIST,
            InfinityDatabase.COLUMN_SHORT_MOD,
            InfinityDatabase.COLUMN_TEMPLATE,
            InfinityDatabase.COLUMN_USES,
            InfinityDatabase.COLUMN_ATTR,
            InfinityDatabase.COLUMN_SUPPRESSIVE,
            InfinityDatabase.COLUMN_ALT_PROFILE,
            InfinityDatabase.COLUMN_MODE,
    };

    public WeaponsData(Context context) {
        m_dbHelper = new InfinityDatabase(context);
    }

    public WeaponsData(SQLiteDatabase db) {
        m_database = db;
    }

    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }

    public void close() {
        m_database.close();
    }

    public void writeWeapons(ArrayList<Weapon> weapons) {
        Iterator<Weapon> weapon_it = weapons.iterator();
        while (weapon_it.hasNext()) {
            Weapon w = weapon_it.next();

            ContentValues v = new ContentValues();
            v.put(InfinityDatabase.COLUMN_AMMO, w.ammo);
            v.put(InfinityDatabase.COLUMN_BURST, w.burst);
            v.put(InfinityDatabase.COLUMN_CC, w.cc);
            v.put(InfinityDatabase.COLUMN_DAMAGE, w.damage);
            v.put(InfinityDatabase.COLUMN_EM_VUL, w.em_vul);
            v.put(InfinityDatabase.COLUMN_LONG_DIST, w.long_dist);
            v.put(InfinityDatabase.COLUMN_LONG_MOD, w.long_mod);
            v.put(InfinityDatabase.COLUMN_MAX_DIST, w.max_dist);
            v.put(InfinityDatabase.COLUMN_MAX_MOD, w.max_mod);
            v.put(InfinityDatabase.COLUMN_MEDIUM_DIST, w.medium_dist);
            v.put(InfinityDatabase.COLUMN_MEDIUM_MOD, w.medium_mod);
            v.put(InfinityDatabase.COLUMN_NAME, w.name);
            v.put(InfinityDatabase.COLUMN_NOTE, w.note);
            v.put(InfinityDatabase.COLUMN_SHORT_DIST, w.short_dist);
            v.put(InfinityDatabase.COLUMN_SHORT_MOD, w.short_mod);
            v.put(InfinityDatabase.COLUMN_TEMPLATE, w.template);
            v.put(InfinityDatabase.COLUMN_USES, w.uses);
            v.put(InfinityDatabase.COLUMN_ATTR, w.attr);
            v.put(InfinityDatabase.COLUMN_SUPPRESSIVE, w.suppressive);
            v.put(InfinityDatabase.COLUMN_ALT_PROFILE, w.alt_profile);
            v.put(InfinityDatabase.COLUMN_MODE, w.mode);

            if (m_database.insert(InfinityDatabase.TABLE_WEAPONS, null, v) == -1) {
                Log.d(TAG, "Failed insert");
            }

        }

    }

    public Map<String, Weapon> getWeapons() {

        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_WEAPONS, allColumns, null, null,
                    null, null, null, null);

            cursor.moveToFirst();

            Map<String, Weapon> weapons = new HashMap<>();

            while (!cursor.isAfterLast()) {
                Weapon weapon = cursorToWeapon(cursor);

                weapons.put(weapon.name, weapon);

                cursor.moveToNext();
            }

            return weapons;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Weapon cursorToWeapon(Cursor cursor) {
        Weapon weapon = new Weapon();

        //InfinityDatabase.COLUMN_ID,
        weapon.ammo = cursor.getString(1);
        weapon.burst = cursor.getString(2);
        weapon.cc = cursor.getString(3);
        weapon.damage = cursor.getString(4);
        weapon.em_vul = cursor.getString(5);
        weapon.long_dist = cursor.getString(6);
        weapon.long_mod = cursor.getString(7);
        weapon.max_dist = cursor.getString(8);
        weapon.max_mod = cursor.getString(9);
        weapon.medium_dist = cursor.getString(10);
        weapon.medium_mod = cursor.getString(11);
        weapon.name = cursor.getString(12);
        weapon.note = cursor.getString(13);
        weapon.short_dist = cursor.getString(14);
        weapon.short_mod = cursor.getString(15);
        weapon.template = cursor.getString(16);
        weapon.uses = cursor.getString(17);
        weapon.attr = cursor.getString(18);
        weapon.suppressive = cursor.getString(19);
        weapon.alt_profile = cursor.getString(20);
        weapon.mode = cursor.getString(21);

        return weapon;
    }

}
