package dk.itu.map.parser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

public class Way extends MapElement {
    private CoordArrayList coords;

    /**
     * Create an instance of a Way
     * @param id the id of the way
     * @param tags the tags of the way
     * @param coords the coordinates of the nodes in the way
     * @param nodeIDs the node ids of the way
     */
    public Way(long id, List<String> tags, CoordArrayList coords, LongArrayList nodeIDs) {
        super(id, tags, nodeIDs);

        this.coords = coords;
    }

    @Override
    public void stream(DataOutputStream stream) throws IOException {
        stream.writeLong(getId());
        stream.writeInt(coords.size());
        for (int i = 0; i < coords.size(); i++) {
            float[] coord = coords.get(i);
            stream.writeFloat(coord[0]);
            stream.writeFloat(coord[1]);
        }
        stream.writeInt(0); // Ways dont have inner coords
        stream.writeUTF(primaryType);
        stream.writeUTF(secondaryType);
    }

    @Override
    public CoordArrayList getCoords() {
        return coords;
    }

    /**
     * Get the first coordinates of the way
     * @return the first coordinates of the way
     */
    public float[] getFirstCoords() {
        return coords.get(0);
    }

    /**
     * Get the last coordinates of the way
     * @return the last coordinates of the way
     */
    public float[] getLastCoords() {
        return coords.get(-1);
    }
}