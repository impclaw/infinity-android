package com.cgordon.infinityandroid.data;

import java.util.Comparator;

/**
 * Created by cgordon on 7/2/2015.
 */
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
