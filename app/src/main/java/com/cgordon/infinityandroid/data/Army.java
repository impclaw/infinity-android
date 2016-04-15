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

public class Army implements Parcelable {

    public long dbId;
    public String faction;
    public String name;
    public String abbr;

    public ArrayList armyUnits;

    public Army() {
        armyUnits = new ArrayList<ArmyUnit>();
    }

    public Army(Parcel parcel) {
        dbId = parcel.readLong();
        faction = parcel.readString();
        name = parcel.readString();
        abbr = parcel.readString();
        armyUnits = parcel.readArrayList(null);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeString(faction);
        dest.writeString(name);
        dest.writeString(abbr);
        dest.writeList(armyUnits);
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
