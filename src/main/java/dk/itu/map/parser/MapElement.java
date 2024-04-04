package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;

import java.io.DataOutputStream;
import java.util.List;

abstract class MapElement {
    private final long id;
    private final List<String> tags;

    public MapElement(long id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    abstract void stream(DataOutputStream stream) throws java.io.IOException;
    
    abstract CoordArrayList getCoords();
}