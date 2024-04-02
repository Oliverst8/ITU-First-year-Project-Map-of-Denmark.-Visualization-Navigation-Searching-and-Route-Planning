package dk.itu.map.parser;

import java.io.IOException;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Arrays;

import dk.itu.map.structures.CoordArrayList;
import dk.itu.map.structures.LinkedListSimple;
import dk.itu.map.structures.LinkedListSimple.Node;

class Polygon extends MapElement {
    private final int totalWays;
    private int stagedWays;

    private final List<Long> outerRef;
    private final List<Long> innerRef;

    private final Way[] stagedOuterWays;
    private final Way[] stagedInnerWays;
    private LinkedListSimple<Way> outerWays;
    private LinkedListSimple<Way> innerWays;
    
    public Polygon(long id, List<String> tags, List<Long> outerRef, List<Long> innerRef) {
        super(id, tags);

        this.stagedWays = 0;
        this.outerRef = outerRef;
        this.innerRef = innerRef;
        this.stagedOuterWays = new Way[outerRef.size()];
        this.stagedInnerWays = new Way[innerRef.size()];
        this.outerWays = new LinkedListSimple<>();
        this.innerWays = new LinkedListSimple<>();
        this.totalWays = outerRef.size() + innerRef.size();
    }

    @Override
    public void stream(DataOutputStream stream) throws IOException {
        stream.writeInt(countCoords(outerWays));
        for (Way way : outerWays) {
            for (float coord : way.getCoords().toArray()) {
                stream.writeFloat(coord);
            }
        }
        stream.writeInt(countCoords(innerWays));
        for (Way way : innerWays) {
            for (float coord : way.getCoords().toArray()) {
                stream.writeFloat(coord);
            }
        }
        stream.writeInt(getTags().size());
        for (int i = 0; i < getTags().size(); i++) {
            stream.writeUTF(getTags().get(i));
        }
    }

    @Override
    public CoordArrayList getCoords() {
        CoordArrayList coords = new CoordArrayList();

        for (Way way : outerWays) {
            coords.addAll(way.getCoords().toArray());
        }

        for (Way way : innerWays) {
            coords.addAll(way.getCoords().toArray());
        }

        return coords;
    }

    public void addWay(Way way) {
        stagedWays++;
        long id = way.getId();

        // Stage way for parsing
        if (outerRef.contains(id)) {
            stagedOuterWays[outerRef.size() - (outerRef.indexOf(id) + 1)] = way;
        } else if (innerRef.contains(id)) {
            stagedInnerWays[innerRef.size() - (innerRef.indexOf(id) + 1)] = way;
        }
         
        // if we are still missing ways, return.
        if (stagedWays != totalWays) return;
        
        // Do some redundant checks since deleted nodes can exist and not adhere to rules. example: relation/15978913
        for (int i = 0; i < stagedOuterWays.length; i++) {
            if (stagedOuterWays[i] == null) return;
        }
        for (int i = 0; i < stagedInnerWays.length; i++) {
            if (stagedInnerWays[i] == null) return;
        }
        if (stagedOuterWays.length == 0) return;

        // Order ways by their first and last nodes.
        if (stagedOuterWays.length > 0) outerWays = orderWays(stagedOuterWays);
        if (stagedInnerWays.length > 0) innerWays = orderWays(stagedInnerWays);
    }

    private int countCoords(LinkedListSimple<Way> ways) {
        int count = 0;

        if (ways == null) return count;

        for (Way way : ways) {
            count += way.getCoords().size();
        }
        return count;
    }

    private LinkedListSimple<Way> orderWays(Way[] ways) {
        LinkedListSimple<Way> orderedWays = new LinkedListSimple<Way>(Arrays.asList(ways));

        Node<Way> current = orderedWays.getFirst();
        float[] initialNode = current.getValue().getFirstCoords(); // First node in each separate shape.

        while (current.getNext() != null) {
            if (compareCoords(initialNode, current.getValue().getLastCoords())) {
                current = current.getNext();
                initialNode = current.getValue().getFirstCoords();
                continue;
            }

            Node<Way> search = current;

            while (!compareCoords(current.getValue().getLastCoords(), search.getNext().getValue().getFirstCoords())) {
                if (compareCoords(current.getValue().getLastCoords(), search.getNext().getValue().getLastCoords())) {
                    search.getNext().getValue().getCoords().reverse();
                    break;
                }

                search = search.getNext();
            }

            orderedWays.move(current, search);

            current = current.getNext();
        }
        
        return orderedWays;
    }

    private boolean compareCoords(float[] firstCoord, float[] lastCoord) {
        return Arrays.compare(firstCoord, lastCoord) == 0;
    }
}