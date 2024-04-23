package dk.itu.map.structures;

import java.io.*;

public interface WriteAble {

    default void write(String path) throws IOException {
        DataOutputStream stream = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(path)
                )
        );

        write(stream);
        stream.close();
    }
    void write(DataOutputStream stream) throws IOException;
    default void read(String path) throws IOException{
        DataInputStream stream = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(path)
                )
        );

        read(stream);
    }
    void read(DataInputStream stream) throws IOException;
}
