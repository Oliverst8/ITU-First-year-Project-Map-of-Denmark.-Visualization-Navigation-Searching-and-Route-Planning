package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;

import java.io.DataOutputStream;
import java.util.List;

abstract class MapElement {
    private final long id;
    private final List<String> tags;
    protected String primaryType;
    public MapElement(long id, List<String> tags) {
        this.id = id;
        this.tags = tags;
        for(String tag : tags) {
            switch (tag) {
                case "highway":
                    primaryType = "highway";
                    return;
                case "aeroway":
                    primaryType = "aeroway";
                    return;
                case "landuse":
                    primaryType = "landuse";
                    return;
                case "natural":
                    primaryType = "natural";
                    return;
                case "place":
                    primaryType = "place";
                    return;
                default:
                    // Handle the case where the tag is not any of the above
                    primaryType = "null";
                    break;
            }
        }
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