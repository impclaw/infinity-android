package com.cgordon.infinityandroid.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cgordon on 7/2/2015.
 */
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


    public ComparatorType (boolean isc) {
        m_isc = isc;
        m_typeOrderList = Arrays.asList(m_typeOrder);
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        if ((lhs instanceof Unit) && (rhs instanceof  Unit)) {

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
