package dk.itu.map.structures;

public class EdgeList {
    private int size;
    private float[] edges;
    private final int ARRAY_INIT_SIZE = 100_000;

    public EdgeList() {
        edges = new float[ARRAY_INIT_SIZE*3];
        size = 0;
    }

    private void resize() {
        float[] newEdges = new float[edges.length*2];
        System.out.println("Resizing " + this + " to " + newEdges.length);
        for(int i = 0; i < edges.length; i++) {
            newEdges[i] = edges[i];
        }
        edges = newEdges;
    }

    public void addEdge(int coordX, int coordY, float weight) {
        if(size + 4 > edges.length) {
            resize();
        }
        edges[size] = coordX;
        edges[size+1] = coordY;
        edges[size+2] = weight;
    }

    public float[] getEdge(int index) {
        int gotten_index = index*3;
        return new float[] {edges[gotten_index], edges[gotten_index+1], edges[gotten_index+2]};
    }
}
