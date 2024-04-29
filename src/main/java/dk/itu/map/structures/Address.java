package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.IntArrayList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Address implements Runnable, WriteAble{
    private final Queue<String[]> streetNames;
    private Map<String, Integer> streetNumberMap, zipMap, cityMap;
    private List<String> streetNumber, zip, cities;
    private Node root;
    private int size;

    public Address(){
        size = 0;
        streetNames = new LinkedList<>();
        streetNumberMap = new HashMap<>();
        zipMap = new HashMap<>();
        cityMap = new HashMap<>();
        streetNumber = new ArrayList<>();
        zip = new ArrayList<>();
        cities = new ArrayList<>();
    }

    public void addStreetName(String[] streetName){
        streetNames.add(streetName);
    }

    public void run(){
        String[] street = streetNames.remove();
        root = insert(street);
        while(!streetNames.isEmpty()){
            street = streetNames.remove();
            insert(street);
        }
    }

    private Node insert(String[] value){
        return insert(root, value, 0);
    }

    private Node insert(Node node, String[] value, int pos){
        if(pos >= value[0].length()) return node;
        if (node == null){
            node = new Node(value[0].charAt(pos));
            size++;
        }
        int cmp = node.compareTo(value[0].charAt(pos));
        if(cmp > 0){
            node.left = insert(node.left, value, pos);
        } else if(cmp < 0){
            node.right = insert(node.right, value, pos);
        } else if(pos == value[0].length()-1){
            //node.isTerminal = true;
            if(!node.isTerminal) {
                node = new AddressNode(value, value[0].charAt(pos));
            } else ((AddressNode) node).addAddress(value);
        }else {
            node.middle = insert(node.middle, value, pos+1);
        }
        return node;
    }

    private void addToResults(String value, Node input, Map<String[], AddressNode> results){
        AddressNode node = (AddressNode) input;
        for(int i = 0; i < node.zipIndexes.size(); i++){
            results.put(new String[]{value, zip.get(node.zipIndexes.get(i))}, (AddressNode) input);
        }
    }

    public int Size(){
        return size;
    }

    public Node search(String value){
        return search(root, value, 0);
    }

    public Node search(Node node, String value, int pos){
        if(node == null) return null;

        if(pos >= value.length()) {
            //return new InformationNode(node, pos, false);
            return null;
        }
        int cmp = node.compareTo(value.charAt(pos));
        if(cmp == 0){
            if(pos == value.length()-1){
                //return new InformationNode(node, pos, true);
                return node;
            }
            //searchResult =
            return search(node.middle, value, pos+1);

            //if(searchResult == null) return new InformationNode(node,pos, false);
        } else if(cmp > 0){
            //searchResult =
            return search(node.left, value, pos);
            //if(searchResult == null) return new InformationNode(node,pos, false);
        } else {
            //searchResult = search(node.right, value, pos);
            return search(node.right, value, pos);
            //if(searchResult == null) return new InformationNode(node,pos, false);
        }

        //return searchResult;

    }

    public Map<String[], AddressNode> autoComplete(String value, int maxResults){
        Node searchResult = search(value);
        Map<String[], AddressNode> results = new HashMap<>();
        Queue<Node> branches = new LinkedList<>();
        Queue<StringBuilder> branchStrings = new LinkedList<>();

        if (searchResult == null) return results;

        if(searchResult.isTerminal) {
            addToResults(value, searchResult, results);
        }

        if(searchResult.middle != null) branches.add(searchResult.middle);
        else return results;

        branchStrings.add(new StringBuilder(value));

        while(!branches.isEmpty()){
            Node current = branches.remove();
            StringBuilder currentString = branchStrings.remove();

            if(current.left != null) {
                branches.add(current.left);
                branchStrings.add(new StringBuilder(currentString));
            }
            if(current.middle != null) {
                branches.add(current.middle);
                branchStrings.add(new StringBuilder(currentString).append(current.value));
            }
            if(current.right != null) {
                branches.add(current.right);
                branchStrings.add(new StringBuilder(currentString));
            }

            if(current.isTerminal) {
                currentString.append(current.value);
                addToResults(currentString.toString(), current, results);
            }

            if(results.size() >= maxResults) break;

        }

        return results;
    }

    public List<String[]> fillAddress(String[] address, AddressNode node) {
        List<String[]> list = new ArrayList<>();

        for(int i = 0; i < node.zipIndexes.size(); i++){
            if(zip.get(node.zipIndexes.get(i)).equals(address[1])){
                String[] newAddress = new String[4];
                newAddress[0] = address[0];
                newAddress[1] = streetNumber.get(node.streetNumberIndexes.get(i));
                newAddress[2] = address[1];
                newAddress[3] = cities.get(node.cityIndexes.get(i));
                list.add(newAddress);
            }
        }
        if(list.isEmpty()) throw new IllegalArgumentException("Zip not found");
        return list;
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
        root = new Node(stream.readChar());
        root.isTerminal = stream.readBoolean();
        queue.add(root);
        while(!queue.isEmpty()) {
            size++;
            Node current = queue.remove();
            char left = stream.readChar();
            if(left != '\0'){
                current.left = new Node(left);
                current.left.isTerminal = stream.readBoolean();
                queue.add(current.left);
            }
            char middle = stream.readChar();
            if(middle != '\0'){
                current.middle = new Node(middle);
                current.middle.isTerminal = stream.readBoolean();
                queue.add(current.middle);
            }
            char right = stream.readChar();
            if(right != '\0'){
                current.right = new Node(right);
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

        public Node(char value){
            this.value = value;
        }

        public String toString(){
            return "Value: " + value + " Terminal: " + isTerminal + (left != null ? " Has left" : " left is null") + (middle != null ? " Has middle" : " middle is null") + (right != null ? " Has right" : " right is null");
        }

        public int compareTo(char otherValue){
            return Character.compare(Character.toLowerCase(value), Character.toLowerCase(otherValue));
        }

    }

    public class AddressNode extends Node{

        IntArrayList cityIndexes;
        IntArrayList zipIndexes;
        IntArrayList streetNumberIndexes;

        public AddressNode(String[] address, char value){
            super(value);
            isTerminal = true;
            cityIndexes = new IntArrayList();
            zipIndexes = new IntArrayList();
            streetNumberIndexes = new IntArrayList();
            addAddress(address);
        }

        public void addAddress(String[] address){
            int streetNumberIndex;
            if(streetNumberMap.containsKey(address[1])){
                streetNumberIndex = streetNumberMap.get(address[1]);
                streetNumberIndexes.add(streetNumberIndex);
            } else {
                streetNumber.add(address[1]);
                streetNumberMap.put(address[1], streetNumber.size()-1);
                streetNumberIndexes.add(streetNumber.size()-1);
            }

            int zipIndex;
            if(zipMap.containsKey(address[2])){
                zipIndex = zipMap.get(address[2]);
                zipIndexes.add(zipIndex);
            } else {
                zip.add(address[2]);
                zipMap.put(address[2], zip.size()-1);
                zipIndexes.add(zip.size()-1);
            }

            int cityIndex;
            if(cityMap.containsKey(address[3])){
                cityIndex = cityMap.get(address[3]);
                cityIndexes.add(cityIndex);
            } else {
                cities.add(address[3]);
                cityMap.put(address[3], cities.size()-1);
                cityIndexes.add(cities.size()-1);
            }

        }

    }

}
