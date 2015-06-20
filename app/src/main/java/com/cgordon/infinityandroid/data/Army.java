package com.cgordon.infinityandroid.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cgordon on 6/1/2015.
 */
public class Army implements Parcelable {

    public long dbId;
    public String faction;
    public String name;
    public String abbr;

    public ArrayList units;

    public Army() {
        units = new ArrayList<ArmyUnit>();
    }

    public Army(Parcel parcel) {
        dbId = parcel.readLong();
        faction = parcel.readString();
        name = parcel.readString();
        abbr = parcel.readString();
        units = parcel.readArrayList(null);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeString(faction);
        dest.writeString(name);
        dest.writeString(abbr);
        dest.writeList(units);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Army> CREATOR
            = new Parcelable.Creator<Army>() {
        public Army createFromParcel(Parcel in) {
            return new Army(in);
        }

        public Army[] newArray(int size) {
            return new Army[size];
        }
    };
}
