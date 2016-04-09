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
import com.cgordon.infinityandroid.data.ArmyUnitChild;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ArmyParser {

    private final String TAG = ArmyParser.class.getSimpleName();

    private Context m_context;

    public ArmyParser(Context context) {
        m_context = context;
    }

    public ArrayList<Army> parse(int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        ArrayList<Army> armies = new ArrayList<Army>();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                armies.add(parseArmy(reader));
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return armies;
    }

    private Army parseArmy(JsonReader reader) throws IOException {
        reader.beginObject();

        Army army = new Army();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("army")) {
                army.faction = reader.nextString();
            } else if (name.equals("name")) {
                army.name = reader.nextString();
            } else if (name.equals("abbr")) {
                army.abbr = reader.nextString();
            } else if (name.equals("units")) {
                army.units.addAll(parseArmyUnits(reader));
            } else {
                throw new IOException("Unable to parse tag in parseArmy: " + name);
            }
        }
        reader.endObject();

        return army;
    }

    private ArrayList<ArmyUnit> parseArmyUnits(JsonReader reader) throws IOException {
        reader.beginArray();

        ArrayList<ArmyUnit> units = new ArrayList<ArmyUnit>();

        while (reader.hasNext()) {
            units.add(parseArmyUnit(reader));
        }

        reader.endArray();

        return units;

    }

    private ArmyUnit parseArmyUnit(JsonReader reader) throws IOException {
        reader.beginObject();

        ArmyUnit unit = new ArmyUnit();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("ava")) {
                unit.ava = reader.nextString();
            } else if (name.equals("id")) {
                unit.id = reader.nextInt();
            } else if (name.equals("linkable")) {
                unit.linkable = reader.nextBoolean();
            } else if (name.equals("comment")) {
                reader.nextString();
            } else if (name.equals("childs")) {
                unit.children.addAll(parseArmyUnitChildren(reader));
            } else {
                throw new IOException("unknown tag in parseArmyUnit: " + name);
            }
        }

        reader.endObject();

        return unit;
    }

    private ArrayList<ArmyUnitChild> parseArmyUnitChildren(JsonReader reader) throws IOException {
        reader.beginArray();

        ArrayList<ArmyUnitChild> unitChildren = new ArrayList<ArmyUnitChild>();

        while (reader.hasNext()) {
            unitChildren.add(parseArmyUnitChild(reader));
        }

        reader.endArray();

        return unitChildren;

    }

    private ArmyUnitChild parseArmyUnitChild(JsonReader reader) throws IOException {
        reader.beginObject();

        ArmyUnitChild unitChild = new ArmyUnitChild();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("id")) {
                unitChild.id = reader.nextInt();
            } else if (name.equals("swc")) {
                unitChild.swc = reader.nextDouble();
            } else if (name.equals("hide")) {
                unitChild.hide = reader.nextBoolean();
            } else if (name.equals("comment")) {
                reader.nextString();
            } else {
                throw new IOException("unknown tag in parseArmyUnit: " + name);
            }
        }

        reader.endObject();

        return unitChild;
    }
}
