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
import com.cgordon.infinityandroid.data.CombatGroup;
import com.cgordon.infinityandroid.data.ListElement;
import com.cgordon.infinityandroid.data.Unit;

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
            InfinityDatabase.COLUMN_PROFILE,
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

    public long saveList(String listName, long army, int points, List<Map.Entry<ListElement, Integer>> list, long dbId) {

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

                Map.Entry<ListElement, Integer> listItem = list.get(i);
                if (listItem.getKey() instanceof CombatGroup) {
                    combatGroup = ((CombatGroup) listItem.getKey()).m_id;
                } else {
                    v = new ContentValues();
                    v.put(InfinityDatabase.COLUMN_LIST_ID, listId);
                    Unit unit = (Unit) listItem.getKey();
                    v.put(InfinityDatabase.COLUMN_GROUP, combatGroup);
                    v.put(InfinityDatabase.COLUMN_UNIT_ID, unit.id);
                    v.put(InfinityDatabase.COLUMN_PROFILE, listItem.getValue());

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

    public List<Map.Entry<ListElement, Integer>> getList(long listId) {
        List<Map.Entry<ListElement, Integer>> retval = new ArrayList<>();

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
                CombatGroup cg = new CombatGroup(++combatGroup);
                retval.add(new AbstractMap.SimpleEntry<>((ListElement)cg, 0));
            }

            retval.add(new AbstractMap.SimpleEntry<>((ListElement)unitsData.getUnit(unitId), profile));

            cursor.moveToNext();
        }

        while (combatGroup < 4) {
            CombatGroup cg = new CombatGroup(++combatGroup);
            retval.add(new AbstractMap.SimpleEntry<>((ListElement) cg, 0));
        }

        return retval;

    }

    // rename list

    // delete list

}
