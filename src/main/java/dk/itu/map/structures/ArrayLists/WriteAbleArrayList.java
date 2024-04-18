package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

import java.util.ArrayList;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class WriteAbleArrayList <T extends WriteAble> extends ArrayList<T> implements WriteAble {
    @Override
    public void write(String path) throws IOException {
        DataOutputStream stream = new DataOutputStream(
            new BufferedOutputStream(
                new FileOutputStream(path, true)));

        write(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeInt(size());
        for (WriteAble writeAble : this) {
            writeAble.write(stream);
        }

        stream.close();
    }

    @Override
    public void read(String path) throws IOException {
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
            get(i).read(stream);
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof WriteAbleArrayList)) return false;
        WriteAbleArrayList other = (WriteAbleArrayList) obj;
        if(size() != other.size()) return false;
        for(int i = 0; i < size(); i++){
            if(!get(i).equals(other.get(i))) return false;
        }

        return true;
    }
}
