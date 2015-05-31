package com.cgordon.infinityandroid.json;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cgordon on 5/30/2015.
 */
public class SectorialParser {

    private final String TAG = SectorialParser.class.getSimpleName();

    private Context m_context;

    public SectorialParser(Context context) {
        m_context = context;
    }

    public void parse (int resourceId) {
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
}
