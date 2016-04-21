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

public class CombatGroupElement extends ListElement {

    public int m_id = 0;
    public int m_regularOrders = 0;
    public int m_irregularOrders = 0;
    public int m_impetuousOrders = 0;

    public CombatGroupElement(int id) {
        m_id = id;
    }

    public CombatGroupElement(Parcel in) {
        m_id = in.readInt();
        m_regularOrders = in.readInt();
        m_irregularOrders = in.readInt();
        m_impetuousOrders = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(m_id);
        dest.writeInt(m_regularOrders);
        dest.writeInt(m_irregularOrders);
        dest.writeInt(m_impetuousOrders);
    }

    public static final Parcelable.Creator<CombatGroupElement> CREATOR
            = new Parcelable.Creator<CombatGroupElement>() {
        public CombatGroupElement createFromParcel(Parcel in) {
            return new CombatGroupElement(in);
        }

        public CombatGroupElement[] newArray(int size) {
            return new CombatGroupElement[size];
        }
    };

}
