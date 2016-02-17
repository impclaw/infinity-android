package com.cgordon.infinityandroid.data;

/**
 * Created by cgordon on 2/16/2016.
 */
public class CombatGroup implements ListElement {
    public int m_id = 0;

    public int m_regularOrders = 0;
    public int m_irregularOrders = 0;
    public int m_impetuousOrders = 0;

    public CombatGroup(int id) {
        m_id = id;
    }
}
