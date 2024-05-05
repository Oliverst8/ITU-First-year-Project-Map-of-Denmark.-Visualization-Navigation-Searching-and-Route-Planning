package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
        for (int i = 0; i < values.length; i += 2) {
            list.add(values[i], values[i + 1]);
        }
        return list;
    }

    public static String getTestFilesPath() {
        return "src/test/java/itu/map/testFiles/";
    }

    public static void deleteFile(String path) throws IOException {
        FileUtils.delete(new File(path));
    }
}
