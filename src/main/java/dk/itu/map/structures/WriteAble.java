package dk.itu.map.structures;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface WriteAble {
    void write(String path) throws FileNotFoundException, IOException;
}
