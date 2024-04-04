package dk.itu.map.structures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class WriteAbleArrayList <T extends WriteAble> extends ArrayList<T> implements WriteAble {
    @Override
    public void write(String path) throws FileNotFoundException, IOException {
        for (WriteAble writeAble : this) {
            writeAble.write(path);
        }
    }
}
