package com.cgordon.infinityandroid.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cgordon on 5/31/2015.
 */
public class Unit implements Parcelable {

    public long dbId;
    public String ava;  // sometimes this can be 'T', so can't be an int
    public String sharedAva; // Name of other unit that subtracts from this units Ava
    public ArrayList<Profile> profiles;
    public ArrayList<Option> options;
    public String faction;
    public String note;  //used?
    public String name;
    public String isc;
    public String image;
    public boolean linkable = false;


    public Unit() {
        options = new ArrayList<Option>();
        profiles = new ArrayList<Profile>();
    }

    public Unit(Parcel parcel) {
        dbId = parcel.readLong();
        ava = parcel.readString();
        sharedAva = parcel.readString();
        profiles = parcel.readArrayList(Profile.class.getClassLoader());
        options = parcel.readArrayList(Option.class.getClassLoader());
        faction = parcel.readString();
        note = parcel.readString();
        name = parcel.readString();
        isc = parcel.readString();
        image = parcel.readString();
        linkable = parcel.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeString(ava);
        dest.writeString(sharedAva);
        dest.writeList(profiles);
        dest.writeList(options);
        dest.writeString(faction);
        dest.writeString(note);
        dest.writeString(name);
        dest.writeString(isc);
        dest.writeString(image);
        dest.writeByte((byte) (linkable ? 1 : 0));
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(isc).append("\t");
        sb.append("Ava: " + ava).append("\t");

        if (linkable) {
            sb.append("Linkable");
        }

        sb.append("\n");

        sb.append("\nProfiles:").append("\n\n");
        for (int i = 0; i < profiles.size(); i++) {
            sb.append(profiles.get(i).toString()).append("\n\n");
        }

        sb.append("Options:").append("\n");
        for (int i = 0; i < options.size(); i++) {
            sb.append("- ").append(name).append(options.get(i).toString()).append("\n");
        }
        sb.append("\n");

        if ((note != null) && (!note.isEmpty())) {
            sb.append("Note: ").append(note).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {

        // determine equality based on ISC name.  This is used ofr indexOf operations on a list.
        if (o instanceof Unit) {
            return isc.equals(((Unit) o).isc);
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Unit> CREATOR
            = new Parcelable.Creator<Unit>() {
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

}
