package com.cgordon.infinityandroid.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cgordon on 6/1/2015.
 */
public class UnitsData {

    private final static String TAG = UnitsData.class.getSimpleName();

    private SQLiteDatabase m_database;
    private InfinityDatabase m_dbHelper;
    private final String[] unitColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_AVA,
            InfinityDatabase.COLUMN_SHARED_AVA, 
            InfinityDatabase.COLUMN_FACTION,
            InfinityDatabase.COLUMN_NOTE, 
            InfinityDatabase.COLUMN_NAME, 
            InfinityDatabase.COLUMN_ISC, 
            InfinityDatabase.COLUMN_IMAGE 
    };

    private final String[] optionsColumns = {
        InfinityDatabase.COLUMN_ID,
        InfinityDatabase.COLUMN_UNIT_ID,
        InfinityDatabase.COLUMN_NAME,
        InfinityDatabase.COLUMN_CODE,
        InfinityDatabase.COLUMN_NOTE ,
        InfinityDatabase.COLUMN_CODENAME ,
        InfinityDatabase.COLUMN_COST,
        InfinityDatabase.COLUMN_SWC,
        InfinityDatabase.COLUMN_BSW ,
        InfinityDatabase.COLUMN_CCW ,
        InfinityDatabase.COLUMN_SPEC,
        InfinityDatabase.COLUMN_PROFILE
    };

    private final String[] profileColumns = {
        InfinityDatabase.COLUMN_ID ,
        InfinityDatabase.COLUMN_UNIT_ID ,
        InfinityDatabase.COLUMN_MOV,
        InfinityDatabase.COLUMN_CC,
        InfinityDatabase.COLUMN_BS ,
        InfinityDatabase.COLUMN_PH ,
        InfinityDatabase.COLUMN_WIP ,
        InfinityDatabase.COLUMN_ARM ,
        InfinityDatabase.COLUMN_BTS ,
        InfinityDatabase.COLUMN_WOUNDS ,
        InfinityDatabase.COLUMN_WOUNDS_TYPE ,
        InfinityDatabase.COLUMN_SILHOUETTE ,
        InfinityDatabase.COLUMN_IRR ,
        InfinityDatabase.COLUMN_IMP ,
        InfinityDatabase.COLUMN_CUBE ,
        InfinityDatabase.COLUMN_NOTE,
        InfinityDatabase.COLUMN_ISC,
        InfinityDatabase.COLUMN_NAME,
        InfinityDatabase.COLUMN_TYPE ,
        InfinityDatabase.COLUMN_HACKABLE ,
        InfinityDatabase.COLUMN_BSW ,
        InfinityDatabase.COLUMN_CCW,
        InfinityDatabase.COLUMN_SPEC ,
        InfinityDatabase.COLUMN_OPTION_SPECIFIC ,
        InfinityDatabase.COLUMN_ALL_DIE,
        InfinityDatabase.COLUMN_AVA
    };

    public static final String ShowMercenariesListKey = "show_mercs";

    private SharedPreferences m_prefs;

    public UnitsData(Context context) {
        m_dbHelper = new InfinityDatabase(context);
        m_prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    // Necessary for creating the database during onCreate.  Otherwise the database is locked.
    public UnitsData(SQLiteDatabase db) {
        m_database = db;
    }

    public void open() {
        m_database = m_dbHelper.getWritableDatabase();
    }

    public void close() {
        m_database.close();
    }

    public void writeUnits(ArrayList<Unit> units) {

        Iterator it = units.iterator();

        while (it.hasNext()) {
            Unit unit = (Unit)it.next();

            long unitId = writeUnit(unit) ;

            if (unitId == -1) {
                Log.d(TAG, "Failed insert");
                return;
            }

            List<Profile> profiles = unit.profiles;
            Iterator profileIt = profiles.iterator();
            while (profileIt.hasNext()) {
                Profile profile = (Profile) profileIt.next();
            
                long profileId = writeProfile(profile, unitId);
                if (profileId == -1) {
                    Log.d(TAG, "Failed to write profile");
                    return;
                }
            
            }

            List<Option> options = unit.options;
            Iterator optionIt = options.iterator();
            while (optionIt.hasNext()) {
                Option option = (Option) optionIt.next();
                
                long optionId = writeOption(option, unitId);
                if (optionId == -1) {
                    Log.d(TAG, "failed to write option");
                    return;
                }
            }
            
        }

    }

    private List<Unit> getArmyUnits(Cursor cursor) {

        cursor.moveToFirst();

        ArrayList<Unit> units = new ArrayList<Unit>();

        while (!cursor.isAfterLast()) {

            Unit unit = cursorToUnit(cursor);

            units.add(unit);

            cursor.moveToNext();
        }

        return units;

    }




    private void printCursor(Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] columns = cursor.getColumnNames();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < columns.length; i++) {
                sb.append(columns[i] + ": " + cursor.getString(i) + " ");
            }
            Log.d(TAG, "ISC: " + sb.toString());

            cursor.moveToNext();

        }

    }

    public List<Unit> getUnits(Army army) {
        if (army == null) {
            return null;
        }

        // there are two cases for an army.  Either it's the base force, where they need all the
        // units or it's a sectorial where they need a specific subset

        // full faction
        if (army.name.equals(army.faction)) {
            Cursor cursor = m_database.query(InfinityDatabase.TABLE_UNITS, unitColumns,
                    InfinityDatabase.COLUMN_FACTION + "='" + army.name + "'", null, null, null, InfinityDatabase.COLUMN_ISC, null);

            List<Unit> units = getArmyUnits(cursor);

            boolean showMercs = false;
            if (m_prefs.contains(ShowMercenariesListKey)) {
                showMercs = m_prefs.getBoolean(ShowMercenariesListKey, false);
            }

            if (showMercs) {

                // go get the list of mercenaries for the main factions.  Not allowed for aliens (and
                // mercenaries)
                if ((!army.name.equals("Combined Army"))
                        && (!army.name.equals("Tohaa"))
                        && (!army.name.equals("Mercenary"))) {

                    cursor = m_database.query(InfinityDatabase.TABLE_UNITS, unitColumns,
                            InfinityDatabase.COLUMN_FACTION + "='Mercenary'", null, null, null, InfinityDatabase.COLUMN_ISC, null);


                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        Unit unit = cursorToUnit(cursor);

                        // don't re-add units in the case where there is both a faction and merc
                        // version of the same unit.
                        if (!units.contains(unit)) {
                            units.add(unit);
                        }

                        cursor.moveToNext();
                    }


                }
            }

            return units;
        } else {

            Cursor cursor = m_database.rawQuery("SELECT " + InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_ISC + " FROM " + InfinityDatabase.TABLE_ARMY_UNITS
                    + " where " + InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_ARMY_ID + "=" + army.dbId
                    , null);
            Log.e(TAG, "army units isc count: "+cursor.getCount());
            printCursor(cursor);

            cursor = m_database.rawQuery("SELECT " +

                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ID + ", " +
                            InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_AVA+ ", " +
                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_SHARED_AVA + ", " +
                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_FACTION+ ", " +
                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_NOTE+ ", " +
                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_NAME+ ", " +
                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ISC + ", " +
                            InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_IMAGE + ", " +
                            InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_LINKABLE

                            + " FROM " + InfinityDatabase.TABLE_ARMY_UNITS
                            + " INNER JOIN " + InfinityDatabase.TABLE_UNITS
                            + " ON " + InfinityDatabase.TABLE_ARMY_UNITS+"."+InfinityDatabase.COLUMN_ISC + " like " + InfinityDatabase.TABLE_UNITS+"."+InfinityDatabase.COLUMN_ISC
                            + " where " + InfinityDatabase.TABLE_ARMY_UNITS+"."+InfinityDatabase.COLUMN_ARMY_ID + "=" + army.dbId
                            + " order by " + InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ISC
                    ,
                    null
            );

            Log.e(TAG, "join units: "+cursor.getCount());
            printCursor(cursor);
            return getArmyUnits(cursor);
        }

    }

    private List<Option> getOptions(long unitId) {
        Cursor cursor = m_database.query(InfinityDatabase.TABLE_OPTIONS, optionsColumns, InfinityDatabase.COLUMN_UNIT_ID + "=" + unitId, null, null, null, null, null);

        cursor.moveToFirst();

        ArrayList<Option> options = new ArrayList<Option>();

        while (!cursor.isAfterLast()) {
            Option option = cursorToOption(cursor);

            options.add(option);

            cursor.moveToNext();
        }

        return options;    
    }

    private Option cursorToOption(Cursor cursor) {
        Option option = new Option();

        // 0 - COLUMN_ID + " integer primary key, " +
        // 1 - COLUMN_UNIT_ID + " integer, " +

        option.name = cursor.getString(2);
        option.code = cursor.getString(3);
        option.note = cursor.getString(4);
        option.codename = cursor.getString(5);
        option.cost = cursor.getInt(6);
        option.swc = cursor.getDouble(7);

        String temp = cursor.getString(8);
        if (!temp.isEmpty()) {
            option.bsw = Arrays.asList(temp.split(","));
        }

        temp = cursor.getString(9);
        if (!temp.isEmpty()) {
            option.ccw = Arrays.asList(temp.split(","));
        }

        temp = cursor.getString(10);
        if (!temp.isEmpty()) {
            option.spec = Arrays.asList(temp.split(","));
        }

        option.profile = cursor.getInt(11);

        return option;

    }

    private List<Profile> getProfiles(long unitId) {
        Cursor cursor = m_database.query(InfinityDatabase.TABLE_PROFILES, profileColumns, InfinityDatabase.COLUMN_UNIT_ID + "=" + unitId, null, null, null, null, null);

        cursor.moveToFirst();

        ArrayList<Profile> profiles = new ArrayList<Profile>();

        while (!cursor.isAfterLast()) {
            Profile profile = cursorToProfile(cursor);

            profiles.add(profile);

            cursor.moveToNext();
        }

        return profiles;

    }

    private Profile cursorToProfile(Cursor cursor) {
        Profile profile = new Profile();

        // 0 - COLUMN_ID + " integer primary key, " +
        // 1 - COLUMN_UNIT_ID + " integer, " +

        profile.mov = cursor.getString(2);
        profile.cc = cursor.getString(3);
        profile.bs = cursor.getString(4);
        profile.ph = cursor.getString(5);
        profile.wip = cursor.getString(6);
        profile.arm = cursor.getString(7);
        profile.bts = cursor.getString(8);
        profile.wounds = cursor.getString(9);
        profile.woundType = cursor.getString(10);
        profile.silhouette = cursor.getString(11);
        profile.irr = (cursor.getInt(12) != 0); // boolean
        profile.imp = cursor.getString(13);
        profile.cube = cursor.getString(14);
        profile.note = cursor.getString(15);
        profile.isc = cursor.getString(16);
        profile.name = cursor.getString(17);
        profile.type = cursor.getString(18);
        profile.hackable = (cursor.getInt(19) != 0);

        String temp = cursor.getString(20);
        if (!temp.isEmpty()) {
            profile.bsw = Arrays.asList(temp.split(","));
        }

        temp = cursor.getString(21);
        if (!temp.isEmpty()) {
            profile.ccw = Arrays.asList(temp.split(","));
        }

        temp = cursor.getString(22);
        if (!temp.isEmpty()) {
            profile.spec = Arrays.asList(temp.split(","));
        }

        profile.optionSpecific = cursor.getString(23);
        profile.allProfilesMustDie = cursor.getString(24);
        profile.ava = cursor.getString(25);

        return profile;
    }

    private Unit cursorToUnit(Cursor cursor) {
        Unit unit = new Unit();

        unit.dbId = cursor.getLong(0);
        unit.ava = cursor.getString(1);
        unit.sharedAva = cursor.getString(2);
        unit.faction = cursor.getString(3);
        unit.note = cursor.getString(4);
        unit.name = cursor.getString(5);
        unit.isc = cursor.getString(6);
        unit.image = cursor.getString(7);
        if (cursor.getColumnNames().length > 8) {
            unit.linkable = (cursor.getInt(8) != 0);
        }

        // read profiles
        List<Profile> profiles = getProfiles(unit.dbId);
        unit.profiles = profiles;

        // read options
        List<Option> options = getOptions(unit.dbId);
        unit.options = options;

        return unit;
    }

    private long writeOption(Option option, long unitId) {

        ContentValues v = new ContentValues();
        
        v.put(InfinityDatabase.COLUMN_UNIT_ID, unitId);
        v.put(InfinityDatabase.COLUMN_NAME, option.name);
        v.put(InfinityDatabase.COLUMN_CODE, option.code);
        v.put(InfinityDatabase.COLUMN_NOTE , option.note);
        v.put(InfinityDatabase.COLUMN_CODENAME , option.codename);
        v.put(InfinityDatabase.COLUMN_COST, option.cost);
        v.put(InfinityDatabase.COLUMN_SWC, option.swc);
        v.put(InfinityDatabase.COLUMN_BSW , TextUtils.join(",", option.bsw));
        v.put(InfinityDatabase.COLUMN_CCW , TextUtils.join(",", option.ccw));
        v.put(InfinityDatabase.COLUMN_SPEC, TextUtils.join(",", option.spec));
        v.put(InfinityDatabase.COLUMN_PROFILE, option.profile);

        return m_database.insert(InfinityDatabase.TABLE_OPTIONS, null, v);
    }

    private long writeProfile(Profile profile, long unitId) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_UNIT_ID , unitId);
        v.put(InfinityDatabase.COLUMN_MOV, profile.mov);
        v.put(InfinityDatabase.COLUMN_CC, profile.cc);
        v.put(InfinityDatabase.COLUMN_BS , profile.bs);
        v.put(InfinityDatabase.COLUMN_PH , profile.ph);
        v.put(InfinityDatabase.COLUMN_WIP , profile.wip);
        v.put(InfinityDatabase.COLUMN_ARM , profile.arm);
        v.put(InfinityDatabase.COLUMN_BTS , profile.bts);
        v.put(InfinityDatabase.COLUMN_WOUNDS , profile.wounds);
        v.put(InfinityDatabase.COLUMN_WOUNDS_TYPE , profile.woundType);
        v.put(InfinityDatabase.COLUMN_SILHOUETTE , profile.silhouette);
        v.put(InfinityDatabase.COLUMN_IRR , profile.irr);
        v.put(InfinityDatabase.COLUMN_IMP , profile.imp);
        v.put(InfinityDatabase.COLUMN_CUBE , profile.cube);
        v.put(InfinityDatabase.COLUMN_NOTE, profile.note);
        v.put(InfinityDatabase.COLUMN_ISC, profile.isc);
        v.put(InfinityDatabase.COLUMN_NAME, profile.name);
        v.put(InfinityDatabase.COLUMN_TYPE , profile.type);
        v.put(InfinityDatabase.COLUMN_HACKABLE , profile.hackable);
        v.put(InfinityDatabase.COLUMN_BSW , TextUtils.join(",", profile.bsw));
        v.put(InfinityDatabase.COLUMN_CCW, TextUtils.join(",", profile.ccw));
        v.put(InfinityDatabase.COLUMN_SPEC , TextUtils.join(",", profile.spec));
        v.put(InfinityDatabase.COLUMN_OPTION_SPECIFIC, profile.optionSpecific);
        v.put(InfinityDatabase.COLUMN_ALL_DIE, profile.allProfilesMustDie);
        v.put(InfinityDatabase.COLUMN_AVA, profile.ava);

        return m_database.insert(InfinityDatabase.TABLE_PROFILES, null, v);

    }

    private long writeUnit(Unit unit) {

        ContentValues v = new ContentValues();
        //v.put(InfinityDatabase.COLUMN_AMMO, w.ammo);

        v.put(InfinityDatabase.COLUMN_AVA, unit.ava);
        v.put(InfinityDatabase.COLUMN_SHARED_AVA, unit.sharedAva);
        v.put(InfinityDatabase.COLUMN_FACTION, unit.faction);
        v.put(InfinityDatabase.COLUMN_NOTE, unit.note);
        v.put(InfinityDatabase.COLUMN_NAME, unit.name);
        v.put(InfinityDatabase.COLUMN_ISC, unit.isc);
        v.put(InfinityDatabase.COLUMN_IMAGE, unit.image);

        return m_database.insert(InfinityDatabase.TABLE_UNITS, null, v);
        
    }

}
