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
        DrawableWay way = new DrawableWay(outerCoords, innerCoords, 0, "", "");

        assertArrayEquals(way.getOuterCoords(), outerCoords.toArray());
    }

    @Test
    void testWayEqualsNotAWay() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        DrawableWay way = new DrawableWay(outerCoords, innerCoords, 0, "", "");

        assertNotEquals(way, new Object());
    }

    @Test
    void testWayEqualsSameWay() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        DrawableWay way = new DrawableWay(outerCoords, innerCoords, 0, "", "");

        assertEquals(way, way);
    }

    @Test
    void testIsRelationExpectsFalse() {
        way = new DrawableWay(new CoordArrayList(coords), 0, "", "");
        assertFalse(way.isRelation());
    }

    @Test
    void testIsRelationExpectsTrue() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), 0, "", "");
        assertTrue(way.isRelation());
    }
}
