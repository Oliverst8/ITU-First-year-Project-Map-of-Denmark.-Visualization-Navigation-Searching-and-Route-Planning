package dk.itu.map.parser;

import java.io.IOException;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Arrays;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.SimpleLinkedList;
import dk.itu.map.structures.SimpleLinkedList.Node;

public class Polygon extends MapElement {
    private final int totalWays;
    private int stagedWays;

    private final List<Long> outerRef;
    private final List<Long> innerRef;

    private final Way[] stagedOuterWays;
    private final Way[] stagedInnerWays;
    private SimpleLinkedList<Way> outerWays;
    private SimpleLinkedList<Way> innerWays;
    
    /**
     * Create an instance of a Polygon
     * @param id the id of the polygon
     * @param tags the tags of the polygon
     * @param outerRef the outer references in the polygon
     * @param innerRef the inner references in the polygon
     */
    public Polygon(long id, List<String> tags, List<Long> outerRef, List<Long> innerRef) {
        super(id, tags);

        this.stagedWays = 0;
        this.outerRef = outerRef;
        this.innerRef = innerRef;
        this.stagedOuterWays = new Way[outerRef.size()];
        this.stagedInnerWays = new Way[innerRef.size()];
        this.outerWays = new SimpleLinkedList<>();
        this.innerWays = new SimpleLinkedList<>();
        this.totalWays = outerRef.size() + innerRef.size();
    }

    @Override
    public void stream(DataOutputStream stream) throws IOException {
        stream.writeLong(getId());
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
        stream.writeUTF(primaryType);
        stream.writeUTF(secondaryType);
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

    /**
     * Add a way to the polygon
     * @param way the way to add
     */
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

    /**
     * Count the number of coordinates in a list of ways
     * @param ways
     * @return
     */
    private int countCoords(SimpleLinkedList<Way> ways) {
        int count = 0;

        if (ways == null) return count;

        for (Way way : ways) {
            count += way.getCoords().size();
        }
        return count;
    }

    /**
     * Order ways by their first and last nodes.
     * @param ways the ways to order
     * @return the ordered ways
     */
    private SimpleLinkedList<Way> orderWays(Way[] ways) {
        SimpleLinkedList<Way> orderedWays = new SimpleLinkedList<Way>(Arrays.asList(ways));

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

    /**
     * Compare two coordinates
     * @param firstCoord
     * @param lastCoord
     * @return true if the coordinates are equal, false otherwise
     */
    private boolean compareCoords(float[] firstCoord, float[] lastCoord) {
        return Arrays.compare(firstCoord, lastCoord) == 0;
    }
}