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
    private float calcWeight(MapElement way, int nodeId) {
        CoordArrayList coords = way.getCoords();
        float[] coord1 = coords.get(nodeId);
        float[] coord2 = coords.get(nodeId+1);
        return dist(coord1[0], coord1[1], coord2[0], coord2[1]);
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
    }

    /**
     * Adds vertices to the graph
     * @param vertexID the ids of the vertices to be added
     * @param coords the coordinates of the vertices to be added
     */
    private void addVertices(long[] vertexID, CoordArrayList coords) {
        for (int i = 0; i < vertexID.length; i++) {
            if(!idToIndex.containsKey(vertexID[i])){
                int index = vertexList.size();
                idToIndex.put(vertexID[i], index);
                vertexList.add(new IntArrayList(2));
                float[] coord = coords.get(i);
                this.coords.add(coord[0]);
                this.coords.add(coord[1]);
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
        long[] nodeIDs = way.getNodeIDs();

        byte vehicleRestriction = setVehicleRestriction(way);

        for(int i = 0; i < nodeIDs.length-1; i++){
            int node1 = idToIndex.get(nodeIDs[i]);
            int node2 = idToIndex.get(nodeIDs[(i+1)]);

            int edgeNumberFrom1 = edgeDestinations.size();
            int edgeNumberFrom2 = edgeNumberFrom1+1;

            vertexList.get(node1).add(edgeNumberFrom1);
            vertexList.get(node2).add(edgeNumberFrom2);

            edgeDestinations.add(node2);
            edgeDestinations.add(node1);

            vehicleRestrictions.add(vehicleRestriction);
            vehicleRestrictions.add(vehicleRestriction);

            float weight = calcWeight(way, i);

            edgeWeights.add(weight);
            edgeWeights.add(weight);

        }

        //Maybe for all these we should add at the specific index to make sure no mistakes are made,
        // but as long as we just call these methods here, we should be okay I think
    }

    private byte setVehicleRestriction(MapElement way) {
        List<String> tags = way.getTags();
        String secondayType = null;
        for(int i = 0; i < tags.size(); i += 2){
            if(tags.get(i).equals("highway")){
                secondayType = tags.get(i+1);
                break;
            }
        }
        switch(secondayType){
            case "escape":
            case "raceway":
            case "busway":
            case "busguideway":
            case "proposed":
            case "construction":
            case "service":
                return 0;
            case "pedestrian":
            case "footway":
            case "steps":
            case "corridor":
                return 1;
            case "cycleway":
                return 2;
            case "bridleway":
            case "path":
                return 3;
            case "motorway":
            case "motorway_link":
                return 4;
            case "trunk":
            case "trunk_link":
            case "primary":
            case "primary_link":
            case "secondary":
            case "secondary_link":
            case "tertiary":
            case "tertiary_link":
            case "unclassified":
            case "residential":
            case "living_street":
            case "road":
            case "track":
                return 7;
            default:
                throw new RuntimeException("Unknown highway type: " + secondayType + " in way: " + way.getId());
        }
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
