package dk.itu.map;

import dk.itu.map.structures.WriteAble;

import java.io.*;
import java.util.HashMap;

public class tempHashMapLongToInt extends HashMap<Long, Integer> implements WriteAble {
    @Override
    public void write(String path) throws FileNotFoundException, IOException {
        DataOutputStream stream = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(path)
                )
        );
        write(stream);
        stream.close();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeInt(keySet().size());
        for(long key : keySet()){
            stream.writeLong(key);
            stream.writeInt(get(key));
        }
    }

    @Override
    public void read(String path) throws IOException{
        DataInputStream stream = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(path)
                )
        );
        read(stream);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        int size = stream.readInt();
        for(int i = 0; i < size; i++){
            put(stream.readLong(), stream.readInt());
        }
    }
}