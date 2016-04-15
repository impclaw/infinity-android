/*
 * Copyright 2016 by Chris Gordon
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

import com.cgordon.infinityandroid.data.ArmyList;
import com.cgordon.infinityandroid.data.CombatGroupElement;
import com.cgordon.infinityandroid.data.ListElement;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.UnitElement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListData {

    private static final String TAG = ListData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;

    private String[] listColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_NAME,
            InfinityDatabase.COLUMN_ARMY_ID,
            InfinityDatabase.COLUMN_POINTS,
    };

    private String[] listUnitsColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_LIST_ID,
            InfinityDatabase.COLUMN_GROUP,
            InfinityDatabase.COLUMN_UNIT_ID,
            InfinityDatabase.COLUMN_CHILD_ID,
    };

    public ListData(Context context) {
        m_dbHelper = new InfinityDatabase(context);
    }
    public ListData(SQLiteDatabase db) {
        m_database = db;
    }
    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }
    public void close() {
        m_database.close();
    }

    public long saveList(String listName, long army, int points, List<ListElement> list, long dbId) {

        m_database.beginTransaction();

        long listId;

        Cursor cursor = m_database.query(InfinityDatabase.TABLE_ARMY_LISTS, listColumns,
                InfinityDatabase.COLUMN_ID + "=" + dbId, null, null, null, null, null);

        // there's an existing list under this name and we should overwrite
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            listId = cursor.getLong(0);

            Log.d(TAG, "Deleting List: " + m_database.delete(InfinityDatabase.TABLE_ARMY_LISTS, InfinityDatabase.COLUMN_ID +"=" + listId, null));
            Log.d(TAG, "Deleting List Units: " + m_database.delete(InfinityDatabase.TABLE_ARMY_LIST_UNITS, InfinityDatabase.COLUMN_LIST_ID +"=" + listId, null));

        }

        ContentValues v = new ContentValues();
        v.put(InfinityDatabase.COLUMN_NAME, listName);
        v.put(InfinityDatabase.COLUMN_ARMY_ID, army);
        v.put(InfinityDatabase.COLUMN_POINTS, points);

        listId = m_database.insert(InfinityDatabase.TABLE_ARMY_LISTS, null, v);

        if (listId == -1) {
            Log.d(TAG, "List insert failed");
        } else {

            int combatGroup = 0;

            for( int i = 0; i < list.size(); i++) {

                ListElement listElement = list.get(i);
                if (listElement instanceof CombatGroupElement) {
                    combatGroup = ((CombatGroupElement) listElement).m_id;
                } else {
                    v = new ContentValues();
                    v.put(InfinityDatabase.COLUMN_LIST_ID, listId);
                    UnitElement unitElement = (UnitElement) listElement;
                    v.put(InfinityDatabase.COLUMN_GROUP, combatGroup);
                    v.put(InfinityDatabase.COLUMN_UNIT_ID, unitElement.unitId);
                    v.put(InfinityDatabase.COLUMN_CHILD_ID, unitElement.child);

                    if (m_database.insert(InfinityDatabase.TABLE_ARMY_LIST_UNITS, null, v) == -1) {
                        listId = -1;
                        Log.d(TAG, "Unit List insert failed!: " + i);
                        break;
                    }
                }


            }
        }

        if (listId != -1) {
            m_database.setTransactionSuccessful();
        }

        m_database.endTransaction();
        return listId;
    }

    public boolean deleteList(long listId) {
        boolean retval = false;
        Log.d(TAG, "Deleting List: " + m_database.delete(InfinityDatabase.TABLE_ARMY_LISTS, InfinityDatabase.COLUMN_ID +"=" + listId, null));
        Log.d(TAG, "Deleting List Units: " + m_database.delete(InfinityDatabase.TABLE_ARMY_LIST_UNITS, InfinityDatabase.COLUMN_LIST_ID +"=" + listId, null));
        retval = true;

        return retval;

    }

    public List<ArmyList> getArmyLists() {

        List<ArmyList> lists = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_ARMY_LISTS, listColumns, null, null,
                    null, null, InfinityDatabase.COLUMN_ID + " DESC", null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                ArmyList armyList = new ArmyList();

                armyList.dbId = cursor.getLong(0);
                armyList.name = cursor.getString(1);
                armyList.armyId = cursor.getLong(2);
                armyList.points = cursor.getInt(3);

                lists.add(armyList);

                cursor.moveToNext();
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return lists;
    }

    public List<ListElement> getList(long listId) {
        List<ListElement> retval = new ArrayList<>();

        UnitsData unitsData = new UnitsData(m_database);

        Cursor cursor = null;
        cursor = m_database.query(InfinityDatabase.TABLE_ARMY_LIST_UNITS, listUnitsColumns,
                InfinityDatabase.COLUMN_LIST_ID + "=" + listId, null, null, null, null, null);

        cursor.moveToFirst();

        int combatGroup = 0;

        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            //long listId = cursor.getLong(1);
            int group = cursor.getInt(2);
            int unitId = cursor.getInt(3);
            int profile = cursor.getInt(4);

            while (combatGroup < group) {
                CombatGroupElement cg = new CombatGroupElement(++combatGroup);
                retval.add(cg);
            }

            UnitElement unit = new UnitElement(id, group, unitId, profile);
            retval.add(unit);

            cursor.moveToNext();
        }

        while (combatGroup < 4) {
            CombatGroupElement cg = new CombatGroupElement(++combatGroup);
            retval.add(cg);
        }

        return retval;

    }

    // rename list

    // delete list

}
