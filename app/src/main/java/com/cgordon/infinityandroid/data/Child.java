package com.cgordon.infinityandroid.data;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by cgordon on 5/30/2015.
 */
public class Child {

    ArrayList<String> bsw;
    Double swc;
    String code; // a short name to identify this child
    String note;
    ArrayList<String> ccw;
    String codename;
    int cost;
    ArrayList<String> spec;
    int profile; // when the child only applies to a specific profile

    public Child() {
        bsw = new ArrayList<String>();
        ccw = new ArrayList<String>();
        spec = new ArrayList<String>();
    }

}
