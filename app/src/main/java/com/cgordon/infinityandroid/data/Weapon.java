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

public class Weapon implements Parcelable {

    public String ammo;
    public String burst;
    public String cc;
    public String damage;
    public String em_vul;
    public String long_dist;
    public String long_mod;
    public String max_dist;
    public String max_mod;
    public String medium_dist;
    public String medium_mod;
    public String name;
    public String note;
    public String short_dist;
    public String short_mod;
    public String template;
    public String uses;
    public String attr;
    public String suppressive;
    public String alt_profile;
    public String mode;

    public Weapon() {}

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("- ").append(name).append("\t");

        if (!short_dist.equals("--")) {
            sb.append("S:").append(short_dist).append("(").append(short_mod).append(") / ");
        }
        if (!medium_dist.equals("--")) {
            sb.append("M:").append(medium_dist).append("(").append(medium_mod).append(") / ");
        }
        if (!long_dist.equals("--")) {
            sb.append("L:").append(long_dist).append("(").append(long_mod).append(") / ");
        }
        if (!max_dist.equals("--")) {
            sb.append("X:").append(max_dist).append("(").append(max_mod).append(")").append("\t");
        }

        sb.append("DMG:").append(damage).append("\t");
        sb.append("B:").append(burst).append("\t");
        sb.append("Ammo:").append(ammo).append("\t");

        if ((cc != null) && (cc.equals("Yes"))) {
            sb.append(", CC");
        }

        if ((template != null) && (!template.equals("No"))) {
            sb.append(", ").append(template);
        }

        if (uses != null) {
            sb.append(", ").append("Disposable(").append(uses).append(")");
        }

        if (attr != null) {
            sb.append(", ").append(attr);
        }

        if ((suppressive != null) && (suppressive.equals("Yes"))) {
            sb.append(", Suppressive Fire");
        }

        if (mode != null) {
            sb.append(", Mode:").append(mode);
        }

        if (note != null) {
            sb.append(", Note:").append(note);
        }

        return sb.toString();
    }

    public Weapon(Parcel parcel) {
        ammo = parcel.readString();
        burst = parcel.readString();
        cc = parcel.readString();
        damage = parcel.readString();
        em_vul = parcel.readString();
        long_dist = parcel.readString();
        long_mod = parcel.readString();
        max_dist = parcel.readString();
        max_mod = parcel.readString();
        medium_dist = parcel.readString();
        medium_mod = parcel.readString();
        name = parcel.readString();
        note = parcel.readString();
        short_dist = parcel.readString();
        short_mod = parcel.readString();
        template = parcel.readString();
        uses = parcel.readString();
        attr = parcel.readString();
        suppressive = parcel.readString();
        alt_profile = parcel.readString();
        mode = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(ammo);
         dest.writeString(burst);
         dest.writeString(cc);
         dest.writeString(damage);
         dest.writeString(em_vul);
         dest.writeString(long_dist);
         dest.writeString(long_mod);
         dest.writeString(max_dist);
         dest.writeString(max_mod);
         dest.writeString(medium_dist);
         dest.writeString(medium_mod);
         dest.writeString(name);
         dest.writeString(note);
         dest.writeString(short_dist);
         dest.writeString(short_mod);
         dest.writeString(template);
         dest.writeString(uses);
         dest.writeString(attr);
         dest.writeString(suppressive);
         dest.writeString(alt_profile);
         dest.writeString(mode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Weapon> CREATOR
            = new Parcelable.Creator<Weapon>() {
        public Weapon createFromParcel(Parcel in) {
            return new Weapon(in);
        }

        public Weapon[] newArray(int size) {
            return new Weapon[size];
        }
    };    
}
