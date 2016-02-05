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

import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.ArmyUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cgordon on 6/1/2015.
 */
public class ArmyData {

    private final static String TAG = ArmyData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;

    private final String[] armyColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_FACTION,
            InfinityDatabase.COLUMN_NAME,
            InfinityDatabase.COLUMN_ABBREVIATION
    };

    private final String[] armyUnitColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_ARMY_ID,
            InfinityDatabase.COLUMN_AVA,
            InfinityDatabase.COLUMN_ISC,
            InfinityDatabase.COLUMN_LINKABLE,
            InfinityDatabase.COLUMN_FACTION
    };


    public ArmyData(Context context) {
        m_dbHelper = new InfinityDatabase(context);
    }

    public ArmyData(SQLiteDatabase db) {
        m_database = db;
    }

    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }

    public void close() {
        m_database.close();
    }

    public void writeArmy(ArrayList<Army> armies) {

        Iterator it = armies.iterator();

        while (it.hasNext()) {
            Army army = (Army) it.next();

            long armyId = writeArmy(army);

            if (armyId == -1) {
                Log.d(TAG, "Failed insert");
                return;
            }

            ArrayList<ArmyUnit> armyUnits = army.units;
            Iterator unit_it = armyUnits.iterator();
            while (unit_it.hasNext()) {
                ArmyUnit armyUnit = (ArmyUnit) unit_it.next();

                long armyUnitId = writeArmyUnit(armyUnit, armyId);
                if (armyUnitId == -1) {
                    Log.d(TAG, "Failed to write armyUnit");
                    return;
                }

            }
        }
    }

    public List<Army> getArmyList() {
        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_ARMY, armyColumns, null, null, null, null, null, null);

            ArrayList<Army> armyList = new ArrayList<>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Army army = cursorToArmy(cursor);

                armyList.add(army);

                cursor.moveToNext();
            }

            return armyList;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private ArrayList<ArmyUnit> getArmyUnits(long sectorialId) {
        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_ARMY_UNITS, armyUnitColumns, InfinityDatabase.COLUMN_ARMY_ID + "=" + sectorialId, null, null, null, null, null);

            cursor.moveToFirst();

            ArrayList<ArmyUnit> armyUnits = new ArrayList<>();

            while (!cursor.isAfterLast()) {
                ArmyUnit armyUnit = cursorToSectorialUnit(cursor);

                armyUnits.add(armyUnit);

                cursor.moveToNext();
            }

            return armyUnits;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private ArmyUnit cursorToSectorialUnit(Cursor cursor) {
        ArmyUnit armyUnit = new ArmyUnit();

        armyUnit.dbId = cursor.getLong(0);
        armyUnit.sectorialId = cursor.getLong(1);
        armyUnit.ava = cursor.getString(2);
        armyUnit.isc = cursor.getString(3);
        armyUnit.linkable = (cursor.getInt(4) != 0); // boolean
        armyUnit.army = cursor.getString(5);

        return armyUnit;
    }

    private Army cursorToSectorial(Cursor cursor) {
        Army sectorial = new Army();

        sectorial.dbId = cursor.getLong(0);
        sectorial.faction = cursor.getString(1);
        sectorial.name = cursor.getString(2);
        sectorial.abbr = cursor.getString(3);

        return sectorial;
    }

    private long writeArmyUnit(ArmyUnit armyUnit, long sectorialId) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_ARMY_ID, sectorialId);
        v.put(InfinityDatabase.COLUMN_AVA, armyUnit.ava);
        v.put(InfinityDatabase.COLUMN_ISC, armyUnit.isc);
        v.put(InfinityDatabase.COLUMN_LINKABLE, armyUnit.linkable);
        v.put(InfinityDatabase.COLUMN_FACTION, armyUnit.army);

        return m_database.insert(InfinityDatabase.TABLE_ARMY_UNITS, null, v);
    }


    private long writeArmy(Army sectorial) {

        ContentValues v = new ContentValues();
        //v.put(InfinityDatabase.COLUMN_AMMO, w.ammo);

        v.put(InfinityDatabase.COLUMN_FACTION, sectorial.faction);
        v.put(InfinityDatabase.COLUMN_NAME, sectorial.name);
        v.put(InfinityDatabase.COLUMN_ABBREVIATION, sectorial.abbr);

        return m_database.insert(InfinityDatabase.TABLE_ARMY, null, v);

    }

    public Army getArmy(long dbId) {
        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_ARMY, armyColumns, InfinityDatabase.COLUMN_ID + "=" + dbId, null, null, null, null, null);

            cursor.moveToFirst();

            Army army = cursorToArmy(cursor);
            return army;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private Army cursorToArmy(Cursor cursor) {

        Army army = new Army();

        army.dbId = cursor.getLong(0);
        army.faction = cursor.getString(1);
        army.name = cursor.getString(2);
        army.abbr = cursor.getString(3);

        return army;
    }

}
