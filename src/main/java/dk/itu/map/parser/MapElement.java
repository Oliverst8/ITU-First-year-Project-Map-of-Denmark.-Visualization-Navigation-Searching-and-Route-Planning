package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

import java.io.DataOutputStream;
import java.util.List;

public abstract class MapElement {
    private final long id;
    private final List<String> tags;
    protected String primaryType;
    protected String secondaryType;
    private long[] nodeIDs;

    /**
     * Creates a new MapElement
     * @param id The id of the element
     * @param tags The tags of the element
     */
    public MapElement(long id, List<String> tags) {
        this.id = id;
        this.tags = tags;
        setTypes();
    }

    /**
     * Creates a new MapElement
     * @param id The id of the element
     * @param tags The tags of the element
     * @param nodeIDs The node IDs of the element
     */
    public MapElement(long id, List<String> tags, LongArrayList nodeIDs) {
        this.id = id;
        this.tags = tags;
        this.nodeIDs = nodeIDs.toArray();
        setTypes();
    }

    /**
     * Sets the types of the element
     */
    private void setTypes() {
        for(int i = 0; i < tags.size(); i += 2) {
            switch (tags.get(i)) {
                case "highway":
                    primaryType = "highway";
                    secondaryType = tags.get(i + 1);
                    return;
                case "leisure":
                    primaryType = "leisure";
                    secondaryType = tags.get(i + 1);
                    return;
                case "amenity":
                    primaryType = "amenity";
                    secondaryType = tags.get(i + 1);
                    return;
                case "building":
                    primaryType = "building";
                    secondaryType = tags.get(i + 1);
                    return;
                case "aeroway":
                    primaryType = "aeroway";
                    secondaryType = tags.get(i + 1);
                    return;
                case "landuse":
                    primaryType = "landuse";
                    secondaryType = tags.get(i + 1);
                    return;
                case "natural":
                    primaryType = "natural";
                    secondaryType = tags.get(i + 1);
                    return;
                case "place":
                    primaryType = "place";
                    secondaryType = tags.get(i + 1);
                    return;
                default:
                    primaryType = "null";
                    secondaryType = "null";
                    break;
            }
        }
    }

    /**
     * @return The id of the element
     */
    public long getId() {
        return id;
    }

    /**
     * @return The tags of the element
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Writes the element to a stream
     * @param stream The stream to write to
     * @throws java.io.IOException
     */
    abstract void stream(DataOutputStream stream) throws java.io.IOException;
    
    /**
     * @return The coordinates of the element
     */
    public abstract CoordArrayList getCoords();

    /**
     * @return The node IDs of the element
     */
    public long[] getNodeIDs() {
        return nodeIDs;
    }

    /**
     * @return The primary type of the element
     */
    public String getPrimaryType() {
        return primaryType;
    }

    /**
     * @return The secondary type of the element
     */
    public String getSecondaryType() {
        return secondaryType;
    }
}