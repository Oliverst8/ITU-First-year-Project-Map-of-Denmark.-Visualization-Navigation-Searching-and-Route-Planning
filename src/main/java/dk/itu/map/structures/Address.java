package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.BooleanArrayList;
import dk.itu.map.structures.ArrayLists.CharArrayList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Address implements Runnable, WriteAble{
    private CharArrayList streetTree;
    private BooleanArrayList[] endingChar;
    private TwoDTree kdTree;
    private Queue<String> streetNames;
    private Node root;
    private int size;

    public Address(){
        size = 0;
        streetNames = new LinkedList<>();
        streetTree = new CharArrayList();
    }

    public void addStreetName(String streetName){
        streetNames.add(streetName);
    }

    public void run(){
        String street = streetNames.remove();
        root = insert(street);
        while(!streetNames.isEmpty()){
            street = streetNames.remove();
            insert(street);
        }
    }

    private Node insert(String value){
        return insert(root, value, 0);
    }

    private Node insert(Node node, String value, int pos){
        if(pos >= value.length()) return node;
        if (node == null){
            node = new Node();
            node.value = value.charAt(pos);
            size++;
        }
        if(node.value > value.charAt(pos)){
            node.left = insert(node.left, value, pos);
        } else if(node.value < value.charAt(pos)){
            node.right = insert(node.right, value, pos);
        } else if(pos == value.length()-1){
            node.isTerminal = true;
        }else {
            node.middle = insert(node.middle, value, pos+1);
        }
        return node;
    }

    public int Size(){
        return size;
    }

    public InformationNode search(String value){
        return search(root, value, 0);
    }

    public InformationNode search(Node node, String value, int pos){
        if(node == null) return null;

        if(pos >= value.length()) {
            return new InformationNode(node, pos, false);
        }
        InformationNode searchResult;
        if(node.value == value.charAt(pos)){
            if(node.isTerminal && pos == value.length()-1){
                return new InformationNode(node, pos, true);
            }
            searchResult = search(node.middle, value, pos+1);
            if(searchResult == null) return new InformationNode(node,pos, false);
        } else if(node.value > value.charAt(pos)){
            searchResult = search(node.left, value, pos);
            if(searchResult == null) return new InformationNode(node,pos, false);
        } else {
            searchResult = search(node.right, value, pos);
            if(searchResult == null) return new InformationNode(node,pos, false);
        }

        return searchResult;

    }

    public Set<String> autoComplete(String value, int maxResults){



    }

    private int getLeftChild(int index){
        return index*3+1;
    }

    private int getMiddleChild(int index){
        return index*3+2;
    }

    private int getRightChild(int index){
        return index*3+3;
    }

    public char[] getStreetTree(){
        return streetTree.toArray();
    }



    @Override
    public void write(DataOutputStream stream) throws IOException {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            Node node = queue.remove();
            if(node == null){
                stream.writeChar('\0');
                continue;
            }
            stream.writeChar(node.value);
            stream.writeBoolean(node.isTerminal);
            queue.add(node.left);
            queue.add(node.middle);
            queue.add(node.right);
        }
    }


    @Override
    public void read(DataInputStream stream) throws IOException {
        Queue<Node> queue = new LinkedList<>();
        root = new Node();
        root.value = stream.readChar();
        root.isTerminal = stream.readBoolean();
        queue.add(root);
        while(!queue.isEmpty()) {
            size++;
            Node current = queue.remove();
            char left = stream.readChar();
            if(left != '\0'){
                current.left = new Node();
                current.left.value = left;
                current.left.isTerminal = stream.readBoolean();
                queue.add(current.left);
            }
            char middle = stream.readChar();
            if(middle != '\0'){
                current.middle = new Node();
                current.middle.value = middle;
                current.middle.isTerminal = stream.readBoolean();
                queue.add(current.middle);
            }
            char right = stream.readChar();
            if(right != '\0'){
                current.right = new Node();
                current.right.value = right;
                current.right.isTerminal = stream.readBoolean();
                queue.add(current.right);
            }
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj.getClass() != this.getClass()) return false;
        Address other = (Address) obj;
        if(other.size != this.size) return false;
        Queue<Node> thisQueue = new LinkedList<>();
        Queue<Node> otherQueue = new LinkedList<>();
        thisQueue.add(this.root);
        otherQueue.add(other.root);
        while(!thisQueue.isEmpty()){
            Node thisNode = thisQueue.remove();
            Node otherNode = otherQueue.remove();
            if(thisNode == null && otherNode == null) continue;
            if(thisNode == null || otherNode == null) return false;
            if(thisNode.value != otherNode.value) return false;
            if(thisNode.isTerminal != otherNode.isTerminal) return false;
            thisQueue.add(thisNode.left);
            thisQueue.add(thisNode.middle);
            thisQueue.add(thisNode.right);
            otherQueue.add(otherNode.left);
            otherQueue.add(otherNode.middle);
            otherQueue.add(otherNode.right);
        }
        return true;
    }

    public class Node{
        public Node left;
        public Node middle;
        public Node right;
        public char value;
        public boolean isTerminal;
    }

    public class InformationNode {
        public Node node;
        int pos;
        boolean fullMatch;
        public InformationNode(Node node, int pos, boolean fullMatch){
            this.node = node;
            this.pos = pos;
            this.fullMatch = fullMatch;
        }
    }
}
