package com.cgordon.infinityandroid.data;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cgordon on 5/30/2015.
 */
public class Profile {

    public List<String> bsw;
    public List<String> ccw;
    public List<String> spec;

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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t");
        if (name != null) {
            sb.append(name).append("\t");
        }
        if (irr) {
            sb.append("Irregular").append("\n\t");
        }
        sb.append("Impetuous: ").append(imp).append("\t");
        sb.append("Cube: ").append(cube).append("\t");
        if (isc != null) {
            sb.append("ISC: ").append(isc).append("\t");
        }
        sb.append("Type: ").append(type).append("\n\t");

        sb.append("MOV\tCC\tBS\tPH\tWIP\tARM\tBTS\tW\tS\t\n\t");
        sb.append(mov).append("\t");
        sb.append(cc).append("\t");
        sb.append(bs).append("\t");
        sb.append(ph).append("\t");
        sb.append(wip).append("\t\t");
        sb.append(arm).append("\t\t\t");
        sb.append(bts).append("\t\t");
        sb.append(wounds).append("\t");
        sb.append(silhouette).append("\t");
        sb.append("\n\t");

        if ((spec != null) && (!spec.isEmpty())) {
            sb.append("Special: ").append(TextUtils.join(",", spec)).append("\n\t");
        }
        if ((ccw != null) && (!ccw.isEmpty())) {
            sb.append("CCW: ").append(TextUtils.join(",", ccw)).append("\n\t");
        }
        if ((bsw != null) && (!bsw.isEmpty())) {
            sb.append("BSW: ").append(TextUtils.join(",", bsw)).append("\n\t");
        }
        if ((note != null) && (!note.isEmpty())) {
            sb.append("Note: ").append(note).append("\n\t");
        }
        return sb.toString();
    }
}
