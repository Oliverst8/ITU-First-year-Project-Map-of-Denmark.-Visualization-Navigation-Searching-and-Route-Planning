// package itu.map;


// import dk.itu.map.Model;
// import dk.itu.map.View;
// import dk.itu.map.parser.ChunkHandler;
// import javafx.stage.Stage;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.*;
// public class ViewTest {
//     View view;
//     @BeforeEach
//     void setup(){
//         view = new View();
//     }
//     @Test
//     void testGetSmallestRectExpects3457(){
//         Set<Integer> actual = view.getSmallestRect(4,6,3,3);
//         Set<Integer> expected = Set.of(3,4,6,7);
//         assertEquals(expected, actual);
//     }

//     @Test
//     void testGetSmallestRectExpects6(){
//         Set<Integer> actual = view.getSmallestRect(6,6,3,3);
//         Set<Integer> expected = Set.of(6);
//         assertEquals(expected, actual);
//     }

//     @Test
//     void testGetSmallestRectExpects63(){
//         Set<Integer> actual = view.getSmallestRect(3,6,3,3);
//         Set<Integer> expected = Set.of(6,3);
//         assertEquals(expected, actual);
//     }

//     @Test
//     void testGetSmallestRectExpects345(){
//         Set<Integer> actual = view.getSmallestRect(3,5,3,3);
//         Set<Integer> expected = Set.of(3,4,5);
//         assertEquals(expected, actual);
//     }


// }
