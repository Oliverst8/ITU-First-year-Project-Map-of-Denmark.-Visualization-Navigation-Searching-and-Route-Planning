package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

import java.io.*;
import java.util.ArrayList;

public class WriteAbleArrayList <T extends WriteAble> extends ArrayList<T> implements WriteAble {
    public WriteAbleArrayList(){
        super();
    }

    private int biggestIndex = 0;
    public WriteAbleArrayList(int size){
        super(size);
    }

    @Override
    public boolean add(T element){
        boolean result = super.add(element);
        biggestIndex = Math.max(size(), biggestIndex);
        return result;
    }

    @Override
    public void write(String path) throws IOException {
        DataOutputStream stream = new DataOutputStream(
            new BufferedOutputStream(
                new FileOutputStream(path, true)
            )
        );

        write(stream);
        stream.close();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeInt(biggestIndex);
        for (WriteAble writeAble : this) {
            writeAble.write(stream);
        }

        stream.close();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        int size = stream.readInt();
        biggestIndex = size;
        for(int i = 0; i < size; i++){
            get(i).read(stream);
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof WriteAbleArrayList<?>)) return false;
        WriteAbleArrayList<?> other = (WriteAbleArrayList<?>) obj;
        if(size() != other.size()) return false;
        for(int i = 0; i < size(); i++){
            if(!get(i).equals(other.get(i))) return false;
        }

        return true;
    }
}
