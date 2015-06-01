package com.cgordon.infinityandroid.json;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.cgordon.infinityandroid.data.Sectorial;
import com.cgordon.infinityandroid.data.SectorialUnit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cgordon on 5/30/2015.
 */
public class SectorialParser {

    private final String TAG = SectorialParser.class.getSimpleName();

    private Context m_context;

    public SectorialParser(Context context) {
        m_context = context;
    }

    public ArrayList<Sectorial> parse (int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        ArrayList<Sectorial> sectorials = new ArrayList<Sectorial>();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                sectorials.add(parseSectorialArmy(reader));
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return sectorials;
    }
    private Sectorial parseSectorialArmy(JsonReader reader) throws IOException {
        reader.beginObject();

        Sectorial sectorial = new Sectorial();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("army")) {
                sectorial.army = reader.nextString();
            } else if (name.equals("name")) {
                sectorial.name = reader.nextString();
            } else if (name.equals("abbr")) {
                sectorial.abbr = reader.nextString();
            } else if (name.equals("units")) {
                sectorial.units.addAll(parseSectorialUnits(reader));
            } else {
                throw new IOException("Unable to parse tag in parseSectorialArmy: " + name);
            }
        }
        reader.endObject();

        return sectorial;
    }

    private ArrayList<SectorialUnit> parseSectorialUnits(JsonReader reader) throws IOException {
        reader.beginArray();

        ArrayList<SectorialUnit> units = new ArrayList<SectorialUnit>();

        while (reader.hasNext()) {
            units.add(parseSectorialUnit(reader));
        }

        reader.endArray();

        return units;

    }

    private SectorialUnit parseSectorialUnit(JsonReader reader) throws IOException {
        reader.beginObject();

        SectorialUnit unit = new SectorialUnit();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("ava")) {
                unit.ava = reader.nextString();
            } else if (name.equals("isc")) {
                unit.isc = reader.nextString();
            } else if (name.equals("linkable")) {
                unit.linkable = reader.nextBoolean();
            } else if (name.equals("army")) { // it's from an army other than the sectorial army
                unit.army = reader.nextString();
            } else {
                throw new IOException("unknown tag in parseSectorialUnit: " + name);
            }
        }

        reader.endObject();

        return unit;
    }
}
