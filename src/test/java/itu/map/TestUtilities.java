package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayListV2;
import dk.itu.map.structures.ArrayLists.LongArrayList;

public class TestUtilities {
    public static LongArrayList createLongArrayList(long... values) {
        LongArrayList list = new LongArrayList();
        for (long value : values) {
            list.add(value);
        }
        return list;
    }

    public static CoordArrayListV2 createCoordArrayList(float... values) {
        CoordArrayListV2 list = new CoordArrayListV2();
        for (int i = 0; i < values.length; i += 2) {
            list.add(values[i], values[i + 1]);
        }
        return list;
    }

    public static String getTestFilesPath() {
        return "src/test/java/itu/map/testFiles/";
    }

}
