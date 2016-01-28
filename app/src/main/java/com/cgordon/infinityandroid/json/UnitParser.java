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

package com.cgordon.infinityandroid.json;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.cgordon.infinityandroid.data.Option;
import com.cgordon.infinityandroid.data.Profile;
import com.cgordon.infinityandroid.data.Unit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UnitParser {

    private final String TAG = UnitParser.class.getSimpleName();

    Context m_context;

    public UnitParser(Context context) {
        m_context = context;
    }


    public ArrayList<Unit> parse(int resourceId) {
        ArrayList<Unit> units = new ArrayList<Unit>();

        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                units.add(parseUnit(reader));
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return units;
    }

    private ArrayList<Option> parseChilds(JsonReader reader) throws IOException {
        reader.beginArray();

        ArrayList<Option> options = new ArrayList<Option>();

        while (reader.hasNext()) {
            options.add(parseChild(reader));
        }

        reader.endArray();

        return options;
    }

    private Unit parseUnit(JsonReader reader) throws IOException {
        Unit unit = new Unit();
        Profile profile = new Profile();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("childs")) {
                unit.options.addAll(parseChilds(reader));
            } else if (name.equals("bsw")) {
                profile.bsw.addAll(parseSubArray(reader));
            } else if (name.equals("ccw")) {
                profile.ccw.addAll(parseSubArray(reader));
            } else if (name.equals("ava")) {
                unit.ava = reader.nextString();
                profile.ava = unit.ava;
            } else if (name.equals("irr")) {
                profile.irr = reader.nextString().equals("X");
            } else if (name.equals("arm")) {
                profile.arm = reader.nextString();
            } else if (name.equals("cbcode")) {
                reader.skipValue();
            } else if (name.equals("bts")) {
                profile.bts = reader.nextString();
            } else if (name.equals("army")) {
                unit.faction = reader.nextString();
            } else if (name.equals("note")) {
                unit.note = reader.nextString();
                profile.note = unit.note;
            } else if (name.equals("cube")) {
                profile.cube = reader.nextString();
            } else if (name.equals("bs")) {
                profile.bs = reader.nextString();
            } else if (name.equals("cc")) {
                profile.cc = reader.nextString();
            } else if (name.equals("imp")) { // impetuous
                profile.imp = reader.nextString();
            } else if (name.equals("name")) {
                unit.name = reader.nextString();
            } else if (name.equals("isc")) {
                unit.isc = reader.nextString();
                profile.isc = unit.isc;
            } else if (name.equals("mov")) {
                profile.mov = reader.nextString();
            } else if (name.equals("type")) {
                profile.type = reader.nextString();
            } else if (name.equals("ph")) {
                profile.ph = reader.nextString();
            } else if (name.equals("spec")) {
                profile.spec.addAll(parseSubArray(reader));
            } else if (name.equals("w")) {
                profile.wounds = reader.nextString();
            } else if (name.equals("wip")) {
                profile.wip = reader.nextString();
            } else if (name.equals("profiles")) {
                unit.profiles.addAll(parseProfiles(reader));
            } else if (name.equals("image")) { // this is the unit image to use if there isn't one specifically; usually a spec-ops model using a base model's logo
                unit.image = reader.nextString();
            } else if (name.equals("wtype")) {
                profile.woundType = reader.nextString();
            } else if (name.equals("hackable")) {
                profile.hackable = reader.nextString().equals("X");
            } else if (name.equals("ava")) {
                profile.ava = reader.nextString();
            } else if (name.equals("sharedAva")) { // Caledonian Volunteers
                unit.sharedAva = reader.nextString();
            } else if (name.equals("notFor")) { // possibly YuanYuan aren't allowed in yuJing, but shouldn't this be tracked elsewhere?
                reader.skipValue();
            } else if (name.equals("s")) {
                profile.silhouette = reader.nextString();
            } else {
                throw new IOException("Unknown tag in parse Unit: " + name);
            }
        }

        reader.endObject();

        // the data doesn't always have profiles as an array, sometimes that data is incorporated
        // in the unit itself.  So, we need to read a profile's data along the way and find out at
        // the end if there was a profiles subsection.  If there was no subsection, the profile we
        // collected along the way is valid.  If there is a subsection, discard the profile (which
        // should be empty).  There should never be a case where there is a profiles subarray AND
        // a unit-level profile...
        if (unit.profiles.size() == 0) {
            unit.profiles.add(profile);
        } else if (profile.bsw.size() > 0) {
            Log.d(TAG, "Missed some data!");
        }
        return unit;
    }

    private ArrayList<Profile> parseProfiles(JsonReader reader) throws IOException {
        reader.beginArray();

        ArrayList<Profile> profiles = new ArrayList<Profile>();

        while (reader.hasNext()) {
            profiles.add(parseProfile(reader));
        }

        reader.endArray();
        return profiles;
    }

    private Profile parseProfile(JsonReader reader) throws IOException {
        Profile profile = new Profile();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            JsonToken peek = reader.peek();
            if (peek == JsonToken.NULL) {
                reader.skipValue();
                continue;
            }

            if (name.equals("bsw")) {
                profile.bsw.addAll(parseSubArray(reader));
            } else if (name.equals("ccw")) {
                profile.ccw.addAll(parseSubArray(reader));
            } else if (name.equals("irr")) {
                profile.irr = reader.nextString().equals("X");
            } else if (name.equals("arm")) {
                profile.arm = reader.nextString();
            } else if (name.equals("bts")) {
                profile.bts = reader.nextString();
            } else if (name.equals("bs")) {
                profile.bs = reader.nextString();
            } else if (name.equals("cc")) {
                profile.cc = reader.nextString();
            } else if (name.equals("imp")) { //impetuous
                profile.imp = reader.nextString();
            } else if (name.equals("name")) {
                profile.name = reader.nextString();
            } else if (name.equals("mov")) {
                profile.mov = reader.nextString();
            } else if (name.equals("ph")) {
                profile.ph = reader.nextString();
            } else if (name.equals("spec")) {
                profile.spec.addAll(parseSubArray(reader));
            } else if (name.equals("w")) {
                profile.wounds = reader.nextString();
            } else if (name.equals("wip")) {
                profile.wip = reader.nextString();
            } else if (name.equals("type")) {
                profile.type = reader.nextString();
            } else if (name.equals("cube")) {
                profile.cube = reader.nextString();
            } else if (name.equals("cbcode")) {
                reader.skipValue();
            } else if (name.equals("note")) {
                profile.note = reader.nextString();
            } else if (name.equals("wtype")) {
                profile.woundType = reader.nextString();
            } else if (name.equals("optionSpecific")) {
                profile.optionSpecific = reader.nextString();
            } else if (name.equals("isc")) {  // mirage-5
                profile.isc = reader.nextString();
            } else if (name.equals("independent")) {
                reader.skipValue();
            } else if (name.equals("s")) {
                profile.silhouette = reader.nextString();
            } else if (name.equals("hackable")) {
                profile.hackable = reader.nextString().equals("X");
            } else if (name.equals("ava")) {
                profile.ava = reader.nextString();
            } else if (name.equals("image")) {
                reader.nextString();
            } else {
                throw new IOException("Unknown tag in parse Profile: " + name);
            }

        }

        reader.endObject();

        return profile;
    }


    private Option parseChild(JsonReader reader) throws IOException {
        Option option = new Option();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("bsw")) {
                option.bsw.addAll(parseSubArray(reader));
            } else if (name.equals("swc")) {
                option.swc = reader.nextDouble();
            } else if (name.equals("cbcode")) {
                reader.skipValue();
            } else if (name.equals("code")) {
                option.code = reader.nextString();
            } else if (name.equals("note")) {
                option.note = reader.nextString();
            } else if (name.equals("ccw")) {
                option.ccw.addAll(parseSubArray(reader));
            } else if (name.equals("codename")) {
                option.codename = reader.nextString();
            } else if (name.equals("cost")) {
                option.cost = Integer.parseInt(reader.nextString());
            } else if (name.equals("spec")) {
                option.spec.addAll(parseSubArray(reader));
            } else if (name.equals("independent")) {
                // This may never become relevant until/unless an in-play unit status/retreat
                // tracker is implemented.
                reader.beginObject();
                reader.nextName();
                reader.nextString(); //cost
                reader.nextName();
                reader.nextString(); //swc
                reader.endObject();
            } else if (name.equals("profile")) {
                option.profile = Integer.parseInt(reader.nextString());
            } else {
                throw new IOException("Unknown tag in parse Child: " + name);
            }

        }

        reader.endObject();
        return option;
    }

    private ArrayList<String> parseSubArray(JsonReader reader) throws IOException {
        ArrayList<String> subArray = new ArrayList<String>();

        reader.beginArray();

        while (reader.hasNext()) {
            subArray.add(reader.nextString());
        }

        reader.endArray();

        return subArray;
    }


}
