package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.BooleanArrayList;
import dk.itu.map.structures.ArrayLists.CharArrayList;

import java.util.*;

public class Address implements Runnable{
    private CharArrayList streetTree;
    private BooleanArrayList[] endingChar;
    private TwoDTree kdTree;
    private Queue<String> streetNames;
    private Node root;

    public Address(){
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

        Set<String> result = new HashSet<>();
        InformationNode node = search(value);

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
