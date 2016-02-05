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

package com.cgordon.infinityandroid.json;

import android.content.Context;
import android.util.JsonReader;

import com.cgordon.infinityandroid.data.Army;
import com.cgordon.infinityandroid.data.ArmyUnit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SectorialParser {

    private final String TAG = SectorialParser.class.getSimpleName();

    private Context m_context;

    public SectorialParser(Context context) {
        m_context = context;
    }

    public ArrayList<Army> parse(int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        ArrayList<Army> sectorials = new ArrayList<Army>();

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

    private Army parseSectorialArmy(JsonReader reader) throws IOException {
        reader.beginObject();

        Army sectorial = new Army();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("army")) {
                sectorial.faction = reader.nextString();
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

    private ArrayList<ArmyUnit> parseSectorialUnits(JsonReader reader) throws IOException {
        reader.beginArray();

        ArrayList<ArmyUnit> units = new ArrayList<ArmyUnit>();

        while (reader.hasNext()) {
            units.add(parseSectorialUnit(reader));
        }

        reader.endArray();

        return units;

    }

    private ArmyUnit parseSectorialUnit(JsonReader reader) throws IOException {
        reader.beginObject();

        ArmyUnit unit = new ArmyUnit();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("ava")) {
                unit.ava = reader.nextString();
            } else if (name.equals("isc")) {
                unit.isc = reader.nextString();
            } else if (name.equals("linkable")) {
                unit.linkable = reader.nextBoolean();
            } else if (name.equals("army")) { // it's from an faction other than the sectorial faction
                unit.army = reader.nextString();
            } else {
                throw new IOException("unknown tag in parseSectorialUnit: " + name);
            }
        }

        reader.endObject();

        return unit;
    }
}
