package dk.itu.map.parser;

import dk.itu.map.App;
import dk.itu.map.structures.Graph;

import java.io.IOException;

public class UtilityLoader extends Thread{
    private Graph graph;
    private String path;

    public UtilityLoader(String name) {
        this.path = App.dataPath + name + "/utilities";
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