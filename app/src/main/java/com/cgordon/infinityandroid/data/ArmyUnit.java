package com.cgordon.infinityandroid.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cgordon on 6/1/2015.
 */
public class ArmyUnit implements Parcelable{

    public long dbId;
    public long sectorialId;
    public String ava;
    public String isc;
    public boolean linkable;
    public String army;

    public ArmyUnit() {}

    public ArmyUnit(Parcel parcel) {
        dbId = parcel.readLong();
        sectorialId = parcel.readLong();
        ava = parcel.readString();
        isc = parcel.readString();
        linkable = parcel.readByte() != 0;     //linkable == true if byte != 0
        army = parcel.readString();
    }

    public static final Parcelable.Creator<ArmyUnit> CREATOR
            = new Parcelable.Creator<ArmyUnit>() {
        public ArmyUnit createFromParcel(Parcel in) {
            return new ArmyUnit(in);
        }

        public ArmyUnit[] newArray(int size) {
            return new ArmyUnit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeLong(sectorialId);
        dest.writeString(ava);
        dest.writeString(isc);
        dest.writeByte((byte) (linkable ? 1 : 0));     //if linkable == true, byte == 1
        dest.writeString(army);

    }
}
