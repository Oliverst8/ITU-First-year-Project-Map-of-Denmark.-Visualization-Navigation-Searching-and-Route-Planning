package dk.itu.map;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.parser.FileHandler;

public class Model implements Serializable {
    List<Way> ways = new ArrayList<Way>();

    double minlat, maxlat, minlon, maxlon;

    public Model(FileHandler fileHandler) throws XMLStreamException, IOException {
        fileHandler.load();
        ways = fileHandler.ways;
        minlat = fileHandler.minlat;
        maxlat = fileHandler.maxlat;
        minlon = fileHandler.minlon;
        maxlon = fileHandler.maxlon;
    }
}
