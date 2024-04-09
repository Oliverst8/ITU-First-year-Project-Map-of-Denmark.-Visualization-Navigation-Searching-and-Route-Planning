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
        stream.writeLong(getId());
        stream.writeInt(coords.size());
        for (int i = 0; i < coords.size(); i++) {
            stream.writeFloat(coords.get(i));
        }
        stream.writeInt(0); // Ways dont have inner coords
        stream.writeInt(getTags().size());
        for (int i = 0; i < getTags().size(); i++) {
            stream.writeUTF(getTags().get(i));
        }
        stream.writeUTF(primaryType);
    }

    @Override
    public CoordArrayList getCoords() {
        return coords;
    }

    public float[] getFirstCoords() {
        return new float[]{coords.get(0), coords.get(1)};
    }

    public float[] getLastCoords() {
        return new float[]{coords.get(-2), coords.get(-1)};
    }
}