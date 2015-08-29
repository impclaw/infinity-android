/*
 * Copyright 2015 by Chris Gordon
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

public class Weapon {

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

}

// ? public String em_vul;
