package dk.itu.map.fxml;

import dk.itu.map.structures.Way;
import dk.itu.map.parser.OSMParser;
import dk.itu.map.parser.ChunkLoader;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.IOException;

import java.util.*;

import javax.xml.stream.XMLStreamException;

public class Model {
    // The path to the data folder
    private final String dataPath;

    /**
     * Constructor for the Model class
     * Initializes the dataPath and the chunkLayers
     */
    public Model() {
        dataPath = "maps/";
    }

}
