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
import com.cgordon.infinityandroid.data.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
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
            InfinityDatabase.COLUMN_TYPE,// 0 = combat group, 1 = Unit
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

    public boolean saveList(String name, long army, int points, List<Map.Entry<ListElement, Integer>> list) {
        boolean retval = false;

        m_database.beginTransaction();

        ContentValues v = new ContentValues();
        v.put(InfinityDatabase.COLUMN_NAME, name);
        v.put(InfinityDatabase.COLUMN_ARMY_ID, army);
        v.put(InfinityDatabase.COLUMN_POINTS, points);

        long listId = m_database.insert(InfinityDatabase.TABLE_ARMY_LISTS, null, v);

        if (listId == -1) {
            Log.d(TAG, "List insert failed");
        } else {

            retval = true;
            for( int i = 0; i < list.size(); i++) {

                Map.Entry<ListElement, Integer> listItem = list.get(i);
                v = new ContentValues();
                v.put(InfinityDatabase.COLUMN_LIST_ID, listId);
                if (listItem.getKey() instanceof CombatGroup) {
                    CombatGroup combatGroup = (CombatGroup) listItem.getKey();
                    v.put(InfinityDatabase.COLUMN_TYPE, 0);
                    v.put(InfinityDatabase.COLUMN_PROFILE, combatGroup.m_id);
                } else {
                    Unit unit = (Unit) listItem.getKey();
                    v.put(InfinityDatabase.COLUMN_TYPE, 1);
                    v.put(InfinityDatabase.COLUMN_UNIT_ID, unit.dbId);
                    v.put(InfinityDatabase.COLUMN_PROFILE, listItem.getValue());
                }

                if (m_database.insert(InfinityDatabase.TABLE_ARMY_LIST_UNITS, null, v) == -1) {
                    retval = false;
                    Log.d(TAG, "Unit List insert failed!: " + i);
                    break;
                }

            }
        }

        if (retval) {
            m_database.setTransactionSuccessful();
        }
        m_database.endTransaction();
        return retval;
    }

    public List<ArmyList> getArmyLists() {

        List<ArmyList> lists = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_ARMY_LISTS, listColumns, null, null,
                    null, null, null, null);

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

        Cursor cursor = null;
        cursor = m_database.query(InfinityDatabase.TABLE_ARMY_LIST_UNITS, listUnitsColumns,
                InfinityDatabase.COLUMN_LIST_ID + "=" + listId, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Log.d(TAG, "ID: " + cursor.getLong(0));
            Log.d(TAG, "List ID: " + cursor.getLong(1));
            Log.d(TAG, "Type: " + cursor.getInt(2));  // 0 = combat group, 1 = Unit
            Log.d(TAG, "Unit ID: " + cursor.getInt(3));
            Log.d(TAG, "Profile: " + cursor.getInt(4));

            cursor.moveToNext();
        }

        return retval;

    }

    // rename list

    // delete list

}
