package com.cgordon.infinityandroid.data;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by cgordon on 5/30/2015.
 */
public class Option {

    public String name;
    public String code; // a short name to identify this child
    public String note;
    public String codename; // not sure where this is used...
    public int cost;
    public Double swc;
    public ArrayList<String> spec; // specific pieces of gear for this Option ie. MSV2, Hacking Device
    public int profile; // This option requires an additional profile (0-based index)

    public ArrayList<String> bsw;
    public ArrayList<String> ccw;


    public Option() {
        bsw = new ArrayList<String>();
        ccw = new ArrayList<String>();
        spec = new ArrayList<String>();
    }

}
