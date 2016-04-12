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

import java.util.ArrayList;

public class ArmyUnit implements Parcelable {

    public long dbId;
    public int id;
    public long armyId;
    public String ava;
    public boolean linkable;

    public ArrayList<ArmyUnitChild> children;

    public ArmyUnit() {
        children = new ArrayList<>();
    }

    public ArmyUnit(Parcel parcel) {
        dbId = parcel.readLong();
        id = parcel.readInt();
        armyId = parcel.readLong();
        ava = parcel.readString();
        linkable = parcel.readByte() != 0;     //linkable == true if byte != 0
        children = parcel.readArrayList(null);
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
        dest.writeInt(id);
        dest.writeLong(armyId);
        dest.writeString(ava);
        dest.writeByte((byte) (linkable ? 1 : 0));     //if linkable == true, byte == 1
        dest.writeList(children);

    }
}
