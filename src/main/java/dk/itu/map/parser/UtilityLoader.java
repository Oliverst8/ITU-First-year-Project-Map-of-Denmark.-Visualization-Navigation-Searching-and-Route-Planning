package dk.itu.map.parser;

import dk.itu.map.structures.Address;
import dk.itu.map.structures.Graph;

import java.io.IOException;

public class UtilityLoader extends Thread{
    private Graph graph;
    private String path;
    private Address address;

    public UtilityLoader(String path) {
        this.path = path + "/utilities";
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

    public Address getAddress() {
        System.out.println(address.autoComplete("a",10));
        System.out.println(address.autoComplete("a",10).size());
        return address;
    }

    private class AddressLoader extends Thread {
        private Address address;
        private String path;

        public AddressLoader(String path) {
            address = new Address();
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

        public Address getAddress() {
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