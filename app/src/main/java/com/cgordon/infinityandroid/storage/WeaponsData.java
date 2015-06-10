package com.cgordon.infinityandroid.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cgordon.infinityandroid.data.Weapon;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by cgordon on 6/1/2015.
 */
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

            if (m_database.insert(InfinityDatabase.TABLE_WEAPONS, null, v ) == -1) {
                Log.d(TAG, "Failed insert");
            }

        }

    }

}
