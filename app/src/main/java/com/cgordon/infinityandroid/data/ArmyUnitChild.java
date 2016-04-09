/*
 * Copyright 2016 by Chris Gordon
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

public class ArmyUnitChild implements Parcelable {

    public long dbId;
    public int id;
    public Double swc;
    public boolean hide;

    public ArmyUnitChild() {
    }

    public ArmyUnitChild(Parcel parcel) {
        dbId = parcel.readLong();
        id = parcel.readInt();
        swc = parcel.readDouble();
        hide = parcel.readByte() != 0;     //true if byte != 0
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
        dest.writeDouble(swc);
        dest.writeByte((byte) (hide ? 1 : 0));     //true, byte == 1

    }
}
