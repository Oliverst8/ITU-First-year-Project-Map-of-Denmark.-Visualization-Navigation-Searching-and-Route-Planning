package dk.itu.map.structures.ArrayLists;

import java.io.*;

public class CoordArrayList extends PrimitiveArrayList {
    // The array that holds the values
    private float[] arrayLat;
    private float[] arrayLon;

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a default size.
     * default size is 10
      */
    public CoordArrayList() {
        super();
        arrayLat = new float[ARRAY_INIT_SIZE];
        arrayLon = new float[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a given size.
     * @param init_size
     */
    public CoordArrayList(int init_size) {
        super(init_size);
        arrayLat = new float[init_size];
        arrayLon = new float[init_size];
    }

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a given array.
     * @param array
     */
    public CoordArrayList(float[] array) {
        arrayLat = new float[array.length/2];
        arrayLon = new float[array.length/2];

        this.addAll(array);
    }

    /**
     * Resizes the array to double the size.
     */
    @Override
    protected void resize() {
        float[] newArrayLat = new float[arrayLat.length * 2];
        float[] newArrayLon = new float[arrayLon.length * 2];
        System.arraycopy(arrayLat, 0, newArrayLat, 0, arrayLat.length);
        System.arraycopy(arrayLon, 0, newArrayLon, 0, arrayLon.length);
        arrayLat = newArrayLat;
        arrayLon = newArrayLon;
        capacity = arrayLat.length;
    }

    @Override
    public void exchange(int index1, int index2) {
        float tempLat = arrayLat[index1];
        float tempLon = arrayLon[index1];

        arrayLat[index1] = arrayLat[index2];
        arrayLon[index1] = arrayLon[index2];

        arrayLat[index2] = tempLat;
        arrayLon[index2] = tempLon;
    }

    /**
     * Converts the list to a float array.
     * @return float[]
     */
    public float[] toArray() {
        float[] output = new float[size*2];
        for (int i = 0; i < size; i++) {
            output[i*2] = arrayLat[i];
            output[i*2+1] = arrayLon[i];
        }
        return output;
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param valueX to be inserted
     * @param valueY to be inserted
     */
    public void add(float valueX, float valueY) {
        if (size + 1 > arrayLat.length) {
            resize();
        }
        arrayLat[size] = valueX;
        arrayLon[size] = valueY;
        size++;
    }

    public void add(float[] coords){
        add(coords[0], coords[1]);
    }

    public void set(int index, float[] coords){
        arrayLat[index] = coords[0];
        arrayLon[index] = coords[1];
    }
    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return float value at the index
     */
    public float[] get(int index) {
        if (index < 0)
            return new float[]{arrayLat[size+index], arrayLon[size+index]};
        else
            return new float[]{arrayLat[index], arrayLon[index]};
    }

    /**
     * Adds an array of values to the empty spots in the array.
     * If the array is full, it will resize the array.
     * @param values to be inserted
     */
    public void addAll(float[] values) {
        while (size + values.length/2 > arrayLat.length) {
            resize();
        }
        for(int i = 0; i < values.length; i+=2) {
            arrayLat[size] = values[i];
            arrayLon[size] = values[i+1];
            size++;
        }
    }

    /**
     * Reverses the array.
     * Will swap in coordinate pairs.
     * It will swap the first and last values, then the second and second to last values, and so on.
     * If the size is odd, the middle value will not be swapped.
     */
    public void reverse() {
        for (int i = 0; i < size / 2; i++) {
            float tempX = arrayLat[i];
            float tempY = arrayLon[i];

            arrayLat[i] = arrayLat[size - i - 1];
            arrayLon[i] = arrayLon[size - i - 1];

            arrayLat[size - i - 1] = tempX;
            arrayLon[size - i - 1] = tempY;
        }
    }

    /**
     * Returns a new instance of the array, with the same values
     */
    public CoordArrayList clone() {
        return new CoordArrayList(toArray());
    }

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
        stream.writeInt(size);
        for (int i = 0; i < size; i++) {
            stream.writeFloat(arrayLat[i]);
            stream.writeFloat(arrayLon[i]);
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
        size = stream.readInt();
        arrayLat = new float[size];
        arrayLon = new float[size];
        for (int i = 0; i < size; i++) {
            arrayLat[i] = stream.readFloat();
            arrayLon[i] = stream.readFloat();
        }
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CoordArrayList that = (CoordArrayList) obj;
        if (this.size != that.size) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if (this.arrayLat[i] != that.arrayLat[i] || this.arrayLon[i] != that.arrayLon[i]) {
                return false;
            }
        }
        return true;
    }
}
