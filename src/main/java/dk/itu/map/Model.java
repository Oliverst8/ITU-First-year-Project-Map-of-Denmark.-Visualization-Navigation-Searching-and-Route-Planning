package dk.itu.map;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.parser.FileHandler;
import dk.itu.map.structures.Way;

public class Model {
    List<Way> ways = new ArrayList<Way>();

    double minlat, maxlat, minlon, maxlon;

    public Model() {}

    void parseMap(String path) {
        FileHandler fileHandler = new FileHandler(new File("/home/jogge/Downloads/map.osm"));

        try {
            fileHandler.load();

            ways = fileHandler.ways;
            minlat = fileHandler.minlat;
            maxlat = fileHandler.maxlat;
            minlon = fileHandler.minlon;
            maxlon = fileHandler.maxlon;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}