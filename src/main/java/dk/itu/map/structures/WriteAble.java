package dk.itu.map.structures;

import java.io.*;

import dk.itu.map.parser.MapConfig;

public interface WriteAble {
    /**
     * Write the object to a file
     * @param path the path to the file
     * @throws IOException If an error occurs while writing the file
     */
    default void write(String path) throws IOException {
        DataOutputStream stream = new DataOutputStream(
            new BufferedOutputStream(
                new FileOutputStream(path)
            )
        );

        write(stream);
        stream.close();
    }

    /**
     * Write the object to a stream
     * @param stream the stream to write to
     * @throws IOException If an error occurs while writing the stream
     */
    void write(DataOutputStream stream) throws IOException;

    /**
     * Read the object from a file
     * @param path the path to the file
     * @throws IOException If an error occurs while reading the file
     */
    default void read(String path, MapConfig mapConfig) throws IOException{
        DataInputStream stream = new DataInputStream(new BufferedInputStream(mapConfig.locateFile(path)));

        read(stream);
    }

    /**
     * Read the object from a stream
     * @param stream the stream to read from
     * @throws IOException If an error occurs while reading the stream
     */
    void read(DataInputStream stream) throws IOException;
}
