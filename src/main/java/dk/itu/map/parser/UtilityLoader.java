package dk.itu.map.parser;

import dk.itu.map.structures.TernaryTree;
import dk.itu.map.App;
import dk.itu.map.structures.Graph;

import java.io.IOException;

public class UtilityLoader extends Thread{
    private Graph graph;
    private String path;
    private TernaryTree address;

    public UtilityLoader(String name) {
        this.path = App.DATA_PATH + name + "/utilities";
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

    public Graph getGraph() {
        return graph;
    }

    public TernaryTree getAddress() {
        //System.out.println(address.autoComplete("a",10));
//        Map<String[], TernaryTree.AddressNode> result = address.autoComplete("Andreas Road",10);
//        for (Map.Entry<String[], TernaryTree.AddressNode> entry : result.entrySet()) {
//            System.out.println(entry.getKey()[0] + ", " + entry.getKey()[1]);
//        }
//        System.out.println(address.autoComplete("a",10).size());
        return address;
    }

    private class AddressLoader extends Thread {
        private TernaryTree address;
        private String path;

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

        public TernaryTree getAddress() {
            return address;
        }
    }

    private class graphLoader extends Thread {
        private Graph graph;
        private String path;

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

        public Graph getGraph() {
            return graph;
        }
    }

}