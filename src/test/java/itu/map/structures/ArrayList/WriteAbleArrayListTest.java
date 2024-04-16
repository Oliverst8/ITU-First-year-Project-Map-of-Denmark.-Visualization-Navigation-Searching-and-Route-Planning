// package itu.map.structures.ArrayList;

// import java.io.File;

// import itu.map.TestUtilities;

// import dk.itu.map.structures.ArrayLists.IntArrayList;
// import dk.itu.map.structures.ArrayLists.WriteAbleArrayList;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// public class WriteAbleArrayListTest {
//     @Test void readAndWriteToFile() {
//         // Write to file
//         WriteAbleArrayList<IntArrayList> list = new WriteAbleArrayList<>();
//         list.add(new IntArrayList(new int[]{1, 2, 3, 4}));

//         String path = TestUtilities.getTestFilesPath() + "writeAbleArrayListTest.txt";

//         try {
//             list.write(path);

//             // Read from file
//             WriteAbleArrayList<IntArrayList> list2 = new WriteAbleArrayList<>();
//             list2.read(path);

//             // Assert
//             assertEquals(list.size(), list2.size());

//             File file = new File(path);
//             file.delete();
//         } catch (Exception e) {
//             fail("Exception thrown: " + e);
//         }
//     }
// }
