package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class TernaryTree implements Runnable, WriteAble{
    private final List<String[]> streetNames;
    private final List<Float> streetPosition;
    private Map<String, Integer> streetNumberMap, zipMap, cityMap;
    private List<String> streetNumber, zip, cities;
    private Node root;
    private int size;
    private boolean running;

    public TernaryTree(){
        size = 0;
        streetNames = Collections.synchronizedList(new LinkedList<>());
        streetPosition = Collections.synchronizedList(new LinkedList<>());
        streetNumberMap = new HashMap<>();
        zipMap = new HashMap<>();
        cityMap = new HashMap<>();
        streetNumber = new ArrayList<>();
        zip = new ArrayList<>();
        cities = new ArrayList<>();
    }

    public void addStreetName(String[] streetName, float X, float Y){
        streetNames.add(streetName);
        streetPosition.add(X);
        streetPosition.add(Y);
    }

    public void run(){
        running = true;
        while(streetNames.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Starting to build tree");
        String[] street = streetNames.remove(0);
        float lat = streetPosition.remove(0);
        float lon = streetPosition.remove(0);
        root = insert(street, lat, lon);
        while(running){
            while(!streetNames.isEmpty()){
                street = streetNames.remove(0);
                lat = streetPosition.remove(0);
                lon = streetPosition.remove(0);
                insert(street, lat, lon);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Finsihed tree");
    }

    private Node insert(String[] value, float X, float Y){
        return insert(root, value, 0, X, Y);
    }

    private Node insert(Node node, String[] value, int pos, float lat, float lon){
        if(pos >= value[0].length()) return node;
        if (node == null){
            node = new Node(value[0].charAt(pos));
            size++;
        }
        int cmp = node.compareTo(value[0].charAt(pos));
        if(cmp > 0){
            node.left = insert(node.left, value, pos, lat, lon);
        } else if(cmp < 0){
            node.right = insert(node.right, value, pos, lat, lon);
        } else if(pos == value[0].length()-1){
            if(!node.isTerminal) {
                node = new AddressNode(value, value[0].charAt(pos), lat, lon, node);
                node.temp = value[0];
            } else ((AddressNode) node).addAddress(value, lat, lon);
        }else {
            node.middle = insert(node.middle, value, pos+1, lat, lon);
        }
        return node;
    }

    private void addToResults(String value, Node input, List<searchAddress> results){
        AddressNode node = (AddressNode) input;
        HashMap<String, Set<String>> zipToRoad = new HashMap<>();
        for(int i = 0; i < node.zipIndexes.size(); i++){
            String zipCode = zip.get(node.zipIndexes.get(i));
            if(zipToRoad.containsKey(zipCode)){
                Set<String> roads = zipToRoad.get(zipCode);
                if(roads.contains(value)) continue;
                roads.add(value);
            } else {
                Set<String> roads = new HashSet<>();
                roads.add(value);
                zipToRoad.put(zipCode, roads);
            }
            searchAddress search = new searchAddress(value, zipCode, (AddressNode) input);
            results.add(search);
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
            return null;
        }
        int cmp = node.compareTo(value.charAt(pos));
        if(cmp == 0){
            if(pos == value.length()-1){
                return node;
            }
            return search(node.middle, value, pos+1);
        } else if(cmp > 0){
            return search(node.left, value, pos);
        } else {
            return search(node.right, value, pos);
        }
    }

    public List<searchAddress> autoComplete(String value, int maxResults){
        Node searchResult = search(value);
        List<searchAddress> results = new ArrayList<>();
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

            if(current instanceof AddressNode){
                currentString.append(current.value);
                addToResults(currentString.toString(), current, results);
            }

            if(results.size() >= maxResults) break;

        }

        return results;
    }

    public List<searchAddress> fillAddress(String[] address, AddressNode node, String current) {
        List<searchAddress> list = new ArrayList<>();
        current = current.substring(address[0].length());
        if(!current.isEmpty() && current.charAt(0) == ' ') current = current.substring(1);
        System.out.println("Current:" + current + ":");
        addressLoop:
        for(int i = 0; i < node.zipIndexes.size(); i++){
            if(zip.get(node.zipIndexes.get(i)).equals(address[1])){
                String newStreetNumber = streetNumber.get(node.streetNumberIndexes.get(i));;
                String newCity = cities.get(node.cityIndexes.get(i));;

                if (!checkIfShouldSuggest(current, newStreetNumber)) continue addressLoop;

                searchAddress newAddress = new searchAddress(address[0], address[1], node);
                newAddress.streetNumber = newStreetNumber;
                newAddress.city = newCity;
                float[] coords = node.coords.get(i);
                newAddress.point = new Point(coords[1], coords[0], "navigation", Color.PURPLE);
                list.add(newAddress);
            }
        }
        return list;
    }

    private static boolean checkIfShouldSuggest(String current, String newStreetNumber) {
        char[] charArray = current.toCharArray();
        int j = 0;
        if(j >= charArray.length) return true;
        char c = charArray[j];
        for (char cSub : newStreetNumber.toCharArray()) {
            if(j >= charArray.length) return true;
            c = charArray[j];
            j++;
            if (c != cSub) return false;
        }
        return true;
    }

    public void finish(){
        running = false;
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
            node.write(stream);
            queue.add(node.left);
            queue.add(node.middle);
            queue.add(node.right);
        }
        stream.writeInt(streetNumber.size());
        for(String streetNum : streetNumber){
            stream.writeUTF(streetNum);
        }
        stream.writeInt(zip.size());
        for(String zipCode : zip){
            stream.writeUTF(zipCode);
        }
        stream.writeInt(cities.size());
        for(String city : cities){
            stream.writeUTF(city);
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
                if(stream.readBoolean()){
                    current.left = new AddressNode(left);
                    current.left.read(stream);
                } else {
                    current.left = new Node(left);
                    current.left.read(stream);
                }
                queue.add(current.left);
            }
            char middle = stream.readChar();
            if(middle != '\0'){
                if(stream.readBoolean()){
                    current.middle = new AddressNode(middle);
                    current.middle.read(stream);
                } else {
                    current.middle = new Node(middle);
                    current.middle.read(stream);
                }
                queue.add(current.middle);
            }
            char right = stream.readChar();
            if(right != '\0'){
                if(stream.readBoolean()){
                    current.right = new AddressNode(right);
                    current.right.read(stream);
                } else {
                    current.right = new Node(right);
                    current.right.read(stream);
                }
                queue.add(current.right);
            }
        }
        int streetNumberSize = stream.readInt();
        streetNumber = new ArrayList<>(streetNumberSize);
        for(int i = 0; i < streetNumberSize; i++){
            streetNumber.add(stream.readUTF());
        }
        int zipSize = stream.readInt();
        zip = new ArrayList<>(zipSize);
        for(int i = 0; i < zipSize; i++){
            zip.add(stream.readUTF());
        }
        int citySize = stream.readInt();
        cities = new ArrayList<>(citySize);
        for(int i = 0; i < citySize; i++){
            cities.add(stream.readUTF());
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj.getClass() != this.getClass()) return false;
        TernaryTree other = (TernaryTree) obj;
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
            if(!thisNode.equals(otherNode)) return false;
            thisQueue.add(thisNode.left);
            thisQueue.add(thisNode.middle);
            thisQueue.add(thisNode.right);
            otherQueue.add(otherNode.left);
            otherQueue.add(otherNode.middle);
            otherQueue.add(otherNode.right);
        }
        if(!this.streetNumber.equals(other.getStreetNumber())) return false;
        if(!this.zip.equals(other.getZip())) return false;
        if(!this.cities.equals(other.getCities())) return false;

        return true;
    }

    public List<String> getCities() {
        return cities;
    }

    public List<String> getZip() {
        return zip;
    }

    public List<String> getStreetNumber() {
        return streetNumber;
    }

    public class Node implements WriteAble{
        String temp;
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

        @Override
        public void write(DataOutputStream stream) throws IOException {
            stream.writeChar(value);
            stream.writeBoolean(isTerminal);
        }

        /**
         * This expects that terminal is false
         * @param stream
         * @throws IOException
         */
        @Override
        public void read(DataInputStream stream) throws IOException {
            isTerminal = false;
        }

        @Override
        public boolean equals(Object obj){
            if(obj == null) return false;
            if(!(obj.getClass().equals(this.getClass()))) return false;
            Node other = (Node) obj;
            if(this.value != other.value) return false;
            if(this.isTerminal != other.isTerminal) return false;
            return true;
        }
    }

    public class AddressNode extends Node{

        IntArrayList streetNumberIndexes;
        IntArrayList zipIndexes;
        IntArrayList cityIndexes;
        CoordArrayList coords = new CoordArrayList();

        public AddressNode(char value){
            super(value);
            isTerminal = true;
            cityIndexes = new IntArrayList();
            zipIndexes = new IntArrayList();
            streetNumberIndexes = new IntArrayList();
        }


        public AddressNode(String[] address, char value, float lat, float lon){
            super(value);
            isTerminal = true;
            cityIndexes = new IntArrayList();
            zipIndexes = new IntArrayList();
            streetNumberIndexes = new IntArrayList();
            addAddress(address, lat, lon);
        }

        public AddressNode(String[] address, char value, float lat, float lon, Node node){
            this(address, value, lat, lon);
            this.left = node.left;
            this.middle = node.middle;
            this.right = node.right;
        }

        public void addAddress(String[] address, float lat, float lon){
            coords.add(lat, lon);
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

        @Override
        public void write(DataOutputStream stream) throws IOException {
            stream.writeChar(value);
            stream.writeBoolean(isTerminal);
            coords.write(stream);
            streetNumberIndexes.write(stream);
            zipIndexes.write(stream);
            cityIndexes.write(stream);
        }

        @Override
        public void read(DataInputStream stream) throws IOException {
            isTerminal = true;
            coords.read(stream);
            streetNumberIndexes.read(stream);
            zipIndexes.read(stream);
            cityIndexes.read(stream);
        }

        @Override
        public String toString(){
            return "Value: " + value + " Terminal: " + isTerminal + " StreetNumberIndexes: " + streetNumberIndexes + " ZipIndexes: " + zipIndexes + " CityIndexes: " + cityIndexes + " Coords: " + coords;
        }

        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) return false;
            AddressNode other = (AddressNode) obj;
            if (!this.streetNumberIndexes.equals(other.streetNumberIndexes)) return false;
            if (!this.zipIndexes.equals(other.zipIndexes)) return false;
            if (!this.cityIndexes.equals(other.cityIndexes)) return false;
            if(!this.coords.equals(other.coords)) return false;
            return true;
        }
    }

    public static class searchAddress {
        public String streetName;
        public String streetNumber;
        public String zip;
        public String city;
        public Point point;
        public AddressNode node;

        public searchAddress(String streetName, String zip, AddressNode node){
            this.streetName = streetName;
            this.streetNumber = null;
            this.zip = zip;
            this.city = null;
            this.node = node;
        }

        public void clone(searchAddress other){
            this.streetName = other.streetName;
            this.streetNumber = other.streetNumber;
            this.zip = other.zip;
            this.city = other.city;
            this.node = other.node;
        }

        @Override
        public String toString(){
            if(streetNumber == null && city == null) return streetName + ": " + zip;
            return streetName + " " + streetNumber + ", " + zip + " " + city;
        }

        public void reset() {
            this.streetName = null;
            this.streetNumber = null;
            this.zip = null;
            this.city = null;
            this.node = null;
        }
    }

}
