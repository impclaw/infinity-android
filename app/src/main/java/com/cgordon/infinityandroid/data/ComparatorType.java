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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparatorType implements Comparator {

    // These are listed in reverse order to aid the compareTo function below.  A higher index  means
    // that it will be listed before a lower index.  ie. IndexOf("LI") == 6 and IndexOf("MI") == 5
    // so 6-5 == 1 (positive number, therefore greater than the passed in one)
    private static final String[] m_typeOrder = {
            "LI",
            "MI",
            "HI",
            "TAG",
            "REM",
            "SK",
            "WB",
    };

    private final boolean m_isc;

    private List<String> m_typeOrderList;


    public ComparatorType(boolean isc) {
        m_isc = isc;
        m_typeOrderList = Arrays.asList(m_typeOrder);
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

            if (lhsUnit.profiles.get(0).type.equals(rhsUnit.profiles.get(0).type)) {
                return lhsName.compareTo(rhsName);
            } else {
                return m_typeOrderList.indexOf(lhsUnit.profiles.get(0).type) - m_typeOrderList.indexOf(rhsUnit.profiles.get(0).type);
            }
        }
        return 0;
    }

}
