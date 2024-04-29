package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;

public class TwoDTree extends CoordArrayList {

    public TwoDTree() {
        super();
    }

    public TwoDTree(int length) {
        super(length);
    }

    public TwoDTree(float[] array){
        super(array);
    }

    private boolean shouldGoRight(float[] self, float[] other, int axis) {
        int cmp = Float.compare(self[axis], other[axis]);
        return cmp >= 0;
    }

    private float distance(float[] self, float[] other) {
        return (float) Math.sqrt(Math.pow(self[0] - other[0], 2) + Math.pow(self[1] - other[1], 2));
    }

    private float distToLine(float[] self, float[] other, int axis) {
        return Math.abs(self[axis] - other[axis]);
    }

    private int validatePosition(int index) {
        if(index >= size() || get(index)[0] == -1) return -1;
        return index;
    }

    private int getLeftChild(int i) {
        return validatePosition(2 * i + 1);
    }

    private int getRightChild(int i) {
        return validatePosition(2 * i + 2);
    }

    public int nearestNeighbour(float[] point, int vehicleCode, Graph graph) {
        return nearest(point, 0, 0, 0, vehicleCode, graph);
    }

    private int nearest(float[] goal, int i, int best, int axis, int vehicleCode, Graph graph) {

        if(goal == get(i)) return i;

        boolean shouldGoRight = shouldGoRight(goal, get(i), axis);

        int rightChild = getRightChild(i);
        int leftChild = getLeftChild(i);

        int child = shouldGoRight ? rightChild : leftChild;

        if(child == -1){
            child = shouldGoRight ? leftChild : rightChild;
            if(child == -1) return i;
        }

        int nextCheck = nearest(goal, child, best, (axis + 1) % 2, vehicleCode, graph);
        IntArrayList edges = graph.getEdgeList(nextCheck);
        boolean canBeUsed = false;
        for(int j = 0; j < edges.size(); j++){
            int edge = edges.get(j);
            if((graph.getVehicleRestrictions().get(edge) & vehicleCode) > 0 ){
                canBeUsed = true;
            }
        }
        if(canBeUsed){
            best = distance(goal, get(best)) > distance(goal, get(nextCheck)) ? nextCheck : best;
        }

        if(child == rightChild){
            if(leftChild == -1) return best;
            float rPrime = distToLine(goal, get(i), axis);
            if(distance(goal, get(best)) > rPrime) best = nearest(goal, leftChild, best, (axis + 1) % 2, vehicleCode, graph);
        } else {
            if(rightChild == -1) return best;
            float rPrime = distToLine(goal, get(i), axis);
            if(distance(goal, get(best)) > rPrime) best = nearest(goal, rightChild, best, (axis + 1) % 2, vehicleCode, graph);
        }
        return best;
    }


/*
    public float[] nearestNeighbour(float[] coords) {
        Queue<Integer> queue = new LinkedList<>();
        float r = distance(coords, get(0));
        float rPrime = distToLine(coords, get(0), 0);
        int nearest = 0;
        int axis = 0;

        queue.add(nearest);


        while(!queue.isEmpty()) {
            int i = queue.poll();
            boolean goRight; // = goRight(coords, get(i), axis);

            int leftChild = getLeftChild(i);
            int rightChild = getRightChild(i);

            if(leftChild == -1 && rightChild == -1) {
                continue;
            } else if(leftChild == -1) {
                goRight = true;
            } else if(rightChild == -1) {
                goRight = false;
            } else {
                goRight = shouldGoRight(coords, get(leftChild), axis);
            }

            if(goRight){


            } else {

            }

            axis = (axis + 1) % 2;
        }



        throw new UnsupportedOperationException("Not implemented yet");
    }
 */
    public int nearestNeighbour(float x, float y, int vehicleCode, Graph graph) {
        return nearestNeighbour(new float[]{x,y}, vehicleCode, graph);
    }
}
