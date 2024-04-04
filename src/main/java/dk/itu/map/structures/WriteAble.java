package dk.itu.map.structures;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface WriteAble {
    void write(String path) throws IOException;
    void write(DataOutputStream stream) throws IOException;
    void read(String path) throws IOException;
    void read(DataInputStream stream) throws IOException;
}
