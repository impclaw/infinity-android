/*
 * Copyright 2015-2016 by Chris Gordon
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

public class Option implements Parcelable {

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

    public Option(Parcel parcel) {
        name = parcel.readString();
        code = parcel.readString();
        note = parcel.readString();
        codename = parcel.readString();
        cost = parcel.readInt();
        swc = parcel.readDouble();
        spec = parcel.readArrayList(String.class.getClassLoader());
        profile = parcel.readInt();

        bsw = parcel.readArrayList(String.class.getClassLoader());
        ccw = parcel.readArrayList(String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(note);
        dest.writeString(codename);
        dest.writeInt(cost);
        dest.writeDouble(swc);
        dest.writeList(spec);
        dest.writeInt(profile);

        dest.writeList(bsw);
        dest.writeList(ccw);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (spec.size() > 0) {
            sb.append("(").append(TextUtils.join(",", spec)).append(")");
        }
        sb.append("\t");
        sb.append(TextUtils.join(",", bsw)).append("\t");
        sb.append(TextUtils.join(",", ccw)).append("\t");
        sb.append(swc).append("\t\t");
        sb.append(cost).append("\t");

        if ((note != null) && (!note.isEmpty())) {
            sb.append("Note: ").append(note).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Option> CREATOR
            = new Parcelable.Creator<Option>() {
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

}
