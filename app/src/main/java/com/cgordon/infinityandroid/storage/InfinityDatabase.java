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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.json.SectorialParser;
import com.cgordon.infinityandroid.json.UnitParser;
import com.cgordon.infinityandroid.json.WeaponParser;

/**
 * Created by cgordon on 6/1/2015.
 */
public class InfinityDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "infinity.db";
    private static final int DATABASE_VERSION = 34;

    public static final String TABLE_WEAPONS = "weapons";
    public static final String TABLE_UNITS = "units";
    public static final String TABLE_CHILDREN = "children";
    public static final String TABLE_PROFILES = "profiles";
    public static final String TABLE_ARMY = "army";
    public static final String TABLE_ARMY_UNITS = "army_units";
    public static final String TABLE_ARMY_UNITS_CHILDREN = "army_units_children";
    public static final String TABLE_ARMY_LISTS = "army_lists";
    public static final String TABLE_ARMY_LIST_UNITS = "army_list_units";

    // ===== UNITS COLUMNS =====
    public static final String COLUMN_AVA = "ava";
    public static final String COLUMN_SHARED_AVA = "sharedAva";
    public static final String COLUMN_FACTION = "faction";
    public static final String COLUMN_UNIT_ID = "unit_id";
    // COLUMN_NOTE
    // COLUMN_NAME
    // COLUMN_ISC
    public static final String COLUMN_IMAGE = "image";

    // ===== PROFILES COLUMNS =====
    // Unit ID
    public static final String COLUMN_MOV = "mov";
    //COLUMN_CC
    public static final String COLUMN_BS = "bs";
    public static final String COLUMN_PH = "ph";
    public static final String COLUMN_WIP = "wip";
    public static final String COLUMN_ARM = "arm";
    public static final String COLUMN_BTS = "bts";
    public static final String COLUMN_WOUNDS = "wounds";
    public static final String COLUMN_WOUNDS_TYPE = "woundType";
    public static final String COLUMN_SILHOUETTE = "silhouette";
    public static final String COLUMN_IRR = "irr";
    public static final String COLUMN_IMP = "imp";
    public static final String COLUMN_CUBE = "cube";
    // COLUMN_Note
    // COLUMN_ISC
    // COLUMN_Name
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_HACKABLE = "hackable";
    // COLUMN_BSW
    // COLUMN_CCW
    // COLUMN_SPEC
    public static final String COLUMN_CHILD_SPECIFIC = "childSpecific";
    public static final String COLUMN_ALL_DIE = "allProfilesMustDie";
    // COLUMN_AVA

    // ===== CHILD COLUMNS =====
    public static final String COLUMN_CHILD_ID = "child_id";
    // COLUMN_Name
    public static final String COLUMN_CODE = "code"; // a short name to identify this child
    // COLUMN_Note
    public static final String COLUMN_CODENAME = "codename"; // not sure where this is used...
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_SWC = "swc";
    // COLUMN_BSW
    // COLUMN_CCW
    // COLUMN_Spec
    public static final String COLUMN_PROFILE = "profile"; // This option requires an additional profile (0-based index)

    public static final String COLUMN_ARMY_UNIT_ID = "army_unit_id";
    public static final String COLUMN_HIDE = "hide";


    // ===== WEAPONS COLUMNS =====
    public static final String COLUMN_AMMO = "ammo";
    public static final String COLUMN_BURST = "burst";
    // COLUMN_CC
    public static final String COLUMN_DAMAGE = "damage";
    public static final String COLUMN_EM_VUL = "em_vul";
    public static final String COLUMN_LONG_DIST = "long_dist";
    public static final String COLUMN_LONG_MOD = "long_mod";
    public static final String COLUMN_MAX_DIST = "max_dist";
    public static final String COLUMN_MAX_MOD = "max_mod";
    public static final String COLUMN_MEDIUM_DIST = "medium_dist";
    public static final String COLUMN_MEDIUM_MOD = "medium_mod";
    // COLUMN_Name
    // COLUMN_Note
    public static final String COLUMN_SHORT_DIST = "short_dist";
    public static final String COLUMN_SHORT_MOD = "short_mod";
    public static final String COLUMN_TEMPLATE = "template";
    public static final String COLUMN_USES = "uses";
    public static final String COLUMN_ATTR = "attr";
    public static final String COLUMN_SUPPRESSIVE = "suppressive";
    public static final String COLUMN_ALT_PROFILE = "alt_profile";
    public static final String COLUMN_MODE = "mode";

    // ===== GENERAL COLUMNS =====
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ISC = "isc";
    public static final String COLUMN_BSW = "bsw";  // this is comma separated list
    public static final String COLUMN_CCW = "ccw"; // this is comma separated list
    public static final String COLUMN_SPEC = "spec"; // this is comma separated list
    public static final String COLUMN_CC = "cc";

    // ===== ARMY Data =====
    // COLUMN_ID
    // COLUMN_FACTION
    // COLUMN_NAME
    public static final String COLUMN_ABBREVIATION = "abbr";

    // ===== ARMY UNITS DATA =====
    // COLUMN_ID
    public static final String COLUMN_ARMY_ID = "army_id";
    // COLUMN_AVA
    // COLUMN_ISC
    public static final String COLUMN_LINKABLE = "linkable";
    // COLUMN_FACTION

    // ===== ARMY LIST DATA =====
    // COLUMN_ID
    // COLUMN_NAME; String name,
    // COLUMN_ARMY_ID; long armyId,
    public static final String COLUMN_POINTS = "points";

    // ===== ARMY LIST UNIT DATA =====
    // COLUMN_ID
    public static final String COLUMN_LIST_ID = "list_id";  // long listId,
    // COLUMN_UNIT_ID; long unitId,
    // COLUMN_PROFILE; int profile)
    public static final String COLUMN_GROUP = "group_id";


    private static final String CREATE_TABLE_UNITS = "create table " + TABLE_UNITS + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_AVA + " text, " +
            COLUMN_SHARED_AVA + " text, " +
            COLUMN_FACTION + " text, " +
            COLUMN_NOTE + " text, " +
            COLUMN_NAME + " text, " +
            COLUMN_ISC + " text, " +
            COLUMN_IMAGE + " text " +
            ");";

    private static final String CREATE_TABLE_CHILDREN = "create table " + TABLE_CHILDREN + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_CHILD_ID + " integer, " +
            COLUMN_NAME + " text, " +
            COLUMN_CODE + " text, " +
            COLUMN_NOTE + " text, " +
            COLUMN_CODENAME + " text, " +
            COLUMN_COST + " integer, " +
            COLUMN_SWC + " real, " +
            COLUMN_BSW + " text, " +
            COLUMN_CCW + " text, " +
            COLUMN_SPEC + " text, " +
            COLUMN_PROFILE + " integer " +
            ");";

    private static final String CREATE_TABLE_PROFILES = "create table " + TABLE_PROFILES + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_UNIT_ID + " integer, " +
            COLUMN_MOV + " text, " +
            COLUMN_CC + " text, " +
            COLUMN_BS + " text, " +
            COLUMN_PH + " text, " +
            COLUMN_WIP + " text, " +
            COLUMN_ARM + " text, " +
            COLUMN_BTS + " text, " +
            COLUMN_WOUNDS + " text, " +
            COLUMN_WOUNDS_TYPE + " text, " +
            COLUMN_SILHOUETTE + " text, " +
            COLUMN_IRR + " integer, " + // boolean
            COLUMN_IMP + " text, " +
            COLUMN_CUBE + " text, " +
            COLUMN_NOTE + " text, " +
            COLUMN_ISC + " text, " +
            COLUMN_NAME + " text, " +
            COLUMN_TYPE + " text, " +
            COLUMN_HACKABLE + " integer, " + // boolean
            COLUMN_BSW + " text, " +
            COLUMN_CCW + " text, " +
            COLUMN_SPEC + " text, " +
            COLUMN_CHILD_SPECIFIC + " text, " +
            COLUMN_ALL_DIE + " text, " +
            COLUMN_AVA + " text " +
            ");";

    private static final String CREATE_TABLE_WEAPONS = "create table " + TABLE_WEAPONS + " (" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_AMMO + " text, " +
            COLUMN_BURST + " text, " +
            COLUMN_CC + " text, " +
            COLUMN_DAMAGE + " text, " +
            COLUMN_EM_VUL + " text, " +
            COLUMN_LONG_DIST + " text, " +
            COLUMN_LONG_MOD + " text, " +
            COLUMN_MAX_DIST + " text, " +
            COLUMN_MAX_MOD + " text, " +
            COLUMN_MEDIUM_DIST + " text, " +
            COLUMN_MEDIUM_MOD + " text, " +
            COLUMN_NAME + " text, " +
            COLUMN_NOTE + " text, " +
            COLUMN_SHORT_DIST + " text, " +
            COLUMN_SHORT_MOD + " text, " +
            COLUMN_TEMPLATE + " text, " +
            COLUMN_USES + " text, " +
            COLUMN_ATTR + " text, " +
            COLUMN_SUPPRESSIVE + " text, " +
            COLUMN_ALT_PROFILE + " text, " +
            COLUMN_MODE + " text " +
            ");";


    private static final String CREATE_TABLE_ARMY = "create table " + TABLE_ARMY + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_FACTION + " text, " +
            COLUMN_NAME + " text, " +
            COLUMN_ABBREVIATION + " text " +
            ");";

    private static final String CREATE_TABLE_ARMY_UNITS = "create table " + TABLE_ARMY_UNITS + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_ARMY_ID + " integer, " +
            COLUMN_AVA + " text, " +
            COLUMN_UNIT_ID + " text, " +
            COLUMN_LINKABLE + " integer, " + // boolean
            COLUMN_FACTION + " text " +
            ");";

    private static final String CREATE_TABLE_ARMY_UNITS_CHILDREN = "create table " + TABLE_ARMY_UNITS_CHILDREN + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_ARMY_UNIT_ID + " integer, " +
            COLUMN_SWC + " text, " +
            COLUMN_HIDE + " text " +
            ");";

    private static final String CREATE_TABLE_ARMY_LISTS = "create table if not exists " + TABLE_ARMY_LISTS + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_NAME  + " text, " +
            COLUMN_ARMY_ID + " integer, " +
            COLUMN_POINTS  + " integer " +
            ");";

    private static final String CREATE_TABLE_ARMY_LIST_UNITS = "create table if not exists " + TABLE_ARMY_LIST_UNITS + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_LIST_ID + " integer, " +
            COLUMN_GROUP + " integer, " +
            COLUMN_UNIT_ID + " integer, " +
            COLUMN_CHILD_ID + " integer " +
            ");";

    private Context m_context;

    private final static String TAG = InfinityDatabase.class.getSimpleName();

    public InfinityDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        m_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEAPONS);
        db.execSQL(CREATE_TABLE_UNITS);
        db.execSQL(CREATE_TABLE_CHILDREN);
        db.execSQL(CREATE_TABLE_PROFILES);
        db.execSQL(CREATE_TABLE_ARMY);
        db.execSQL(CREATE_TABLE_ARMY_UNITS);
        db.execSQL(CREATE_TABLE_ARMY_UNITS_CHILDREN);
        db.execSQL(CREATE_TABLE_ARMY_LISTS);
        db.execSQL(CREATE_TABLE_ARMY_LIST_UNITS);

        // Load units
        UnitParser unitParser = new UnitParser(m_context);
        UnitsData unitsData = new UnitsData(db);
        unitsData.writeUnits(unitParser.parse(R.raw.pano_units));
        unitsData.writeUnits(unitParser.parse(R.raw.yuji_units));
        unitsData.writeUnits(unitParser.parse(R.raw.aria_units));
        unitsData.writeUnits(unitParser.parse(R.raw.noma_units));
        unitsData.writeUnits(unitParser.parse(R.raw.haqq_units));
        unitsData.writeUnits(unitParser.parse(R.raw.comb_units));
        unitsData.writeUnits(unitParser.parse(R.raw.alep_units));
        unitsData.writeUnits(unitParser.parse(R.raw.toha_units));
        unitsData.writeUnits(unitParser.parse(R.raw.merc_units));

        // Load sectorial data
        SectorialParser sectorialParser = new SectorialParser(m_context);
        ArmyData armyData = new ArmyData(db);
        armyData.writeArmy(sectorialParser.parse(R.raw.sectorials));

        // Load weapon data
        WeaponParser weaponParser = new WeaponParser(m_context);
        WeaponsData weaponsData = new WeaponsData(db);
        weaponsData.writeWeapons(weaponParser.parse(R.raw.weapons));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEAPONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMY_UNITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMY_UNITS_CHILDREN);
        if (oldVersion < 34) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMY_LISTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMY_LIST_UNITS);
        }

        onCreate(db);
    }
}
