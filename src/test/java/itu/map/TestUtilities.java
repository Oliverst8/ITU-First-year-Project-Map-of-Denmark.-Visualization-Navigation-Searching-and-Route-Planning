package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

public class TestUtilities {
    public static LongArrayList createLongArrayList(long... values) {
        LongArrayList list = new LongArrayList();
        for (long value : values) {
            list.add(value);
        }
        return list;
    }

    public static CoordArrayList createCoordArrayList(float... values) {
        CoordArrayList list = new CoordArrayList();
        for (float value : values) {
            list.add(value);
        }
        return list;
    }

}
