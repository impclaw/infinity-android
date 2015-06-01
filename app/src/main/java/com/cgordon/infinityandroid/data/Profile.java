package com.cgordon.infinityandroid.data;

import java.util.ArrayList;

/**
 * Created by cgordon on 5/30/2015.
 */
public class Profile {

    public ArrayList<String> bsw;
    public ArrayList<String> ccw;
    public ArrayList<String> spec;

    public String mov;
    public String cc;
    public String bs;
    public String ph;
    public String wip;
    public String arm;
    public String bts;
    public String wounds;
    public String woundType;
    public String silhouette;
    public boolean irr;
    public String imp;
    public String cube;
    public String note;
    public String isc;
    public String name;
    public String type;
    public boolean hackable;

    public String optionSpecific; // this profile is only used with certain options.  Should match up with Option.profile (?)
    public String allProfilesMustDie; //If there are multiple profiles, victory points aren't awarded until all profiles are dead

    public Profile() {
        bsw = new ArrayList<String>();
        ccw = new ArrayList<String>();
        spec = new ArrayList<String>();
    }

}
