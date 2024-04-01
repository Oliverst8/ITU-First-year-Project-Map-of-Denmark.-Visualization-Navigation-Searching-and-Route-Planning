package dk.itu.map.structures;
import java.util.Collection;

public class LinkedListSimple<E> {
    transient int size = 0;
    transient Node<E> first;
    
    public LinkedListSimple(Collection<E> c) {
        addAll(c);
    }

    private void addAll(Collection<E> c) {
        Node<E> current = null;
        for (E e : c) {
            if (current == null) {
                current = first = new Node<E>(c.iterator().next(), null);
            }
            current.setNext(new Node<E>(e, null));
        }
    }

    public Node<E> getFirst() {
        return first;
    }

    
    public static class Node<E> {
        private E item;
        private Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> node) {
            next = node;
        }

        public E getValue() {
            return item;
        }
    }
}


