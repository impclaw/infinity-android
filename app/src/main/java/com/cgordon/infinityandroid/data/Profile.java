/*
 * Copyright 2015 by Chris Gordon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cgordon.infinityandroid.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;

public class Profile implements Parcelable {

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
    public String ava;

    public String optionSpecific; // this profile is only used with certain options.  Should match up with Option.profile (?)
    public String allProfilesMustDie; //If there are multiple profiles, victory points aren't awarded until all profiles are dead

    public Profile() {
        bsw = new ArrayList<String>();
        ccw = new ArrayList<String>();
        spec = new ArrayList<String>();
    }

    public Profile(Parcel parcel) {
        bsw = parcel.readArrayList(String.class.getClassLoader());
        ccw = parcel.readArrayList(String.class.getClassLoader());
        spec = parcel.readArrayList(String.class.getClassLoader());

        mov = parcel.readString();
        cc = parcel.readString();
        bs = parcel.readString();
        ph = parcel.readString();
        wip = parcel.readString();
        arm = parcel.readString();
        bts = parcel.readString();
        wounds = parcel.readString();
        woundType = parcel.readString();
        silhouette = parcel.readString();

        irr = parcel.readByte() != 0;
        imp = parcel.readString();
        cube = parcel.readString();
        note = parcel.readString();
        isc = parcel.readString();
        name = parcel.readString();
        type = parcel.readString();
        hackable = parcel.readByte() != 0;
        ava = parcel.readString();

        optionSpecific = parcel.readString();
        allProfilesMustDie = parcel.readString();

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(bsw);
        dest.writeList(ccw);
        dest.writeList(spec);

        dest.writeString(mov);
        dest.writeString(cc);
        dest.writeString(bs);
        dest.writeString(ph);
        dest.writeString(wip);
        dest.writeString(arm);
        dest.writeString(bts);
        dest.writeString(wounds);
        dest.writeString(woundType);
        dest.writeString(silhouette);

        dest.writeByte((byte) (irr ? 1 : 0));
        dest.writeString(imp);
        dest.writeString(cube);
        dest.writeString(note);
        dest.writeString(isc);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeByte((byte) (hackable ? 1 : 0));
        dest.writeString(ava);

        dest.writeString(optionSpecific);
        dest.writeString(allProfilesMustDie);

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
        if (ava != null) {
            sb.append("AVA: ").append(ava).append("\t");
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Profile> CREATOR
            = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

}
