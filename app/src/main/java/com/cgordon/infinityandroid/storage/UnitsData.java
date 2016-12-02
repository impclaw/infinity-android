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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.ArmyUnitChild;
import com.cgordon.infinityandroid.data.Child;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UnitsData {

    private final static String TAG = UnitsData.class.getSimpleName();

    private final static String IMPERSONATION_PLUS = "Impersonation Plus";
    private final static String IMPERSONATION_BASIC = "Basic Impersonation";
    private final static String CH_CAMOUFLAGE = "CH: Camouflage";
    private final static String CH_TO_CAMOUFLAGE = "CH: TO Camouflage";
    private final static String CH_AMBUSH_CAMOUFLAGE = "CH: Ambush Camouflage";
    private final static String REGENERATION = "Regeneration";
    private final static String ENGINEER = "Engineer";
    private final static String AKBAR_DOCTOR = "Akbar Doctor";
    private final static String G_MNEMONICA = "G: Mnemonica";
    private final static String G_REMOTE_PRESENCE = "G: Remote Presence";
    private final static String VETERAN_L2 = "Veteran L2";

    private final static String SURPRISE_ATTACK = "Surprise Attack";
    private final static String SURPRISE_SHOT_L1 = "Surprise Shot L1";
    private final static String STEALTH = "Stealth";
    private final static String SHOCK_IMMUNITY = "Shock Immunity";
    private final static String DEACTIVATOR = "Deactivator";
    private final static String DOCTOR_PLUS = "Doctor Plus";
    private final static String VALOR_COURAGE = "Valor: Courage";

    private final static String SIXTH_SENSE_L2 = "Sixth Sense L2";
    private final static String V_NO_WOUND_INCAP = "V: No Wound Incapacitation";


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

    private final String[] childColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_UNIT_ID,
            InfinityDatabase.COLUMN_CHILD_ID,
            InfinityDatabase.COLUMN_NAME,
            InfinityDatabase.COLUMN_NOTE,
            InfinityDatabase.COLUMN_COST,
            InfinityDatabase.COLUMN_SWC,
            InfinityDatabase.COLUMN_BSW,
            InfinityDatabase.COLUMN_CCW,
            InfinityDatabase.COLUMN_SPEC,
            InfinityDatabase.COLUMN_PROFILE
    };

    private final String[] profileColumns = {
            InfinityDatabase.COLUMN_ID,
            InfinityDatabase.COLUMN_UNIT_ID,
            InfinityDatabase.COLUMN_PROFILE_ID,
            InfinityDatabase.COLUMN_MOV,
            InfinityDatabase.COLUMN_CC,
            InfinityDatabase.COLUMN_BS,
            InfinityDatabase.COLUMN_PH,
            InfinityDatabase.COLUMN_WIP,
            InfinityDatabase.COLUMN_ARM,
            InfinityDatabase.COLUMN_BTS,
            InfinityDatabase.COLUMN_WOUNDS,
            InfinityDatabase.COLUMN_WOUNDS_TYPE,
            InfinityDatabase.COLUMN_SILHOUETTE,
            InfinityDatabase.COLUMN_IRR,
            InfinityDatabase.COLUMN_IMP,
            InfinityDatabase.COLUMN_CUBE,
            InfinityDatabase.COLUMN_NOTE,
            InfinityDatabase.COLUMN_ISC,
            InfinityDatabase.COLUMN_NAME,
            InfinityDatabase.COLUMN_TYPE,
            InfinityDatabase.COLUMN_HACKABLE,
            InfinityDatabase.COLUMN_BSW,
            InfinityDatabase.COLUMN_CCW,
            InfinityDatabase.COLUMN_SPEC,
            InfinityDatabase.COLUMN_OPTION_SPECIFIC,
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

        for (Unit unit : units) {
            long unitId = writeUnit(unit);

            if (unitId == -1) {
                Log.d(TAG, "Failed insert");
                return;
            }

            List<Profile> profiles = unit.profiles;
            for (Profile profile : profiles) {
                long profileId = writeProfile(profile, unitId);
                if (profileId == -1) {
                    Log.d(TAG, "Failed to write profile");
                    return;
                }

            }

            List<Child> children = unit.children;
            for (Child child : children) {
                long childId = writeChild(child, unitId);
                if (childId == -1) {
                    Log.d(TAG, "failed to write child");
                    return;
                }
            }

        }

    }

    private List<Unit> getArmyUnits(Cursor cursor) {

        cursor.moveToFirst();

        ArrayList<Unit> units = new ArrayList<>();

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

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < columns.length; i++) {
                sb.append(columns[i]).append(": ").append(cursor.getString(i)).append(" ");
            }
            Log.d(TAG, "ISC: " + sb.toString());

            cursor.moveToNext();

        }

    }

    public List<Unit> getUnits(Army army) {
        if (army == null) {
            return null;
        }

        Cursor cursor = null;

        // get the data for the army as a whole.  Join the Army List with the Unit Data.

        try {
            cursor = m_database.rawQuery("SELECT " +

                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ID + ", " +
                InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_AVA + ", " +
                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_SHARED_AVA + ", " +
                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_FACTION + ", " +
                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_NOTE + ", " +
                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_NAME + ", " +
                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ISC + ", " +
                InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_IMAGE + ", " +
                InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_LINKABLE

                + " FROM " + InfinityDatabase.TABLE_ARMY_UNITS
                + " INNER JOIN " + InfinityDatabase.TABLE_UNITS
                + " ON " + InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_UNIT_ID + " like " + InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ID
                + " where " + InfinityDatabase.TABLE_ARMY_UNITS + "." + InfinityDatabase.COLUMN_ARMY_ID + "=" + army.dbId
                + " order by " + InfinityDatabase.TABLE_UNITS + "." + InfinityDatabase.COLUMN_ISC
                , null
            );

            Log.e(TAG, "join units: " + cursor.getCount());
            //printCursor(cursor);
            List<Unit> armyUnits = getArmyUnits(cursor);
            cursor.close();

            // load specific army changes to the basic unit data.  ie. child entries that change swc
            // costs or hide entries.

            // get the list of differences for this army
            Map<Integer, Map<Integer, ArmyUnitChild>> children = ArmyData.getArmyUnitChildren(army.dbId, m_database);

            // loop through the list of units for this army
            Iterator armyUnit_it = armyUnits.iterator();
            while (armyUnit_it.hasNext()) {
                Unit armyUnit = (Unit) armyUnit_it.next();

                // if the unit id for this unit is in the map of unit-specific child modifications
                if (children.containsKey((int)armyUnit.id)) {

                    // get the list of child specific modifications
                    Map<Integer, ArmyUnitChild> currentArmyUnitChildren = children.get((int) armyUnit.id);

                    // loop through the list of children for this unit to apply the changes.
                    Iterator children_it = armyUnit.children.iterator();
                    while (children_it.hasNext()) {
                        Child unitChild = (Child) children_it.next();

                        // check the map for the current unit child id and apply the changes.
                        if(currentArmyUnitChildren.containsKey(unitChild.id)) {
                            ArmyUnitChild currentArmyChild = currentArmyUnitChildren.get(unitChild.id);;
                            if (currentArmyChild.swc >=0) {
                                unitChild.swc = currentArmyChild.swc;
                            }
                            if (currentArmyChild.hide) {
                                children_it.remove();
                            }
                        }
                    }

                }
            }





            boolean showMercs = false;
            if (m_prefs.contains(ShowMercenariesListKey)) {
                showMercs = m_prefs.getBoolean(ShowMercenariesListKey, false);
            }

            // mercs are only allowed for vanilla factions.
            if (army.name.equals(army.faction) && showMercs) {

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
                        if (!armyUnits.contains(unit)) {
                            armyUnits.add(unit);
                        }

                        cursor.moveToNext();
                    }
                    cursor.close();
                }

            }

            return armyUnits;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
//
//    public Unit getUnit(long unitId) {
//        Unit retval = null;
//
//        Cursor cursor = m_database.query(InfinityDatabase.TABLE_UNITS, unitColumns,
//                InfinityDatabase.COLUMN_ID + "=" + unitId, null, null, null, null, null);
//
//        if (cursor.getCount() > 0) {
//            cursor.moveToNext();
//            retval = cursorToUnit(cursor);
//        }
//
//        return retval;
//    }

    private ArrayList<Child> getChildren(long unitId) {
        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_CHILDREN, childColumns, InfinityDatabase.COLUMN_UNIT_ID + "=" + unitId, null, null, null, null, null);


            cursor.moveToFirst();

            ArrayList<Child> children = new ArrayList<>();

            while (!cursor.isAfterLast()) {
                Child child = cursorToChild(cursor);

                children.add(child);

                cursor.moveToNext();
            }

            return children;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Child cursorToChild(Cursor cursor) {
        Child child = new Child();

        // 0 - COLUMN_ID + " integer primary key, " +
        // 1 - COLUMN_UNIT_ID + " integer, " +
        child.id = cursor.getInt(2);
        child.name = cursor.getString(3);
        child.note = cursor.getString(4);
        child.cost = cursor.getInt(5);
        child.swc = cursor.getDouble(6);

        String temp = cursor.getString(7);
        if (!temp.isEmpty()) {
            child.bsw = new ArrayList<>(Arrays.asList(temp.split(",")));
        }

        temp = cursor.getString(8);
        if (!temp.isEmpty()) {
            child.ccw = new ArrayList<>(Arrays.asList(temp.split(",")));
        }

        temp = cursor.getString(9);
        if (!temp.isEmpty()) {
            child.spec = new ArrayList<>(Arrays.asList(temp.split(",")));
        }

        expandSpec(child.spec);

        child.profile = cursor.getInt(10);

        return child;

    }

    private ArrayList<Profile> getProfiles(long unitId) {
        Cursor cursor = null;
        try {
            cursor = m_database.query(InfinityDatabase.TABLE_PROFILES, profileColumns, InfinityDatabase.COLUMN_UNIT_ID + "=" + unitId, null, null, null, null, null);

            cursor.moveToFirst();

            ArrayList<Profile> profiles = new ArrayList<>();

            while (!cursor.isAfterLast()) {
                Profile profile = cursorToProfile(cursor);

                profiles.add(profile);

                cursor.moveToNext();
            }

            return profiles;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Profile cursorToProfile(Cursor cursor) {
        Profile profile = new Profile();

        // 0 - COLUMN_ID + " integer primary key, " +
        // 1 - COLUMN_UNIT_ID + " integer, " +

        profile.id = cursor.getInt(2);
        profile.mov = cursor.getString(3);
        profile.cc = cursor.getString(4);
        profile.bs = cursor.getString(5);
        profile.ph = cursor.getString(6);
        profile.wip = cursor.getString(7);
        profile.arm = cursor.getString(8);
        profile.bts = cursor.getString(9);
        profile.wounds = cursor.getString(10);
        profile.woundType = cursor.getString(11);
        profile.silhouette = cursor.getString(12);
        profile.irr = (cursor.getInt(13) != 0); // boolean
        profile.imp = cursor.getString(14);
        profile.cube = cursor.getString(15);
        profile.note = cursor.getString(16);
        profile.isc = cursor.getString(17);
        profile.name = cursor.getString(18);
        profile.type = cursor.getString(19);
        profile.hackable = (cursor.getInt(20) != 0);

        String temp = cursor.getString(21);
        if (!temp.isEmpty()) {
            profile.bsw = new ArrayList<>(Arrays.asList(temp.split(",")));
        }

        temp = cursor.getString(22);
        if (!temp.isEmpty()) {
            profile.ccw = new ArrayList<>(Arrays.asList(temp.split(",")));
        }

        temp = cursor.getString(23);
        if (!temp.isEmpty()) {
            profile.spec = new ArrayList<>(Arrays.asList(temp.split(",")));
        }

        expandSpec(profile.spec);

        profile.optionSpecific = cursor.getString(24);
        profile.allProfilesMustDie = cursor.getString(25);
        profile.ava = cursor.getString(26);

        return profile;
    }

    private void expandSpec(ArrayList<String> spec) {
//        private final static String IMPERSONATION_PLUS = "Impersonation Plus";
//        private final static String IMPERSONATION_BASIC = "Basic Impersonation";
//        private final static String CH_CAMOFLAGE = "CH: Camoflage";
//        private final static String CH_TO_CAMOFLAGE = "CH: TO Camoflage";
//        private final static String CH_AMBUSH_CAMOFLAGE = "CH: Ambush Camoflage";
//        private final static String REGENERATION = "Regeneration";

        if (spec.contains(IMPERSONATION_BASIC)
                || spec.contains(IMPERSONATION_PLUS)
                || spec.contains(CH_AMBUSH_CAMOUFLAGE)
                || spec.contains(CH_CAMOUFLAGE)
                || spec.contains(CH_TO_CAMOUFLAGE)) {
            spec.add(SURPRISE_ATTACK);
            spec.add(SURPRISE_SHOT_L1);
            spec.add(STEALTH);
        }

        if (spec.contains(REGENERATION)) {
            spec.add(SHOCK_IMMUNITY);
        }

        if (spec.contains(ENGINEER)) {
            spec.add(DEACTIVATOR);
        }

        if (spec.contains(G_MNEMONICA) || spec.contains(G_REMOTE_PRESENCE)) {
            spec.add(VALOR_COURAGE);
        }

        if (spec.contains(AKBAR_DOCTOR)) {
            spec.add(DOCTOR_PLUS);
            Log.d(TAG, "spec: " + spec.toString());
        }

        if (spec.contains(VETERAN_L2)) {
            spec.add(SIXTH_SENSE_L2);
            spec.add(V_NO_WOUND_INCAP);
        }


    }

    private Unit cursorToUnit(Cursor cursor) {
        Unit unit = new Unit();

        unit.id = cursor.getLong(0);
        unit.ava = cursor.getString(1);
        unit.sharedAva = cursor.getString(2);
        unit.faction = cursor.getString(3);
        unit.note = cursor.getString(4);
        unit.name = cursor.getString(5);
        unit.isc = cursor.getString(6);
        unit.image = cursor.getInt(7);
        if (cursor.getColumnNames().length > 8) {
            unit.linkable = (cursor.getInt(8) != 0);
        }

        // read profiles
        unit.profiles = getProfiles(unit.id);

        // read children
        unit.children = getChildren(unit.id);

        return unit;
    }

    private long writeChild(Child child, long unitId) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_UNIT_ID, unitId);
        v.put(InfinityDatabase.COLUMN_CHILD_ID, child.id);
        v.put(InfinityDatabase.COLUMN_NAME, child.name);
        v.put(InfinityDatabase.COLUMN_NOTE, child.note);
        v.put(InfinityDatabase.COLUMN_COST, child.cost);
        v.put(InfinityDatabase.COLUMN_SWC, child.swc);
        v.put(InfinityDatabase.COLUMN_BSW, TextUtils.join(",", child.bsw));
        v.put(InfinityDatabase.COLUMN_CCW, TextUtils.join(",", child.ccw));
        v.put(InfinityDatabase.COLUMN_SPEC, TextUtils.join(",", child.spec));
        v.put(InfinityDatabase.COLUMN_PROFILE, child.profile);

        return m_database.insert(InfinityDatabase.TABLE_CHILDREN, null, v);
    }

    private long writeProfile(Profile profile, long unitId) {

        ContentValues v = new ContentValues();

        v.put(InfinityDatabase.COLUMN_UNIT_ID, unitId);
        v.put(InfinityDatabase.COLUMN_PROFILE_ID, profile.id);
        v.put(InfinityDatabase.COLUMN_MOV, profile.mov);
        v.put(InfinityDatabase.COLUMN_CC, profile.cc);
        v.put(InfinityDatabase.COLUMN_BS, profile.bs);
        v.put(InfinityDatabase.COLUMN_PH, profile.ph);
        v.put(InfinityDatabase.COLUMN_WIP, profile.wip);
        v.put(InfinityDatabase.COLUMN_ARM, profile.arm);
        v.put(InfinityDatabase.COLUMN_BTS, profile.bts);
        v.put(InfinityDatabase.COLUMN_WOUNDS, profile.wounds);
        v.put(InfinityDatabase.COLUMN_WOUNDS_TYPE, profile.woundType);
        v.put(InfinityDatabase.COLUMN_SILHOUETTE, profile.silhouette);
        v.put(InfinityDatabase.COLUMN_IRR, profile.irr);
        v.put(InfinityDatabase.COLUMN_IMP, profile.imp);
        v.put(InfinityDatabase.COLUMN_CUBE, profile.cube);
        v.put(InfinityDatabase.COLUMN_NOTE, profile.note);
        v.put(InfinityDatabase.COLUMN_ISC, profile.isc);
        v.put(InfinityDatabase.COLUMN_NAME, profile.name);
        v.put(InfinityDatabase.COLUMN_TYPE, profile.type);
        v.put(InfinityDatabase.COLUMN_HACKABLE, profile.hackable);
        v.put(InfinityDatabase.COLUMN_BSW, TextUtils.join(",", profile.bsw));
        v.put(InfinityDatabase.COLUMN_CCW, TextUtils.join(",", profile.ccw));
        v.put(InfinityDatabase.COLUMN_SPEC, TextUtils.join(",", profile.spec));
        v.put(InfinityDatabase.COLUMN_OPTION_SPECIFIC, profile.optionSpecific);
        v.put(InfinityDatabase.COLUMN_ALL_DIE, profile.allProfilesMustDie);
        v.put(InfinityDatabase.COLUMN_AVA, profile.ava);

        return m_database.insert(InfinityDatabase.TABLE_PROFILES, null, v);

    }

    private long writeUnit(Unit unit) {

        ContentValues v = new ContentValues();
        //v.put(InfinityDatabase.COLUMN_AMMO, w.ammo);

        v.put(InfinityDatabase.COLUMN_ID, unit.id);
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
