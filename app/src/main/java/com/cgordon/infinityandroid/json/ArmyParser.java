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

    public void parse(int resourceId) {
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
                } else if (name.equals("imp")) {
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
                } else if (name.equals("functions")) {
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
            } else if (name.equals("imp")) {
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
            } else if (name.equals("hiddenalias")) {
                Log.d(TAG, name + " " + reader.nextString());
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




}
