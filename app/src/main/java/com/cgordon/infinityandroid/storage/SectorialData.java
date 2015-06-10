package com.cgordon.infinityandroid.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.SectorialUnit;
import com.cgordon.infinityandroid.data.Sectorial;
import com.cgordon.infinityandroid.data.Sectorial;
import com.cgordon.infinityandroid.data.Weapon;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by cgordon on 6/1/2015.
 */
public class SectorialData {

    public static final String ARMY_PAN_OCEANIA = "PanOceania";
    public static final String ARMY_YU_JING = "Yu Jing";
    public static final String ARMY_ARIADNA = "Ariadna";
    public static final String ARMY_NOMADS = "Nomads";
    public static final String ARMY_HAQQISLAM = "Haqqislam";
    public static final String ARMY_COMBINED_ARMY = "Combined Army";
    public static final String ARMY_ALEPH = "Aleph";
    public static final String ARMY_TOHAA = "Tohaa";

    private final static String TAG = SectorialData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;

    private final String[] sectorialColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_ARMY,
            InfinityDatabase.COLUMN_NAME,
            InfinityDatabase.COLUMN_ABBREVIATION
    };

    private final String[] sectorialUnitColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_SECTORIAL_ID,
            InfinityDatabase.COLUMN_AVA,
            InfinityDatabase.COLUMN_ISC,
            InfinityDatabase.COLUMN_LINKABLE ,
            InfinityDatabase.COLUMN_ARMY
    };


    public SectorialData(Context context) {
        m_dbHelper = new InfinityDatabase(context);
    }

    public SectorialData(SQLiteDatabase db) {
        m_database = db;
    }

    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }

    public void close() {
        m_database.close();
    }

    public void writeSectorial(ArrayList<Sectorial> sectorials) {

        Iterator it = sectorials.iterator();

        while (it.hasNext()) {
            Sectorial sectorial = (Sectorial)it.next();

            long sectorialId = writeSectorial(sectorial) ;

            if (sectorialId == -1) {
                Log.d(TAG, "Failed insert");
                return;
            }

            ArrayList<SectorialUnit> sectorialUnits = sectorial.units;
            Iterator sectorialUnitIt = sectorialUnits.iterator();
            while (sectorialUnitIt.hasNext()) {
                SectorialUnit sectorialUnit = (SectorialUnit) sectorialUnitIt.next();

                long sectorialUnitId = writeSectorialUnit(sectorialUnit, sectorialId);
                if (sectorialUnitId == -1) {
                    Log.d(TAG, "Failed to write sectorialUnit");
                    return;
                }

            }
        }
    }

    public ArrayList<Sectorial> getAllSectorials() {
        return processSectorial(m_database.query(InfinityDatabase.TABLE_SECTORIAL, sectorialColumns, null, null, null, null, null, null));

    }

    public ArrayList<Sectorial> getSectorials(String army) {
        return processSectorial(m_database.query(InfinityDatabase.TABLE_SECTORIAL, sectorialColumns, InfinityDatabase.COLUMN_ARMY + "='" + army + "'", null, null, null, null, null));
    }

    ArrayList<Sectorial> processSectorial(Cursor cursor)
    {
        cursor.moveToFirst();

        ArrayList<Sectorial> sectorials = new ArrayList<Sectorial>();

        while (!cursor.isAfterLast()) {
            Sectorial sectorial = cursorToSectorial(cursor);

            Log.d(TAG, "Sectorial ISC: " + sectorial.name);

            // read sectorialUnits
            ArrayList<SectorialUnit> sectorialUnits = getSectorialUnits(sectorial.dbId);
            sectorial.units = sectorialUnits;

            sectorials.add(sectorial);

            cursor.moveToNext();
        }

        return sectorials;

    }
    private ArrayList<SectorialUnit> getSectorialUnits(long sectorialId) {
        Cursor cursor = m_database.query(InfinityDatabase.TABLE_SECTORIAL_UNITS, sectorialUnitColumns, InfinityDatabase.COLUMN_SECTORIAL_ID + "=" + sectorialId, null, null, null, null, null);

        cursor.moveToFirst();

        ArrayList<SectorialUnit> sectorialUnits = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            SectorialUnit sectorialUnit = cursorToSectorialUnit(cursor);

            sectorialUnits.add(sectorialUnit);

            cursor.moveToNext();
        }

        return sectorialUnits;

    }

    private SectorialUnit cursorToSectorialUnit(Cursor cursor) {
        SectorialUnit sectorialUnit = new SectorialUnit();

        sectorialUnit.dbId = cursor.getLong(0);
        sectorialUnit.sectorialId = cursor.getLong(1);
        sectorialUnit.ava = cursor.getString(2);
        sectorialUnit.isc = cursor.getString(3);
        sectorialUnit.linkable = (cursor.getInt(4) != 0); // boolean
        sectorialUnit.army = cursor.getString(5);

        return sectorialUnit;
    }

    private Sectorial cursorToSectorial(Cursor cursor) {
        Sectorial sectorial = new Sectorial();

        sectorial.dbId = cursor.getLong(0);
        sectorial.army = cursor.getString(1);
        sectorial.name = cursor.getString(2);
        sectorial.abbr = cursor.getString(3);

        return sectorial;
    }

    private long writeSectorialUnit(SectorialUnit sectorialUnit, long sectorialId) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_SECTORIAL_ID, sectorialId);
        v.put(InfinityDatabase.COLUMN_AVA, sectorialUnit.ava);
        v.put(InfinityDatabase.COLUMN_ISC, sectorialUnit.isc);
        v.put(InfinityDatabase.COLUMN_LINKABLE, sectorialUnit.linkable);
        v.put(InfinityDatabase.COLUMN_ARMY, sectorialUnit.army);

        return m_database.insert(InfinityDatabase.TABLE_SECTORIAL_UNITS, null, v);
    }


    private long writeSectorial(Sectorial sectorial) {

        ContentValues v = new ContentValues();
        //v.put(InfinityDatabase.COLUMN_AMMO, w.ammo);

        v.put(InfinityDatabase.COLUMN_ARMY, sectorial.army);
        Log.d(TAG, "ARMY: " + sectorial.army);
        v.put(InfinityDatabase.COLUMN_NAME, sectorial.name);
        v.put(InfinityDatabase.COLUMN_ABBREVIATION, sectorial.abbr);

        return m_database.insert(InfinityDatabase.TABLE_SECTORIAL, null, v);

    }

}
