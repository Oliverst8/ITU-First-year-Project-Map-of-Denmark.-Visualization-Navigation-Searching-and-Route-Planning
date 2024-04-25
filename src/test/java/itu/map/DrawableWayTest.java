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
    MapModel model;

    @BeforeEach
    void setUp() {
        coords = new float[]{1, 2, 3, 4, 5, 6};
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        model = new MapModel();
    }

    @Test
    void testConstrutor() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        String[] tags = new String[2];
        DrawableWay way = new DrawableWay(outerCoords, innerCoords, tags,0, "", "");

        assertArrayEquals(way.getOuterCoords(), outerCoords.toArray());
        assertArrayEquals(way.getTags(), tags);
    }

    @Test
    void testWayEqualsNotAWay() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        String[] tags = new String[2];
        DrawableWay way = new DrawableWay(outerCoords, innerCoords, tags, 0, "", "");

        assertNotEquals(way, new Object());
    }

    @Test
    void testWayEqualsSameWay() {
        CoordArrayList innerCoords = new CoordArrayList();
        CoordArrayList outerCoords = new CoordArrayList();
        String[] tags = new String[2];
        DrawableWay way = new DrawableWay(outerCoords, innerCoords, tags, 0, "", "");

        assertEquals(way, way);
    }

    @Test
    void testToString() {
        float[] coords = {1, 2, 3, 4, 5, 6};
        CoordArrayList innerCoords = new CoordArrayList(coords);
        CoordArrayList outerCoords = new CoordArrayList(coords);
        String[] tags = new String[2];
        String expected = "Nodes:\n3\n1.0 2.0\n3.0 4.0\n5.0 6.0\nInner nodes:\n3\n1.0 2.0\n3.0 4.0\n5.0 6.0\nTags:\n2\nnull null\n";
        assertEquals(expected, new DrawableWay(outerCoords, innerCoords, tags, 0, "", "").toString());
    }

    @Test
    void testDraw() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"motorway"}, 0, "", "");
        way.draw(gc, 100, model.theme);
    }

    @Test
    void testDrawNoCoords() {
        way = new DrawableWay(new CoordArrayList(), new String[]{"motorway"}, 0, "", "");
        way.draw(gc, 100, model.theme);
    }

    @Test
    void testDrawIsIsland() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"island"}, 0, "", "");
        way.draw(gc, 100, model.theme);
    }

    @Test
    void testIsRelationExpectsFalse() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(), new String[]{"motorway"}, 0, "", "");
        assertFalse(way.isRelation());
    }

    @Test
    void testIsRelationExpectsTrue() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"type", "relation"}, 0, "", "");
        assertTrue(way.isRelation());
    }

    @Test
    void testDrawShouldFill() {
        way = new DrawableWay(new CoordArrayList(coords), new CoordArrayList(coords), new String[]{"beach"}, 0, "", "");
        way.draw(gc, 100, model.theme);
    }

}
