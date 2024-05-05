package dk.itu.map.parser;

import dk.itu.map.structures.TernaryTree;
import dk.itu.map.App;
import dk.itu.map.structures.Graph;

import java.io.IOException;

public class UtilityLoader extends Thread{
    private Graph graph;
    private String path;
    private TernaryTree address;

    /**
     * Create an instance of a UtilityLoader
     */
    public UtilityLoader() {
        this.path = App.mapPath + "/utilities";
        graph = new Graph();
    }

    @Override
    public void run() {
        graphLoader graphLoader = new graphLoader(path);
        AddressLoader addressLoader = new AddressLoader(path);
        addressLoader.start();
        graphLoader.start();
        graph = graphLoader.getGraph();
        address = addressLoader.getAddress();
        try {
            addressLoader.join();
            graphLoader.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the graph
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Get the address
     * @return the address
     */
    public TernaryTree getAddress() {
        return address;
    }

    /**
     * A utility class to load an address
     */
    private class AddressLoader extends Thread {
        private TernaryTree address;
        private String path;

        /**
         * Create an instance of an AddressLoader
         * @param path the path to the map file
         */
        public AddressLoader(String path) {
            address = new TernaryTree();
            this.path = path + "/address.txt";
        }

        @Override
        public void run() {
            try {
                address.read(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Get the address
         * @return the address
         */
        public TernaryTree getAddress() {
            return address;
        }
    }

    /**
     * A utility class to load a graph
     */
    private class graphLoader extends Thread {
        private Graph graph;
        private String path;

        /**
         * Create an instance of a graphLoader
         * @param path the path to the map file
         */
        public graphLoader(String path) {
            this.path = path;
            graph = new Graph();
        }

        @Override
        public void run() {
            try {
                graph.loadFromDataPath(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Get the graph
         * @return the graph
         */
        public Graph getGraph() {
            return graph;
        }
    }
}