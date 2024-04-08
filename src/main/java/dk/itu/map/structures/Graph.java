package dk.itu.map.structures;

import dk.itu.map.parser.MapElement;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;


public class Graph {
    protected final LongIntHashMap idToIndex;
    protected final WriteAbleArrayList<IntArrayList> vertexList; //List that holds the edges of each vertex
    protected final IntArrayList edgeDestinations; //List that holds the destination of each edge (Get index from vertexList)
    protected final FloatArrayList edgeWeights; //List that holds the weight of each edge
    protected final FloatArrayList coords; //List that holds the coordinates of each vertex

    /**
     * Constructor for the Graph class
     * Initializes the idToIndex, vertexList, edgeDestinations, edgeWeights and coords
     */
    public Graph() {
        idToIndex = new LongIntHashMap();
        vertexList = new WriteAbleArrayList<>();
        edgeDestinations = new IntArrayList();
        edgeWeights = new FloatArrayList(50_000);
        coords = new FloatArrayList();
        //wayIDs = new LongArrayList();
    }

    public int size(){
        return idToIndex.size();
    }

    //This test needs refactoring, since it is now split up in new Arrays
    public IntArrayList getEdges() {
        return edgeDestinations;
    }

    public LongArrayList getIds() {
        //return wayIDs;
        throw new UnsupportedOperationException();
    }
    public IntArrayList getEdgeList(int vertex){return vertexList.get(vertex);}

    public int getDestination(int edge){
        return edgeDestinations.get(edge);
    }
    public float getWeight(int edge){
        return edgeWeights.get(edge);
    }

    public FloatArrayList getWeights() {
        return edgeWeights;
    }

    public float[] getCoords(int index){
        return new float[]{coords.get(index*2), coords.get(index*2+1)};
    }

    public int idToVertexId(long id){
        return idToIndex.get(id);
    }

    public void loadFromDataPath(String path) throws IOException {
        String folderPath = path + "/graph";
        File[] files = new File[]{
                new File(folderPath + "/idToIndex.txt"),
                new File(folderPath + "/vertexList.txt"),
                new File(folderPath + "/edgeDestinations.txt"),
                new File(folderPath + "/edgeWeights.txt"),
                new File(folderPath + "/coords.txt"),
                //new File(folderPath + "/wayIDs.txt")
        };

        DataInputStream[] streams = new DataInputStream[files.length];

        for(int i = 0; i < files.length; i++){
            try {
                streams[i] = new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(files[i].getAbsolutePath())
                        )
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try{
            DataInputStream stream = new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(files[1].getAbsolutePath())
                    )
            );
            int sizeOfIdToIndex = stream.readInt();
            for(int i = 0; i < sizeOfIdToIndex; i++){
                vertexList.add(new IntArrayList(0));
            }
            vertexList.trimToSize();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        WriteAble[] instanceVariables = new WriteAble[]{
                idToIndex,
                vertexList,
                edgeDestinations,
                edgeWeights,
                coords,
                //wayIDs
        };

        IntStream.range(0, instanceVariables.length).parallel().forEach(i -> {
            try {
                instanceVariables[i].read(streams[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        for(DataInputStream stream : streams){
            stream.close();
        }

    }
}
