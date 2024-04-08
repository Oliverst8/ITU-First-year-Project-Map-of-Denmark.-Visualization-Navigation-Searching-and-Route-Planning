package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.WriteAble;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class GraphBuilder extends Graph implements Runnable {

    private final List<MapElement> ways;
    private boolean running = true;
    public GraphBuilder(){
        super();
        ways = Collections.synchronizedList(new LinkedList<>());
    }

    private float dist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    //Vi kender ikke enheden her, men det er måske givet i bredde- (eller længde-?) grader?
    // Skal måske konverteres, men det er vel ligemeget egentlig, (indtil vi konvertere til tid?)
    private float calcWeight(MapElement way, int firstNode) {
        CoordArrayList coords = way.getCoords();
        return dist(coords.get(firstNode), coords.get(firstNode+1), coords.get(firstNode+2), coords.get(firstNode+3));
        //return (float) Math.sqrt(Math.pow(coords.get(firstNode) - coords.get(firstNode+2), 2) + Math.pow(coords.get(firstNode+1) - coords.get(firstNode+3), 2));
    }

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
    }

    private void addVertices(long[] vertexID, CoordArrayList coords) {
        for (int i = 0; i < vertexID.length; i++) {
            if(!idToIndex.containsKey(vertexID[i])){
                int index = vertexList.size();
                idToIndex.put(vertexID[i], index);
                vertexList.add(new IntArrayList(2));
                this.coords.add(coords.get(i*2));
                this.coords.add(coords.get(i*2+1));
                //coords.add();
                //Here we should add coords, but I dont know how to get them currently, as I should either give this method a way,
                // or look at the LongFloatArrayHashMap in FileHandler, or just give them as arguments

                //Here we could add node ids to an nodeIDArray, if we want them later
            }
        }
    }

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

    public void addWay(MapElement way) {
        ways.add(way);
    }

    public void stop(){
        running = false;
    }

    public void writeToFile(String path){
        String folderPath = path + "/graph";
        (new File(folderPath)).mkdirs();

        File[] files = new File[]{
                new File(folderPath + "/idToIndex.txt"),
                new File(folderPath + "/vertexList.txt"),
                new File(folderPath + "/edgeDestinations.txt"),
                new File(folderPath + "/edgeWeights.txt"),
                new File(folderPath + "/coords.txt"),
                //new File(folderPath + "/wayIDs.txt")
        };

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
                instanceVariables[i].write(files[i].getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
