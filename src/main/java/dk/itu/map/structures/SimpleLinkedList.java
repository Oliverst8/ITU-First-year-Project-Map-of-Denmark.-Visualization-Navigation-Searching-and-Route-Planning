package dk.itu.map.structures;
import java.util.Collection;
import java.util.Iterator;

public class SimpleLinkedList<E> implements Iterable<E> {
    private int size;
    private Node<E> first;

    /**
     * Instantiates a new SimpleLinkedList.
     */
    public SimpleLinkedList() {
        size = 0;
    }

    /**
     * Instantiates a new SimpleLinkedList with the elements of the given collection.
     * @param c the collection to add to the list
     */
    public SimpleLinkedList(Collection<E> c) {
        size = 0;
        addAll(c);
    }

    /**
     * Adds all the elements of the given collection to the list.
     * @param c the collection to add to the list
     */
    private void addAll(Collection<E> c) {
        Node<E> current = null;
        size = c.size();

        for (E e : c) {
            if (current == null) {
                current = first = new Node<E>(e, null);
                continue;
            }
            current.setNext(new Node<E>(e, null));
            current = current.getNext();
        }
    }

    /**
     * @param e the first element in the list
     */
    public Node<E> getFirst() {
        return first;
    }

    /**
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * @return an iterator used to iterate over the elements in the list
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = first;

            public boolean hasNext() {
                return current != null;
            }

            public E next() {
                E item = current.getValue();
                current = current.getNext();
                return item;
            }
        };
    }

    /*
     * preDestination -> destination ... preTarget -> target -> postTarget
     * ----
     * preDestination -> target -> destination ... preTarget -> postTarget
     */
    public void move(Node<E> preDestination, Node<E> preTarget) {
        Node<E> target = preTarget.getNext();

        preTarget.setNext(target.getNext());
        target.setNext(preDestination.getNext());

        preDestination.setNext(target);
    }

    /**
     * A node in the list.
     */
    public static class Node<E> {
        private E item;
        private Node<E> next;

        /**
         * Instantiates a new Node.
         * @param element the element to store in the node
         * @param next the next node in the list
         */
        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }

        /**
         * @return the next node in the list
         */
        public Node<E> getNext() {
            return next;
        }

        /**
         * Sets the next node in the list.
         * @param node the node to set as the next node
         */
        public void setNext(Node<E> node) {
            next = node;
        }

        /**
         * @return the element stored in the node
         */
        public E getValue() {
            return item;
        }
    }
}

