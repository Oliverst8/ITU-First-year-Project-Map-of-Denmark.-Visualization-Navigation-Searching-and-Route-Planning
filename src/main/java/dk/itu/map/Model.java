package dk.itu.map;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.parser.FileHandler;
import dk.itu.map.structures.Way;

public class Model implements Serializable {
    List<Way> ways = new ArrayList<Way>();

    double minlat, maxlat, minlon, maxlon;

    public Model() throws XMLStreamException, IOException {
        FileHandler fileHandler = new FileHandler(new File("/home/jogge/Downloads/map.osm"));
        
        //fileHandler.load();
        ways = fileHandler.ways;
        minlat = fileHandler.minlat;
        maxlat = fileHandler.maxlat;
        minlon = fileHandler.minlon;
        maxlon = fileHandler.maxlon;
    }
}
