package itu.map;

import dk.itu.map.fxml.models.MapModel;
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

        assertEquals(way.getOuterCoords(), outerCoords);
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

    @Test
    void testSetColorsNoFill() {
        way = new DrawableWay(new CoordArrayList(coords), 0, "aeroway", "runway");
        MapModel mapModel = new MapModel("Internal");


        way.draw(gc, 1, 1, mapModel.theme);

        assertEquals(gc.getStroke().toString(), "0xf3f6ffff");
        assertEquals(gc.getLineWidth(), 0.0025499999430030583);
        assertEquals(gc.getFill().toString(), "0x000000ff");

    }

    @Test
    void testSetColorsFill() {
        way = new DrawableWay(new CoordArrayList(coords), 0, "natural", "beach");
        MapModel mapModel = new MapModel("Internal");

        way.draw(gc, 1, 1, mapModel.theme);

        assertEquals(gc.getStroke().toString(), "0x000000ff");
        assertEquals(gc.getLineWidth(), 5.0999998711631635E-5);
        assertEquals(gc.getFill().toString(), "0xf7eccfff");

    }
}
