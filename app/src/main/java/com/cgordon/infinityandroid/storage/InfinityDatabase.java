package com.cgordon.infinityandroid.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cgordon.infinityandroid.R;
import com.cgordon.infinityandroid.data.Sectorial;
import com.cgordon.infinityandroid.data.Unit;
import com.cgordon.infinityandroid.data.Weapon;
import com.cgordon.infinityandroid.json.SectorialParser;
import com.cgordon.infinityandroid.json.UnitParser;
import com.cgordon.infinityandroid.json.WeaponParser;

import java.util.ArrayList;

/**
 * Created by cgordon on 6/1/2015.
 */
public class InfinityDatabase extends SQLiteOpenHelper {

     private static final String DATABASE_NAME = "infinity.db";
     private static final int DATABASE_VERSION = 9;

    public static final String TABLE_WEAPONS = "weapons";
    public static final String TABLE_UNITS = "units";
    public static final String TABLE_OPTIONS = "options";
    public static final String TABLE_PROFILES = "profiles";
    public static final String TABLE_SECTORIAL = "sectorial";
    public static final String TABLE_SECTORIAL_UNITS = "sectorial_units";

    // ===== UNITS COLUMNS =====
    public static final String COLUMN_AVA = "ava";
    public static final String COLUMN_SHARED_AVA = "sharedAva";
    public static final String COLUMN_ARMY = "army";
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
    public static final String COLUMN_OPTION_SPECIFIC = "optionSpecific";
    public static final String COLUMN_ALL_DIE = "allProfilesMustDie";

    // ===== OPTIONS COLUMNS =====
    // COLUMN_Unit ID
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
    public static final String COLUMN_UNIT_ID = "unit_id";
    public static final String COLUMN_CC = "cc";

    // ===== Sectorial Data =====
    // COLUMN_ID
    // COLUMN_ARMY
    // COLUMN_NAME
    public static final String COLUMN_ABBREVIATION = "abbr";

    // ===== SECTORIAL UNITS DATA =====
    // COLUMN_ID
    public static final String COLUMN_SECTORIAL_ID = "sectorial_id";
    // COLUMN_AVA
    // COLUMN_ISC
    public static final String COLUMN_LINKABLE = "linkable";
    // COLUMN_ARMY


    private static final String CREATE_TABLE_UNITS = "create table " + TABLE_UNITS + " ( " +
        COLUMN_ID + " integer primary key, " +
        COLUMN_AVA  + " text, " +
        COLUMN_SHARED_AVA + " text, " +
        COLUMN_ARMY + " text, " +
        COLUMN_NOTE + " text, " +
        COLUMN_NAME + " text, " +
        COLUMN_ISC + " text, " +
        COLUMN_IMAGE + " text " +
        ");";

    private static final String CREATE_TABLE_OPTIONS = "create table " + TABLE_OPTIONS + " ( " +
        COLUMN_ID + " integer primary key, " +
        COLUMN_UNIT_ID + " integer, " +
        COLUMN_NAME + " text, " +
        COLUMN_CODE + " text, " +
        COLUMN_NOTE + " text, " +
        COLUMN_CODENAME + " text, " +
        COLUMN_COST  + " integer, " +
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
        COLUMN_BS  + " text, " +
        COLUMN_PH  + " text, " +
        COLUMN_WIP  + " text, " +
        COLUMN_ARM  + " text, " +
        COLUMN_BTS  + " text, " +
        COLUMN_WOUNDS  + " text, " +
        COLUMN_WOUNDS_TYPE  + " text, " +
        COLUMN_SILHOUETTE  + " text, " +
        COLUMN_IRR  + " integer, " + // boolean
        COLUMN_IMP  + " text, " +
        COLUMN_CUBE  + " text, " +
        COLUMN_NOTE + " text, " +
        COLUMN_ISC + " text, " +
        COLUMN_NAME + " text, " +
        COLUMN_TYPE  + " text, " +
        COLUMN_HACKABLE  + " integer, " + // boolean
        COLUMN_BSW  + " text, " +
        COLUMN_CCW + " text, " +
        COLUMN_SPEC  + " text, " +
        COLUMN_OPTION_SPECIFIC  + " text, " +
        COLUMN_ALL_DIE   + " text " +
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


    private static final String CREATE_TABLE_SECTORIAL = "create table " + TABLE_SECTORIAL + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_ARMY + " text, " +
            COLUMN_NAME + " text, " +
            COLUMN_ABBREVIATION + " text " +
            ");";

    private static final String CREATE_TABLE_SECTORIAL_UNITS = "create table " + TABLE_SECTORIAL_UNITS + " ( " +
            COLUMN_ID + " integer primary key, " +
            COLUMN_SECTORIAL_ID + " integer, " +
            COLUMN_AVA + " text, " +
            COLUMN_ISC + " text, " +
            COLUMN_LINKABLE + " integer, " + // boolean
            COLUMN_ARMY + " text " +
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
        db.execSQL(CREATE_TABLE_OPTIONS);
        db.execSQL(CREATE_TABLE_PROFILES);
        db.execSQL(CREATE_TABLE_SECTORIAL);
        db.execSQL(CREATE_TABLE_SECTORIAL_UNITS);

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
        unitsData.writeUnits(unitParser.parse(R.raw.other_units));

        // Load sectorial data
        SectorialParser sectorialParser = new SectorialParser(m_context);
        SectorialData sectorialData = new SectorialData(db);
        sectorialData.writeSectorial(sectorialParser.parse(R.raw.sectorials));

        // Load weapon data
        WeaponParser weaponParser = new WeaponParser(m_context);
        WeaponsData weaponsData = new WeaponsData(db);
        weaponsData.writeWeapons(weaponParser.parse(R.raw.weapons));
     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEAPONS);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTORIAL);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTORIAL_UNITS);

         onCreate(db);
     }
}
