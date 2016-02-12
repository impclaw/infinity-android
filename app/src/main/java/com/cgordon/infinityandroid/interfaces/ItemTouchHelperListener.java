package com.cgordon.infinityandroid.interfaces;

/**
 * Created by cgordon on 2/12/2016.
 */
public interface ItemTouchHelperListener {
    boolean onItemMove(int fromIndex, int toIndex);
    boolean onItemSwipe(int index);

}
