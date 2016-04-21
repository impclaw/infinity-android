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

public class UnitElement extends ListElement {

    public long dbId;
    public int group;
    public int unitId;
    public int child;

    public UnitElement(Parcel in) {
        dbId = in.readLong();
        group = in.readInt();
        unitId = in.readInt();
        child = in.readInt();
    }

    public UnitElement(long dbId, int group, int unitId, int child) {
        this.dbId = dbId;
        this.group = group;
        this.unitId = unitId;
        this.child = child;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeInt(group);
        dest.writeInt(unitId);
        dest.writeInt(child);
    }

    public static final Parcelable.Creator<UnitElement> CREATOR
            = new Parcelable.Creator<UnitElement>() {
        public UnitElement createFromParcel(Parcel in) {
            return new UnitElement(in);
        }

        public UnitElement[] newArray(int size) {
            return new UnitElement[size];
        }
    };

}
