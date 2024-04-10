package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

import java.io.DataOutputStream;
import java.util.List;

public abstract class MapElement {
    private final long id;
    private final List<String> tags;
    private long[] nodeIDs;

    public MapElement(long id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public MapElement(long id, List<String> tags, LongArrayList nodeIDs) {
        this.id = id;
        this.tags = tags;
        this.nodeIDs = nodeIDs.toArray();
    }

    public long getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    abstract void stream(DataOutputStream stream) throws java.io.IOException;
    
    public abstract CoordArrayList getCoords();

    public long[] getNodeIDs() {
        return nodeIDs;
    }
}