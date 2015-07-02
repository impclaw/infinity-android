package com.cgordon.infinityandroid.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cgordon on 5/31/2015.
 */
public class Unit {

    public long dbId;
    public String ava;  // sometimes this can be 'T', so can't be an int
    public String sharedAva; // Name of other unit that subtracts from this units Ava
    public List<Profile> profiles;
    public List<Option> options;
    public String faction;
    public String note;  //used?
    public String name;
    public String isc;
    public String image;
    public boolean linkable = false;


    public Unit () {
        options = new ArrayList<Option>();
        profiles = new ArrayList<Profile>();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(isc).append("\t");
        sb.append("Ava: " + ava).append("\t");

        if (linkable) {
            sb.append("Linkable");
        }

        sb.append("\n");

        sb.append("\nProfiles:").append("\n\n");
        for (int i = 0; i < profiles.size(); i++) {
            sb.append(profiles.get(i).toString()).append("\n\n");
        }

        sb.append("Options:").append("\n");
        for (int i = 0; i < options.size(); i++) {
            sb.append("- ").append(name).append(options.get(i).toString()).append("\n");
        }
        sb.append("\n");

        if ((note != null) &&(!note.isEmpty())) {
            sb.append("Note: ").append(note).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {

        // determine equality based on ISC name.  This is used ofr indexOf operations on a list.
        if (o instanceof Unit) {
            return isc.equals(((Unit) o).isc);
        }
        else {
            return super.equals(o);
        }
    }
}
