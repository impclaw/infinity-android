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

public class ArmyList implements Parcelable {

    public long dbId = -1;
    public String name = null;
    public long armyId = -1;
    public  int points = 0;

    public ArmyList() {}

    public ArmyList(Parcel parcel) {
        dbId = parcel.readLong();
        name = parcel.readString();
        armyId = parcel.readLong();
        points = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeString(name);
        dest.writeLong(armyId);
        dest.writeInt(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ArmyList> CREATOR
            = new Parcelable.Creator<ArmyList>() {
        public ArmyList createFromParcel(Parcel in) {
            return new ArmyList(in);
        }

        public ArmyList[] newArray(int size) {
            return new ArmyList[size];
        }
    };

}
