package com.cgordon.infinityandroid.data;

import java.util.ArrayList;

/**
 * Created by cgordon on 5/31/2015.
 */
public class Unit {

    public long dbId;
    public String ava;  // sometimes this can be 'T', so can't be an int
    public String sharedAva; // Name of other unit that subtracts from this units Ava
    public ArrayList<Profile> profiles;
    public ArrayList<Option> options;
    public String army;
    public String note;  //used?
    public String name;
    public String isc;
    public String image;

    public Unit () {
        options = new ArrayList<Option>();
        profiles = new ArrayList<Profile>();

    }

    }
