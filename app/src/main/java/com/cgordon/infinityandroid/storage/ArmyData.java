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
import com.cgordon.infinityandroid.data.ArmyUnitChild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
            InfinityDatabase.COLUMN_FACTION,
            InfinityDatabase.COLUMN_NOTE
    };

    public static final String[] armyUnitChildColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_ARMY_ID,
            InfinityDatabase.COLUMN_ARMY_UNIT_ID,
            InfinityDatabase.COLUMN_ARMY_UNIT_CHILD_ID,
            InfinityDatabase.COLUMN_SWC,
            InfinityDatabase.COLUMN_HIDE  // boolean
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

            ArrayList<ArmyUnit> armyUnits = army.armyUnits;
            Iterator unit_it = armyUnits.iterator();
            while (unit_it.hasNext()) {
                ArmyUnit armyUnit = (ArmyUnit) unit_it.next();

                long armyUnitId = writeArmyUnit(armyUnit, armyId);
                if (armyUnitId == -1) {
                    Log.d(TAG, "Failed to write armyUnit");
                    return;
                } else {
                    if (!writeArmyUnitChildren(armyUnit.children, armyId, armyUnit.id)) {
                        Log.d(TAG, "Failed to write armyUnitChildren");
                        return;

                    }
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
                ArmyUnit armyUnit = cursorToArmyUnit(cursor);

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

    static public Map<Integer, Map<Integer, ArmyUnitChild>> getArmyUnitChildren(long armyId, SQLiteDatabase database) {
        Cursor cursor = null;
        try {
            cursor = database.query(InfinityDatabase.TABLE_ARMY_UNITS_CHILDREN,
                    armyUnitChildColumns, InfinityDatabase.COLUMN_ARMY_ID + "=" + armyId, null,
                    null, null, null, null);

            cursor.moveToFirst();

            Map<Integer, Map<Integer, ArmyUnitChild>> armyUnitChildren = new HashMap<>();

            while (!cursor.isAfterLast()) {
                ArmyUnitChild armyUnitChild =  new ArmyUnitChild();

                armyUnitChild.dbId = cursor.getLong(0);
                armyUnitChild.armyId = cursor.getLong(1);
                armyUnitChild.unitId = cursor.getInt(2);
                armyUnitChild.id = cursor.getInt(3);
                armyUnitChild.swc = cursor.getDouble(4);
                armyUnitChild.hide = (cursor.getInt(5) != 0); // boolean

                Map<Integer, ArmyUnitChild> childList;
                if (armyUnitChildren.containsKey(armyUnitChild.unitId)) {
                    childList = armyUnitChildren.get(armyUnitChild.unitId);
                } else {
                    childList = new HashMap<>();
                    armyUnitChildren.put(armyUnitChild.unitId, childList);
                }

                childList.put(armyUnitChild.id, armyUnitChild);

                cursor.moveToNext();
            }

            return armyUnitChildren;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
//
//    public static ArmyUnitChild cursorToArmyUnitChild(Cursor cursor) {
//        ArmyUnitChild child = new ArmyUnitChild();
//
//        child.dbId = cursor.getLong(0);
//        child.armyId = cursor.getLong(1);
//        child.unitId = cursor.getInt(2);
//        child.id = cursor.getInt(3);
//        child.swc = cursor.getDouble(4);
//        child.hide = (cursor.getInt(5) != 0); // boolean
//
//        return child;
//    }

    private ArmyUnit cursorToArmyUnit(Cursor cursor) {
        ArmyUnit armyUnit = new ArmyUnit();

        armyUnit.dbId = cursor.getLong(0);
        armyUnit.armyId = cursor.getLong(1);
        armyUnit.ava = cursor.getString(2);
        armyUnit.id = cursor.getInt(3);
        armyUnit.linkable = (cursor.getInt(4) != 0); // boolean
        armyUnit.note = cursor.getString(5);

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

    private boolean writeArmyUnitChildren(ArrayList<ArmyUnitChild> children, long armyId, long armyUnitId) {

        Iterator it = children.iterator();

        while (it.hasNext()) {
            ArmyUnitChild child = (ArmyUnitChild) it.next();

            ContentValues v = new ContentValues();
            v.put(InfinityDatabase.COLUMN_ARMY_ID, armyId);
            v.put(InfinityDatabase.COLUMN_ARMY_UNIT_ID, armyUnitId);
            v.put(InfinityDatabase.COLUMN_ARMY_UNIT_CHILD_ID, child.id);
            v.put(InfinityDatabase.COLUMN_SWC, child.swc);
            v.put(InfinityDatabase.COLUMN_HIDE, child.hide);

            if (m_database.insert(InfinityDatabase.TABLE_ARMY_UNITS_CHILDREN, null, v) == -1) {
                return false;
            }
        }

        return true;
    }

    private long writeArmyUnit(ArmyUnit armyUnit, long sectorialId) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_ARMY_ID, sectorialId);
        v.put(InfinityDatabase.COLUMN_AVA, armyUnit.ava);
        v.put(InfinityDatabase.COLUMN_UNIT_ID, armyUnit.id);
        v.put(InfinityDatabase.COLUMN_LINKABLE, armyUnit.linkable);
        v.put(InfinityDatabase.COLUMN_NOTE, armyUnit.note);

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
