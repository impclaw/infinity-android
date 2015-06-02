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
public class UnitsData {


    private final static String TAG = UnitsData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;
    private String[] allColumns = {
            InfinityDatabase.COLUMN_ID,

    };

    public UnitsData(Context context) {
        m_dbHelper = new InfinityDatabase(context);
    }

    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }

    public void writeUnits(ArrayList<Weapon> weapons) {
        Iterator<Weapon> weapon_it = weapons.iterator();
        while (weapon_it.hasNext()) {
            Weapon w = weapon_it.next();

            ContentValues v = new ContentValues();
            v.put(InfinityDatabase.COLUMN_AMMO, w.ammo);


            if (m_database.insert(InfinityDatabase.TABLE_WEAPONS, null, v ) == -1) {
                Log.d(TAG, "Failed insert");
            }

        }

    }

}
