package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.FloatArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.WriteAbleArrayList;
import dk.itu.map.structures.HashMaps.LongIntHashMap;
import dk.itu.map.tempHashMapLongToInt;

import java.io.*;
import java.util.stream.IntStream;


public class Graph {
    protected final tempHashMapLongToInt idToIndex;
    protected WriteAbleArrayList<IntArrayList> vertexList; //List that holds the edges of each vertex
    protected final IntArrayList edgeDestinations; //List that holds the destination of each edge (Get index from vertexList)
    protected IntArrayList oldToNewVertexIndex; //List that holds the new index of each vertex
    protected final FloatArrayList edgeWeights; //List that holds the weight of each edge
    protected CoordArrayList coords; //List that holds the coordinates of each vertex

    /**
     * Constructor for the Graph class
     * Initializes the idToIndex, vertexList, edgeDestinations, edgeWeights and coords
     */
    public Graph() {
        idToIndex = new tempHashMapLongToInt();
        vertexList = new WriteAbleArrayList<>();
        edgeDestinations = new IntArrayList();
        edgeWeights = new FloatArrayList(50_000);
        coords = new CoordArrayList();
        oldToNewVertexIndex = new IntArrayList();
        //wayIDs = new LongArrayList();
    }



    /**
     * @return the number of vertices in the graph
     */
    public int size(){
        return idToIndex.size();
    }

    /**
     * @param vertex to be gotten edge list of
     * @return A list of vertices edge indexes. These indexes refer to the edgeDestinations and edgeWeights lists
     */
    public IntArrayList getEdgeList(int vertex){return vertexList.get(vertex);}

    /**
     * @param edge the index of the edges destination to be gotten
     * @return the destination of the edge
     */
    public int getDestination(int edge){
        return oldToNewVertexIndex.get(edgeDestinations.get(edge));
    }

    /**
     * @param edge the index of the edge weight to be gotten
     * @return the weight of the edge
     */
    public float getWeight(int edge){
        return edgeWeights.get(edge);
    }

    /**
     * @param index the index of the vertex coords to be gotten
     * @return the coordinates of the vertex
     */
    public float[] getCoords(int index){
        return coords.get(index);
    }

    /**
     * @param id the id of the vertex
     * @return the index of the vertex
     */
    public int idToVertexId(long id){
        return oldToNewVertexIndex.get(idToIndex.get(id));
    }

    /**
     * Loads the graph from a given folder
     * @param path the path where the graph folder is located
     * @throws IOException
     */
    public void loadFromDataPath(String path) throws IOException {
        String folderPath = path + "/graph";
        File[] files = new File[]{
                new File(folderPath + "/idToIndex.txt"),
                new File(folderPath + "/vertexList.txt"),
                new File(folderPath + "/edgeDestinations.txt"),
                new File(folderPath + "/edgeWeights.txt"),
                new File(folderPath + "/coords.txt"),
                new File(folderPath + "/oldToNewVertexIndex.txt")
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
                oldToNewVertexIndex
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

    public IntArrayList getEdges() {
        return edgeDestinations;
    }

    public FloatArrayList getWeights() {
        return edgeWeights;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Graph){
            Graph other = (Graph) obj;
            if(!idToIndex.equals(other.idToIndex)) return false;
            if(!vertexList.equals(other.vertexList)) return false;
            if(!edgeDestinations.equals(other.edgeDestinations)) return false;
            if(!edgeWeights.equals(other.edgeWeights)) return false;
            if(!coords.equals(other.coords)) return false;
            if(!oldToNewVertexIndex.equals(other.oldToNewVertexIndex)) return false;
            return true;
        }
        return false;
    }
}
