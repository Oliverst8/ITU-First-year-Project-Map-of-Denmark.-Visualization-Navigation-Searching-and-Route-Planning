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
        for(int i = 0; i < tags.size(); i += 2) {
            switch (tags.get(i)) {
                case "highway":
                    primaryType = "highway";
                    return;
                case "leisure":
                    primaryType = "leisure";
                    return;
                case "amenity":
                    primaryType = "amenity";
                    return;
                case "building":
                    primaryType = "building";
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