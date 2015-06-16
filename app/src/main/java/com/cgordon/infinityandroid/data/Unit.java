package com.cgordon.infinityandroid.data;

import java.util.ArrayList;
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
        sb.append(isc).append("\n");

        return sb.toString();
    }
}
