package com.cgordon.infinityandroid.data;

import java.util.ArrayList;

/**
 * Created by cgordon on 6/1/2015.
 */
public class Army {

    public long dbId;
    public String faction;
    public String name;
    public String abbr;

    public ArrayList<ArmyUnit> units;

    public Army() {
        units = new ArrayList<ArmyUnit>();
    }
}
