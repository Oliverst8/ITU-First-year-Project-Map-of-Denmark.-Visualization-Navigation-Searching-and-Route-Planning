package itu.map;

import dk.itu.map.structures.SimpleLinkedList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleLinkedListTest {

    @Test
    void testAdding1MillionElements(){
        ArrayList<Integer> collection = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            collection.add(i);
        }
        SimpleLinkedList<Integer> simpleLinkedList = new SimpleLinkedList<Integer>(collection);
        assertEquals(100000, simpleLinkedList.size());

        SimpleLinkedList.Node<Integer> node = simpleLinkedList.getFirst();
        for (int i = 0; i < 100000; i++) {
            assertEquals(i, node.getValue());
            node = node.getNext();
        }
    }

    @Test
    void testMoveElements(){
        ArrayList<Integer> collection = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            collection.add(i);
        }
        SimpleLinkedList<Integer> simpleLinkedList = new SimpleLinkedList<Integer>(collection);

        SimpleLinkedList.Node<Integer> preDestination = simpleLinkedList.getFirst();
        SimpleLinkedList.Node<Integer> preTarget = preDestination.getNext();

        simpleLinkedList.move(preDestination, preTarget);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(2);
        expected.add(1);

        SimpleLinkedList.Node<Integer> node = simpleLinkedList.getFirst();
        for (int i = 0; i < 3; i++) {
            assertEquals(expected.get(i), node.getValue());
            node = node.getNext();
        }
    }
}
