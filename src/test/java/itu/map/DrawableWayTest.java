package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.DrawableWay;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class DrawableWayTest {

    DrawableWay way;
    Canvas canvas;
    GraphicsContext gc;
    float[] coords;

    @BeforeEach
    void setUp() {
        coords = new float[]{1, 2, 3, 4, 5, 6};
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
    }

    @Test
    void testConstrutor() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        String[] tags = new String[2];
        DrawableWay way = new DrawableWay(innerCoords, outerCoords, tags);

        assertArrayEquals(way.getCoords(), outerCoords.toArray());
        assertArrayEquals(way.getTags(), tags);
    }

    @Test
    void testWayEqualsNotAWay() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        String[] tags = new String[2];
        DrawableWay way = new DrawableWay(innerCoords, outerCoords, tags);

        assertNotEquals(way, new Object());
    }

    @Test
    void testWayEqualsSameWay() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        String[] tags = new String[2];
        DrawableWay way = new DrawableWay(innerCoords, outerCoords, tags);

        assertEquals(way, way);
    }

    @Test
    void testToString() {
        float[] coords = {1, 2, 3, 4, 5, 6};
        CoordArrayList innerCoords = new CoordArrayList(coords);
        CoordArrayList outerCoords = new CoordArrayList(coords);
        String[] tags = new String[2];
        String expected = "Nodes:\n3\n1.0 2.0\n3.0 4.0\n5.0 6.0\nInner nodes:\n3\n1.0 2.0\n3.0 4.0\n5.0 6.0\nTags:\n2\nnull null\n";
        assertEquals(expected, new DrawableWay(innerCoords, outerCoords, tags).toString());
    }

    @Test
    void testDraw() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"motorway"});
        way.draw(gc, 100);
    }

    @Test
    void testDrawNoCoords() {
        way = new DrawableWay(new CoordArrayList(), new CoordArrayList(), new String[]{"motorway"});
        way.draw(gc, 100);
    }

    @Test
    void testDrawIsIsland() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"island"});
        way.draw(gc, 100);
    }

    @Test
    void testIsRelationExpectsFalse() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(), new String[]{"motorway"});
        assertFalse(way.isRelation());
    }

    @Test
    void testIsRelationExpectsTrue() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"type", "relation"});
        assertTrue(way.isRelation());
    }

    @Test
    void testDrawShouldFill() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"beach"});
        way.draw(gc, 100);
    }

}
