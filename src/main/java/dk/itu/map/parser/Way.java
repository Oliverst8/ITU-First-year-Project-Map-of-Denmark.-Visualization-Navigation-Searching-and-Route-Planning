package dk.itu.map.parser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

public class Way extends MapElement {
    private CoordArrayList coords;

    public Way(long id, List<String> tags, CoordArrayList coords, LongArrayList nodeIDs) {
        super(id, tags, nodeIDs);

        this.coords = coords;
    }

    @Override
    public void stream(DataOutputStream stream) throws IOException {
        stream.writeInt(coords.size());
        for (int i = 0; i < coords.size(); i++) {
            float[] coord = coords.get(i);
            stream.writeFloat(coord[0]);
            stream.writeFloat(coord[1]);
        }
        stream.writeInt(0); // Ways dont have inner coords
        stream.writeInt(getTags().size());
        for (int i = 0; i < getTags().size(); i++) {
            stream.writeUTF(getTags().get(i));
        }
    }

    @Override
    public CoordArrayList getCoords() {
        return coords;
    }

    public float[] getFirstCoords() {
        return coords.get(0);
    }

    public float[] getLastCoords() {
        return coords.get(-1);
    }
}