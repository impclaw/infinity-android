package com.cgordon.infinityandroid.data;

import java.util.ArrayList;

/**
 * Created by cgordon on 6/1/2015.
 */
public class Army {

    public String army;
    public String name;
    public String abbr;

    public ArrayList<Unit> units;

    public Army() {
        units = new ArrayList<Unit>();
    }
}
