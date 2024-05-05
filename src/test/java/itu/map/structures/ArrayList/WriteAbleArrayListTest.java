 package itu.map.structures.ArrayList;

 import java.io.File;
 import java.io.IOException;

 import itu.map.TestUtilities;

 import dk.itu.map.structures.ArrayLists.IntArrayList;
 import dk.itu.map.structures.ArrayLists.WriteAbleArrayList;

 import org.junit.jupiter.api.Test;
 import static org.junit.jupiter.api.Assertions.*;

 public class WriteAbleArrayListTest {
     @Test void readAndWriteToFile() throws IOException {
         // Write to file
         WriteAbleArrayList<IntArrayList> list = new WriteAbleArrayList<>();
         list.add(new IntArrayList(new int[]{1, 2, 3, 4}));

         String path = TestUtilities.getTestFilesPath() + "writeAbleArrayListTest.txt";
         list.write(path);

         // Read from file
         WriteAbleArrayList<IntArrayList> list2 = new WriteAbleArrayList<>();
         list2.add(new IntArrayList());
         list2.read(path);

         // Assert
         assertEquals(list.size(), list2.size());

         File file = new File(path);
         file.delete();

     }

     @Test
     void testEqualsDifferentObject(){
         WriteAbleArrayList<IntArrayList> list = new WriteAbleArrayList<>();
         IntArrayList intArrayList = new IntArrayList(new int[]{1, 2, 3, 4});
         list.add(intArrayList);

         assertNotEquals(list, intArrayList);
     }

     @Test
     void testEqualsDifferentSize() {
            WriteAbleArrayList<IntArrayList> list = new WriteAbleArrayList<>();
            WriteAbleArrayList<IntArrayList> list2 = new WriteAbleArrayList<>();
            IntArrayList intArrayList = new IntArrayList(new int[]{1, 2, 3, 4});
            list.add(intArrayList);
            list2.add(intArrayList);
            list2.add(intArrayList);

            assertNotEquals(list, list2);
     }

     @Test
     void testEqualsDifferentContent() {
            WriteAbleArrayList<IntArrayList> list = new WriteAbleArrayList<>();
            WriteAbleArrayList<IntArrayList> list2 = new WriteAbleArrayList<>();
            IntArrayList intArrayList = new IntArrayList(new int[]{1, 2, 3, 4});
            IntArrayList intArrayList2 = new IntArrayList(new int[]{1, 2, 3, 4,5});
            list.add(intArrayList);
            list2.add(intArrayList2);


            assertNotEquals(list, list2);
     }

 }
