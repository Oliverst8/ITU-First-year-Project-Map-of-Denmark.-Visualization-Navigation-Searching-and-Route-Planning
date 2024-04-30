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
    private float calcWeightDistance(MapElement way, int nodeId) {
        CoordArrayList coords = way.getCoords();
        float[] coord1 = coords.get(nodeId);
        float[] coord2 = coords.get(nodeId+1);
        return distanceInKM(coord1, coord2);
    }

    private float calcWeightTime(MapElement way, int nodeId) {
        String[] speedLimitString = null;
        int speedLimit = 0;
        for(int i = 0; i < way.getTags().size(); i += 2){
            if(way.getTags().get(i).equals("maxspeed")){
                speedLimitString = way.getTags().get(i+1).split(" ");
                break;
            }
        }

        if(speedLimitString != null && !speedLimitString[0].equals("none") && !speedLimitString[0].equals("signals") && !speedLimitString[0].equals("DK:urban")){
            if(speedLimitString.length == 1) {
                speedLimit = Integer.parseInt(speedLimitString[0]);
            } else{
                speedLimit = (int) (Integer.parseInt(speedLimitString[0])*1.609344);
            }
        }

        if(speedLimitString == null || speedLimitString[0].equals("signals") || speedLimitString[0].equals("DK:urban")) {
            switch(way.getSecondaryType()){
                case "living_street", "rest_area":
                    speedLimit = 15;
                    break;
                case "residential", "secondary", "secondary_link", "tertiary", "tertiary_link", "unclassified", "road", "track", "service":
                        speedLimit = 50;
                        break;
                case "primary", "primary_link", "trunk", "trunk_link":
                    speedLimit = 80;
                    break;
                case "motorway", "motorway_link":
                    speedLimit = 130;
                    break;
            }
        } else if(speedLimitString[0].equals("none")){
            speedLimit = 130;
        }

        CoordArrayList coords = way.getCoords();
        float[] coord1 = coords.get(nodeId);
        float[] coord2 = coords.get(nodeId+1);

        return distanceInKM(coord1, coord2)/speedLimit;


    }
    private float distanceInKM(float[] coord1, float[] coord2) {
        double lonDistance = Math.abs(coord1[0] - coord2[0])*111.320*0.56;
        double latDistance = Math.abs(coord1[1] - coord2[1])*110.574;

        return (float) Math.sqrt(lonDistance*lonDistance + latDistance*latDistance);
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
        coords = newCoords;

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

            float weight = calcWeightDistance(way, i);

            distanceWeights.add(weight);
            distanceWeights.add(weight);

            weight = calcWeightTime(way, i);

            timeWeights.add(weight);
            timeWeights.add(weight);
        }

        //Maybe for all these we should add at the specific index to make sure no mistakes are made,
        // but as long as we just call these methods here, we should be okay I think
    }

    private byte setVehicleRestriction(MapElement way) {
        if(way.getId() == 37948939){
            System.out.println();
        }

        String secondayType = way.getSecondaryType();
        switch(secondayType){
            case "pedestrian":
            case "footway":
            case "steps":
            case "corridor":
                return 1;
            case "cycleway":
            case "bridleway":
            case "path":
                return 3;
            case "motorway":
            case "motorway_link":
            case "rest_area":
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
            case "service":
                return 7;
            default:
                return 0;
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
                new File(folderPath + "/vehicleRestrictions.txt"),
                new File(folderPath + "/distanceWeights.txt"),
                new File(folderPath + "/timeWeights.txt"),
                new File(folderPath + "/coords.txt"),
                new File(folderPath + "/oldToNewVertexIndex.txt"),
                //new File(folderPath + "/wayIDs.txt")
        };

        WriteAble[] instanceVariables = new WriteAble[]{
                idToIndex,
                vertexList,
                edgeDestinations,
                vehicleRestrictions,
                distanceWeights,
                timeWeights,
                coords,
                oldToNewVertexIndex,
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
