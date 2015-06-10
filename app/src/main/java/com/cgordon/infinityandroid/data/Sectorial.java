package com.cgordon.infinityandroid.data;

import java.util.ArrayList;

/**
 * Created by cgordon on 6/1/2015.
 */
public class Sectorial {

    public long dbId;
    public String army;
    public String name;
    public String abbr;

    public ArrayList<SectorialUnit> units;

    public Sectorial() {
        units = new ArrayList<SectorialUnit>();
    }
}
