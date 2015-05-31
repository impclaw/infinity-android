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
public class WeaponParser {

    private final String TAG = WeaponParser.class.getSimpleName();
    private final Context m_context;

    public WeaponParser(Context context) {
        m_context = context;
    }

    public void parse(int resourceId) {
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
