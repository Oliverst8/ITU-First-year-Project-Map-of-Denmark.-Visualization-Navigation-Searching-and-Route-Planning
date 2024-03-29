package dk.itu.map.structures;

public class LongArrayList {
    private long[] list;
    private int size;
    private final int ARRAY_INIT_SIZE = 100_000;

    public LongArrayList() {
        list = new long[ARRAY_INIT_SIZE];
        size = 0;
    }

    public void add(long value) {
        if (size >= list.length) {
            resize();
        }
        list[size] = value;
        size++;
    }

    public long get(int index) {
        return list[index];
    }

    public int size() {
        return size;
    }

    private void resize() {
        long[] newList = new long[list.length * 2];
        for (int i = 0; i < list.length; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }
}
