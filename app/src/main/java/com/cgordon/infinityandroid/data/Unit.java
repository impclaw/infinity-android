package com.cgordon.infinityandroid.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cgordon on 5/31/2015.
 */
public class Unit implements Comparable<Unit> {

    // These are listed in reverse order to aid the compareTo function below.  A higher index  means
    // that it will be listed before a lower index.  ie. IndexOf("LI") == 6 and IndexOf("MI") == 5
    // so 6-5 == 1 (positive number, therefore greater than the passed in one)
    private static final String[] m_typeOrder = {
            "LI",
            "MI",
            "HI",
            "TAG",
            "REM",
            "SK",
            "WB",
    };

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

    private List<String> m_typeOrderList;

    public Unit () {
        options = new ArrayList<Option>();
        profiles = new ArrayList<Profile>();
        m_typeOrderList = Arrays.asList(m_typeOrder);
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

        sb.append("Options:").append("\n\n");
        for (int i = 0; i < options.size(); i++) {
            sb.append(name).append(options.get(i).toString()).append("\n");
        }
        sb.append("\n");

        if ((note != null) &&(!note.isEmpty())) {
            sb.append("Note: ").append(note).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Unit another) {

        if (profiles.get(0).type.equals(another.profiles.get(0))) {
            return isc.compareTo(another.isc);
        } else {
                return m_typeOrderList.indexOf(profiles.get(0).type) - m_typeOrderList.indexOf(another.profiles.get(0).type);
        }
    }
}
