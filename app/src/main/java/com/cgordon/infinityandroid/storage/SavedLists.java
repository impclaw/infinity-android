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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cgordon.infinityandroid.data.ListElement;

import java.util.List;
import java.util.Map;

public class SavedLists {

    private static final String TAG = SavedLists.class.getSimpleName();
    private final InfinityDatabase m_dbHelper;
    private SQLiteDatabase m_database;

    public SavedLists(Context context) {
        m_dbHelper = new InfinityDatabase(context);
    }
    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }
    public void close() {
        m_database.close();
    }

    public boolean saveList(String name, long army, int points, List<Map.Entry<ListElement, Integer>> list) {
        boolean retval = false;

        Log.d(TAG, "Saving!");

        return retval;
    }

}
