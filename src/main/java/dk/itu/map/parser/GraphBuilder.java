package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.WriteAbleArrayList;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.TwoDTree;
import dk.itu.map.structures.TwoDTreeBuilder;
import dk.itu.map.structures.WriteAble;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class GraphBuilder extends Graph implements Runnable {

    private final List<MapElement> ways; //List of ways to be added to the graph
    private boolean running = true; //Boolean to keep the thread running

    /**
     * Constructor for the GraphBuilder class
     * Initializes the ways list
     */
    public GraphBuilder(){
        super();
        ways = Collections.synchronizedList(new LinkedList<>());
    }

    /**
     * Calculates the distance between two points
     * @param x1 the x coordinate of the first point
     * @param y1 the y coordinate of the first point
     * @param x2 the x coordinate of the second point
     * @param y2 the y coordinate of the second point
     * @return the distance between the two points
     */
    private float dist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    //Vi kender ikke enheden her, men det er måske givet i bredde- (eller længde-?) grader?
    // Skal måske konverteres, men det er vel ligemeget egentlig, (indtil vi konvertere til tid?)
    //ADVARSEL FUNGERER IKKE
    /**
     * Calculates the weight of a way
     * @param way the way to calculate the weight of
     * @return the weight of the way
     */
    private float calcWeight(MapElement way, int firstNode) {
        CoordArrayList coords = way.getCoords();
        float[] coord1 = coords.get(firstNode);
        float[] coord2 = coords.get(firstNode+1);
        return dist(coord1[0], coord1[1], coord2[0], coord2[1]);
        //return (float) Math.sqrt(Math.pow(coords.get(firstNode) - coords.get(firstNode+2), 2) + Math.pow(coords.get(firstNode+1) - coords.get(firstNode+3), 2));
    }

    /**
     * Parses ways added to the graph
     */
    public void run() {
        while(running || !ways.isEmpty()){
            if(running && ways.size() < 100_000){
                try {
                    Thread.sleep(30);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(!ways.isEmpty()){
                MapElement way = ways.remove(0);
                addVertices(way.getNodeIDs(), way.getCoords());
                addEdge(way);
            }
        }
        TwoDTreeBuilder treeBuilder = new TwoDTreeBuilder(coords);
        treeBuilder.build();
        int[] treeIndexes = treeBuilder.getTree();
        sortCoordsAndIndexes(treeIndexes);
    }

    private void sortCoordsAndIndexes(int[] newVertices) {
        oldToNewVertexIndex = new IntArrayList(vertexList.size());


        WriteAbleArrayList<IntArrayList> newVertexList = new WriteAbleArrayList<>(newVertices.length);
        TwoDTree newCoords = new TwoDTree(newVertices.length);

        for(int i = 0; i < newVertices.length; i++){

            if(newVertices[i] == -1){
                newVertexList.add(new IntArrayList(0));
                newCoords.add(new float[]{-1,-1});
            } else{
                oldToNewVertexIndex.set(newVertices[i], i); //Not Built corŕectly I think, way to many 0s
                newVertexList.add(vertexList.get(newVertices[i]));
                newCoords.add(coords.get(newVertices[i]));
            }
        }
        vertexList = newVertexList;
        getDifference(newVertices, idToIndex.getValues());
        getDifference(idToIndex.getValues(), newVertices);
        coords = newCoords;

    }

    public static HashSet<Integer> getIndexOf(int[] array, int value){
        HashSet<Integer> indexes = new HashSet<>();
        for(int i = 0; i < array.length; i++){
            if(array[i] == value) indexes.add(i);
        }
        return indexes;
    }

    public static HashSet<Integer> getDifference(int[] array1, int[] array2){
        HashSet<Integer> set1 = new HashSet<>();
        HashSet<Integer> set2 = new HashSet<>();
        for(int i = 0; i < array1.length; i++){
            set1.add(array1[i]);
        }
        for(int i = 0; i < array2.length; i++){
            set2.add(array2[i]);
        }

        set1.removeAll(set2);

        return set1;
    }

    public static HashSet<Integer> getDuplicates(int[] array){
        HashSet<Integer> normalSet = new HashSet<>();
        HashSet<Integer> duplicates = new HashSet<>();
        for(int i = 0; i < array.length; i++){
            if(!normalSet.add(array[i])){
                duplicates.add(array[i]);
            }
        }
        return duplicates;
    }

    /**
     * Adds vertices to the graph
     * @param vertexID the ids of the vertices to be added
     * @param coords the coordinates of the vertices to be added
     */
    private void addVertices(long[] vertexID, CoordArrayList coords) {
        for (int i = 0; i < vertexID.length; i++) {
            if(!idToIndex.containsKey(vertexID[i])){
                if(vertexID[i] == 11367582572l){
                    System.out.println("Found the node");
                }
                int index = vertexList.size();
                idToIndex.put(vertexID[i], index);
                vertexList.add(new IntArrayList(2));
                float[] coord = coords.get(i);
                //if(coord[0] == 55.750755f && coord[1] == 12.555152f){
                //    System.out.println("Found the node");
                //}
                this.coords.add(coord);
                //coords.add();
                //Here we should add coords, but I dont know how to get them currently, as I should either give this method a way,
                // or look at the LongFloatArrayHashMap in FileHandler, or just give them as arguments

                //Here we could add node ids to an nodeIDArray, if we want them later
            }
        }
    }

    /**
     * Adds an edge to the graph
     * @param way the way to add an edge from
     */
    private void addEdge(MapElement way) {
        /*
        int node1 = idToIndex.get(way.getNodeIDs()[0]);
        int node2 = idToIndex.get(way.getNodeIDs()[1]);
        int edgeNumberFrom1 = edgeDestinations.size();
        int edgeNumberFrom2 = edgeNumberFrom1+1;
        //Weight should be differentiated later, but currently nothing can change it, so ill keep it in one variable
        float edgeWeight = calcWeight(way);

        vertexList.get(node1).add(edgeNumberFrom1);
        vertexList.get(node2).add(edgeNumberFrom2);

        edgeDestinations.add(node2);
        edgeDestinations.add(node1);

        edgeWeights.add(edgeWeight);
        edgeWeights.add(edgeWeight);

        wayIDs.add(way.getId());
        wayIDs.add(way.getId());
        */

        //This is the new version of the above code, which should be more efficient
        long[] nodeIDs = way.getNodeIDs();

        for(int i = 0; i < nodeIDs.length-1; i++){
            int node1 = idToIndex.get(nodeIDs[i]);
            int node2 = idToIndex.get(nodeIDs[(i+1)]);

            int edgeNumberFrom1 = edgeDestinations.size();
            int edgeNumberFrom2 = edgeNumberFrom1+1;

            vertexList.get(node1).add(edgeNumberFrom1);
            vertexList.get(node2).add(edgeNumberFrom2);

            edgeDestinations.add(node2);
            edgeDestinations.add(node1);


            float weight = calcWeight(way, i);

            edgeWeights.add(weight);
            edgeWeights.add(weight);

        }

        //Maybe for all these we should add at the specific index to make sure no mistakes are made,
        // but as long as we just call these methods here, we should be okay I think
    }

    /**
     * Adds a way to the graph, to be parsed by run()
     * @param way the way to be added
     */
    public void addWay(MapElement way) {
        ways.add(way);
    }

    /**
     * Stops the thread
     */
    public void stop(){
        running = false;
    }

    /**
     * Writes the graph to a file
     * @param path the path to write the graph to (Do not include /graph in the path)
     */
    public void writeToFile(String path){
        String folderPath = path + "/graph";
        (new File(folderPath)).mkdirs();

        File[] files = new File[]{
                new File(folderPath + "/idToIndex.txt"),
                new File(folderPath + "/vertexList.txt"),
                new File(folderPath + "/edgeDestinations.txt"),
                new File(folderPath + "/edgeWeights.txt"),
                new File(folderPath + "/coords.txt"),
                new File(folderPath + "/oldToNewVertexIndex.txt")
                //new File(folderPath + "/wayIDs.txt")
        };

        WriteAble[] instanceVariables = new WriteAble[]{
                idToIndex,
                vertexList,
                edgeDestinations,
                edgeWeights,
                coords,
                oldToNewVertexIndex
                //wayIDs
        };

        IntStream.range(0, instanceVariables.length).parallel().forEach(i -> {
            try {
                instanceVariables[i].write(files[i].getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
