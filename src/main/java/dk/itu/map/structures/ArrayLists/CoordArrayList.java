package dk.itu.map.structures.ArrayLists;

import java.io.*;

public class CoordArrayList extends PrimitiveArrayList {

    // The array that holds the values
    private float[] arrayX;
    private float[] arrayY;

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a default size.
     * default size is 10
      */
    public CoordArrayList() {
        super();
        arrayX = new float[ARRAY_INIT_SIZE];
        arrayY = new float[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a given size.
     * @param init_size
     */
    public CoordArrayList(int init_size) {
        super();
        arrayX = new float[init_size];
        arrayY = new float[init_size];
    }

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a given array.
     * @param array
     */
    public CoordArrayList(float[] array) {
        arrayX = new float[array.length/2];
        arrayY = new float[array.length/2];

        this.addAll(array);
    }

    /**
     * Resizes the array to double the size.
     */
    @Override
    protected void resize() {
        float[] newArrayX = new float[arrayX.length * 2];
        float[] newArrayY = new float[arrayY.length * 2];
        System.arraycopy(arrayX, 0, newArrayX, 0, arrayX.length);
        System.arraycopy(arrayY, 0, newArrayY, 0, arrayY.length);
        arrayX = newArrayX;
        arrayY = newArrayY;
    }

    /**
     * Converts the list to a float array.
     * @return float[]
     */
    public float[] toArray() {
        float[] output = new float[size*2];
        for (int i = 0; i < size; i++) {
            output[i*2] = arrayX[i];
            output[i*2+1] = arrayY[i];
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
        if (size + 1 > arrayX.length) {
            resize();
        }
        arrayX[size] = valueX;
        arrayY[size] = valueY;
        size++;
    }

    public void add(float[] coords){
        add(coords[0], coords[1]);
    }

    /**
     * Adds an array of values to the empty spots in the array.
     * If the array is full, it will resize the array.
     * @param values to be inserted
     */
    public void addAll(float[] values) {
        while (size + values.length/2 > arrayX.length) {
            resize();
        }
        for(int i = 0; i < values.length; i+=2) {
            arrayX[size] = values[i];
            arrayY[size] = values[i+1];
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
            float tempX = arrayX[i];
            float tempY = arrayY[i];

            arrayX[i] = arrayX[size - i - 1];
            arrayY[i] = arrayY[size - i - 1];

            arrayX[size - i - 1] = tempX;
            arrayY[size - i - 1] = tempY;
        }
    }

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return float value at the index
     */
    public float[] get(int index) {
        if (index < 0)
            return new float[]{arrayX[size+index], arrayY[size+index]};
        else
            return new float[]{arrayX[index], arrayY[index]};
    }

    @Override
    public void exchange(int index1, int index2) {
        float tempX = arrayX[index1];
        float tempY = arrayY[index1];

        arrayX[index1] = arrayX[index2];
        arrayY[index1] = arrayY[index2];

        arrayX[index2] = tempX;
        arrayY[index2] = tempY;
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
            stream.writeFloat(arrayX[i]);
            stream.writeFloat(arrayY[i]);
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
        arrayX = new float[size];
        arrayY = new float[size];
        for (int i = 0; i < size; i++) {
            arrayX[i] = stream.readFloat();
            arrayY[i] = stream.readFloat();
        }
    }
}
