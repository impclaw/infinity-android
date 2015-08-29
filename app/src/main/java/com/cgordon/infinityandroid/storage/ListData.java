/*
 * Copyright 2015 by Chris Gordon
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
import android.database.sqlite.SQLiteDatabase;

public class ListData {

    private final static String TAG = ArmyData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;

    private final String[] listListsColumns = {
        InfinityDatabase.COLUMN_ID,
        InfinityDatabase.COLUMN_NAME,
        InfinityDatabase.COLUMN_ARMY_ID,
        InfinityDatabase.COLUMN_POINTS
    };

    private final String[] listUnitsColumns = {
        InfinityDatabase.COLUMN_ID,
        InfinityDatabase.COLUMN_LIST_ID,
        InfinityDatabase.COLUMN_UNIT_ID,
        InfinityDatabase.COLUMN_PROFILE
    };

    private final String[] listContentsColumns = {

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

    /**
     *
     * @param name Name of the list - does not have to be unique
     * @param armyId Army/Sectorial primary key
     * @param points Points for the list
     * @return Primary key id for the new list
     */
    public long addList(String name, long armyId, int points) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_NAME, name);
        v.put(InfinityDatabase.COLUMN_ARMY_ID, armyId);
        v.put(InfinityDatabase.COLUMN_POINTS, points);

        return m_database.insert(InfinityDatabase.TABLE_ARMY_LISTS, null, v);

    }

    public boolean deleteList(long listId) {


        return false;
    }

    public boolean renameList(long listId, String name) {
        return false;
    }

    /**
     *
     * @param listId The list primary key id
     * @param unitId The unit primary key id
     * @param profile The profile index within the unit
     * @return The primary key id for this item within the listContents
     */
    public long addUnit(long listId, long unitId, int profile) {
        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_LIST_ID, listId);
        v.put(InfinityDatabase.COLUMN_UNIT_ID, unitId);
        v.put(InfinityDatabase.COLUMN_PROFILE, profile);

        return m_database.insert(InfinityDatabase.TABLE_ARMY_LIST_UNITS, null, v);
    }

    public boolean removeUnit(long listContentId) {

//        m_database.insert(InfinityDatabase.TABLE_ARMY_LISTS, )

        return false;
    }

}
