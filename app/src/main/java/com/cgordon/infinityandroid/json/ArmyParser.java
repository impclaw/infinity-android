package com.cgordon.infinityandroid.json;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cgordon on 5/27/2015.
 */
public class ArmyParser {

    private final String TAG = ArmyParser.class.getSimpleName();

    Context m_context;
    public ArmyParser(Context context) {
        m_context = context;
    }

    public void parseSectorialList (int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                parseSectorialArmy(reader);
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


    private void parseSectorialArmy(JsonReader reader) throws IOException {
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("army")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("name")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("abbr")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("units")) {
                parseSectorialUnits(reader);
            } else {
                throw new IOException("Unable to parse tag in parseSectorialArmy: " + name);
            }
        }
        reader.endObject();

    }

    private void parseSectorialUnits(JsonReader reader) throws IOException {
        reader.beginArray();

        while (reader.hasNext()) {
            parseSectorialUnit(reader);
        }
        
        reader.endArray();
    }

    private void parseSectorialUnit(JsonReader reader) throws IOException {
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("ava")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("isc")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("linkable")) {
                Log.d(TAG, name + " " + reader.nextBoolean());
            } else if (name.equals("army")) { // it's from an army other than the sectorial army
                Log.d(TAG, name + " " + reader.nextString());
            } else {
                throw new IOException("unknown tag in parseSectorialUnit: " + name);
            }
        }

        reader.endObject();
    }

    public void parseArmy(int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                parseUnit(reader);
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private void parseChilds(JsonReader reader) throws IOException {
        reader.beginArray();

        while (reader.hasNext()) {
            parseChild(reader);
        }

        reader.endArray();
    }

    private void parseUnit(JsonReader reader) throws IOException {
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("childs")) {
                    parseChilds(reader);
                } else if (name.equals("bsw")) {
                    parseSubArray(reader);
                } else if (name.equals("ccw")) {
                    parseSubArray(reader);
                } else if (name.equals("ava")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("irr")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("arm")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("cbcode")) {
                    parseSubArray(reader);
                } else if (name.equals("bts")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("army")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("note")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("cube")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("bs")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("cc")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("imp")) { // impetuous
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("name")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("isc")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("mov")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("type")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("ph")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("spec")) {
                    parseSubArray(reader);
                } else if (name.equals("w")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("wip")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("profiles")) {
                    parseProfiles(reader);
                } else if (name.equals("image")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("wtype")) {
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("sharedAva")) { // Caledonian Volunteers
                    Log.d(TAG, name + " " + reader.nextString());
                } else if (name.equals("functions")) { // can be ignored
                    reader.skipValue();
                } else if (name.equals("altp")) {
                    reader.skipValue();
                } else if (name.equals("notFor")) {
                    reader.skipValue();
                } else {
                    throw new IOException("Unknown tag in parse Unit: " + name);
                }
            }

            reader.endObject();



    }

    private void parseProfiles(JsonReader reader) throws IOException {
        reader.beginArray();

        while (reader.hasNext()) {
            parseProfile(reader);
        }

        reader.endArray();
    }

    private void parseProfile(JsonReader reader) throws IOException {

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("bsw")) {
                parseSubArray(reader);
            } else if (name.equals("ccw")) {
                parseSubArray(reader);
            } else if (name.equals("irr")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("arm")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("bts")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("bs")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("cc")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("imp")) { //impetuous
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("name")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("mov")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("ph")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("spec")) {
                parseSubArray(reader);
            } else if (name.equals("w")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("wip")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("type")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("cube")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("ava")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("cbcode")) {
                reader.skipValue();
            } else if (name.equals("army")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("note")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("wtype")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("optionSpecific")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("isc")) {  // mirage-5
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("independent")) {
                reader.skipValue();
            } else if (name.equals("allProfilesMustDie")) {  // mirage-5
                Log.d(TAG, name + " " + reader.nextString());
            } else {
                throw new IOException("Unknown tag in parse Profile: " + name);
            }

        }

        reader.endObject();
    }


    private void parseChild(JsonReader reader) throws IOException {
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("bsw")) {
                parseSubArray(reader);
            } else if (name.equals("swc")) {
                Log.d(TAG, name + " " + reader.nextDouble());
            } else if (name.equals("cbcode")) {
                reader.skipValue();
            } else if (name.equals("code")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("note")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("ccw")) {
                parseSubArray(reader);
            } else if (name.equals("codename")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("cost")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("spec")) {
                parseSubArray(reader);
            } else if (name.equals("profile")) {
                Log.d(TAG, name + " " + reader.nextString());
// was used in the deva functionary, but not used in MayaNet
//            } else if (name.equals("hiddenalias")) {
//                Log.d(TAG, name + " " + reader.nextString());
            } else {
                throw new IOException("Unknown tag in parse Child: " + name);
            }

        }

        reader.endObject();
    }

    private void parseSubArray(JsonReader reader) throws IOException {
        reader.beginArray();

        while (reader.hasNext()) {
            Log.d(TAG, "w: " + reader.nextString());
        }

        reader.endArray();
    }


    public void parseWeapons(int resourceId) {
        InputStream inputStream = m_context.getResources().openRawResource(resourceId);

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        try {
            reader.beginArray();

            while (reader.hasNext()) {
                parseWeapon(reader);
            }

            reader.endArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private void parseWeapon(JsonReader reader) throws IOException {
        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("ammo")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("burst")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("cc")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("damage")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("em_vul")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("long_dist")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("long_mod")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("max_dist")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("max_mod")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("medium_dist")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("medium_mod")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("name")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("note")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("short_dist")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("short_mod")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("template")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("uses")) {
                Log.d(TAG, name + " " + reader.nextString());
            } else if (name.equals("attr")) { // attribute
                Log.d(TAG, name + " " + reader.nextString());
            } else {
                throw new IOException("unknown tag in praseWeaon: " + name);
            }
        }
        reader.endObject();
    }


}
