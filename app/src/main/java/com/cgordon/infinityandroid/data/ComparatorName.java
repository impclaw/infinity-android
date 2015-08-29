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

import java.util.Comparator;

public class ComparatorName implements Comparator {

    private boolean m_isc;

    public ComparatorName(boolean isc) {
        m_isc = isc;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        if ((lhs instanceof Unit) && (rhs instanceof Unit)) {

            Unit lhsUnit = (Unit) lhs;
            Unit rhsUnit = (Unit) rhs;
            String lhsName, rhsName;

            if (m_isc) {
                lhsName = lhsUnit.isc;
                rhsName = rhsUnit.isc;
            } else {
                lhsName = lhsUnit.name;
                rhsName = rhsUnit.name;
            }

            return lhsName.compareTo(rhsName);
        }

        return 0;
    }
}
