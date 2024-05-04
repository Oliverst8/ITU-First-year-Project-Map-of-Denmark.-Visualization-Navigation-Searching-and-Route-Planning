package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class TernaryTree implements Runnable, WriteAble{
    private final List<String[]> streetNames; //List (Queue) to keep track of addresses to add to the tree
    private final List<Float> streetPosition; //List (Queue) to keep track of addresses coordinates (Lat, lon)
    private Map<String, Integer> streetNumberMap, zipMap, cityMap; //Map that maps from strings to their position in the laters lists (Only used while building)
    private List<String> streetNumber, zip, cities; //List that holds the different streetnumber, zip and cities
    private Node root; //The root of the tree
    private int size; //The size of the tree
    private boolean running; //Boolean to tell weather or should keep building

    /**
     * Constructor for the TernaryTree
     * Initializes private fields
     */
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

    /**
     * Add a streetname to be inserted into the tree
     * @param streetName The street to be inserted (Position 0 is the streetname, position 1 is the streetnumber, position 2 is the zip, position 3 is the city)
     * @param lat the lattitude of the street
     * @param lon the longitude of the street
     */
    public void addStreetName(String[] streetName, float lat, float lon){
        streetNames.add(streetName);
        streetPosition.add(lat);
        streetPosition.add(lon);
    }

    /**
     * Method to start building the tree. Should always be called through a thread
     * Will keep building the tree until the streetNames list is empty and .finish() has been called
     */
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
            while(!streetNames.isEmpty() && (streetPosition.size() >= 2)){
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

    /**
     * Inserts a street into the tree
     * @param value The street to be inserted (Position 0 is the streetname, position 1 is the streetnumber, position 2 is the zip, position 3 is the city)
     * @param lat the lattitude of the address
     * @param lon the longitude of the address
     * @return The first node of the address
     */
    private Node insert(String[] value, float lat, float lon){
        return insert(root, value, 0, lat, lon);
    }

    /**
     * Recursive method to insert a adress into the tree
     * @param node the node to be inserted into/under
     * @param value the address to be inserted
     * @param pos what character in the streetname that has been gotten to
     * @param lat the lattitude of the address
     * @param lon the longitude of the address
     * @return The inserted node
     */
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
            } else ((AddressNode) node).addAddress(value, lat, lon);
        }else {
            node.middle = insert(node.middle, value, pos+1, lat, lon);
        }
        return node;
    }

    /**
     * Method to add a street to the results list of an autocomplete
     * @param value The street to be added
     * @param input The last node of the street
     * @param results the list the results should be added to. Creates a searchAdress of the steetname zip code, and the given node
     */
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

    /**
     * Method to get the size of the tree
     * @return the size of the tree
     */
    public int Size(){
        return size;
    }

    /**
     * Method to search for a street in the tree
     * @param value the street to be searched for
     * @return the terminal node of the street. (Returns null if it dosent have a terminal node in the tree (Dosent exist))
     */
    public Node search(String value){
        return search(root, value, 0);
    }

    /**
     * Recursive method to search for a street in the tree
     * @param node the node to be searched from
     * @param value the street to be searched for
     * @param pos the position in the streetname that has been gotten to
     * @return The node of the last character of the string. (Returns null if it dosent exist in the tree)
     */
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

    /**
     * Method to autocomplete a streetname
     * @param value the streetname to be autocompleted
     * @param maxResults the maximum amount of results to be returned (Will return more if there are multiple street with the name (Differnet zipcodes))
     * @return a list of searchAddresses that match the streetname containing the found streetname,the zip code, and the node it is found at
     */
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

    /**
     * Creates a list of searchAddresses that match the given address and zip code
     * @param address the selected address autocompleted (position 0 is the streetname and position 1 is the zip code)
     * @param node the terminal node of the adress
     * @param current the string to be autocompleted
     * @return a list of results
     */
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

    /**
     * Checks if a street and number matches the current string
     * @param current the current string
     * @param newStreetNumber the new street number
     * @return true if the street can be gotten from the current string
     */
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

    /**
     * Tells the thread to stop building the tree
     */
    public void finish(){
        running = false;
    }

    /**
     * Method to write the tree to a file
     * @param stream the stream to write to
     * @throws IOException if the stream cant be written to
     */
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

    /**
     * Method to read the tree from a file
     * @param stream the stream to read from
     * @throws IOException if the stream cant be read from
     */
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

    /**
     * Method to check if graph is equal to another graph
     * @param obj the object to be compared to
     * @return true if all the fields that are used past build fase is equal
     */
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

    /**
     * Method to get the city list
     * @return the list of cities in the tree
     */
    public List<String> getCities() {
        return cities;
    }

    /**
     * Method to get the zip codes in the tree
     * @return the list of zip codes in the tree
     */
    public List<String> getZip() {
        return zip;
    }

    /**
     * Method to get the street numbers in the tree
     * @return the list of street numbers in the tree
     */
    public List<String> getStreetNumber() {
        return streetNumber;
    }

    public class Node implements WriteAble{
        public Node left; //The left child of the node
        public Node middle; //The middle child of the node
        public Node right; //The right child of the node
        public char value; //The value of the node
        public boolean isTerminal; //Boolean to tell if the node is a terminal node

        /**
         * Constructor for the node
         * @param value the value of the node
         */
        public Node(char value){
            this.value = value;
        }

        /**
         * Method to get the string representation of the node
         * @return the string representation of the node
         */
        public String toString(){
            return "Value: " + value + " Terminal: " + isTerminal + (left != null ? " Has left" : " left is null") + (middle != null ? " Has middle" : " middle is null") + (right != null ? " Has right" : " right is null");
        }

        /**
         * Method to compare the value of the node to another value
         * @param otherValue the value to be compared to
         * @return the comparison of the two values (1 if this is bigger, 0 if equal, -1 if this is smaller)
         */
        public int compareTo(char otherValue){
            return Character.compare(Character.toLowerCase(value), Character.toLowerCase(otherValue));
        }

        /**
         * Method to write the node to a file
         * @param stream the stream to write to
         * @throws IOException if the stream cant be written to
         */
        @Override
        public void write(DataOutputStream stream) throws IOException {
            stream.writeChar(value);
            stream.writeBoolean(isTerminal);
        }

        /**
         * Makes the isTerminal field false
         * @param stream the stream to read from
         * @throws IOException if the stream cant be read from
         */
        @Override
        public void read(DataInputStream stream) throws IOException {
            isTerminal = false;
        }

        /**
         * Method to check if the node is equal to another node
         * @param obj the object to be compared to
         * @return true if the two nodes are equal in value and terminal value
         */
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

        IntArrayList streetNumberIndexes; //List of indexes to the streetnumber list
        IntArrayList zipIndexes; //List of indexes to the zip list
        IntArrayList cityIndexes; //List of indexes to the city list
        CoordArrayList coords = new CoordArrayList(); //List of coordinates of the address

        /**
         * Constructor for the AddressNode
         * @param value the value of the node
         */
        public AddressNode(char value){
            super(value);
            isTerminal = true;
            cityIndexes = new IntArrayList();
            zipIndexes = new IntArrayList();
            streetNumberIndexes = new IntArrayList();
        }

        /**
         * Constructor for the AddressNode
         * @param address the address to be added to the node
         * @param value the value of the node
         * @param lat the lattitude of the address
         * @param lon the longitude of the address
         */
        public AddressNode(String[] address, char value, float lat, float lon){
            super(value);
            isTerminal = true;
            cityIndexes = new IntArrayList();
            zipIndexes = new IntArrayList();
            streetNumberIndexes = new IntArrayList();
            addAddress(address, lat, lon);
        }

        /**
         * Constructor for the AddressNode
         * @param address the address to be added to the node
         * @param value the value of the node
         * @param lat the lattitude of the address
         * @param lon the longitude of the address
         * @param node the node this should be a copy off
         */
        public AddressNode(String[] address, char value, float lat, float lon, Node node){
            this(address, value, lat, lon);
            this.left = node.left;
            this.middle = node.middle;
            this.right = node.right;
        }

        /**
         * Adds an address to the node
         * @param address the address to be added
         * @param lat the lattitude of the address
         * @param lon the longitude of the address
         */
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

        /**
         * Method to write the node to a file
         * @param stream the stream to write to
         * @throws IOException if the stream cant be written to
         */
        @Override
        public void write(DataOutputStream stream) throws IOException {
            stream.writeChar(value);
            stream.writeBoolean(isTerminal);
            coords.write(stream);
            streetNumberIndexes.write(stream);
            zipIndexes.write(stream);
            cityIndexes.write(stream);
        }

        /**
         * Method to read the node from a file
         * @param stream the stream to read from
         * @throws IOException if the stream cant be read from
         */
        @Override
        public void read(DataInputStream stream) throws IOException {
            isTerminal = true;
            coords.read(stream);
            streetNumberIndexes.read(stream);
            zipIndexes.read(stream);
            cityIndexes.read(stream);
        }

        /**
         * @return the string representation of the node
         */
        @Override
        public String toString(){
            return "Value: " + value + " Terminal: " + isTerminal + " StreetNumberIndexes: " + streetNumberIndexes + " ZipIndexes: " + zipIndexes + " CityIndexes: " + cityIndexes + " Coords: " + coords;
        }

        /**
         * Method to check if the node is equal to another node
         * @param obj the object to be compared to
         * @return true if the two nodes are equal in value, terminal value, streetnumberindexes, zipindexes, cityindexes and coords
         */
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
        public String streetName; //The streetname of the address
        public String streetNumber; //The streetnumber of the address
        public String zip; //The zip code of the address
        public String city; //The city of the address
        public Point point; //The location of the address
        public AddressNode node; //The node of the address

        /**
         * Constructor for the searchAddress
         * @param streetName the streetname of the address
         * @param zip the zip code of the address
         * @param node the node of the address
         */
        public searchAddress(String streetName, String zip, AddressNode node){
            this.streetName = streetName;
            this.streetNumber = null;
            this.zip = zip;
            this.city = null;
            this.node = node;
        }

        /**
         * Clones the fields of another node to this one
         * @param other the node to be cloned
         */
        public void clone(searchAddress other){
            this.streetName = other.streetName;
            this.streetNumber = other.streetNumber;
            this.zip = other.zip;
            this.city = other.city;
            this.node = other.node;
        }

        /**
         * @return the string representation of the address
         */
        @Override
        public String toString(){
            if(streetNumber == null && city == null) return streetName + ": " + zip;
            return streetName + " " + streetNumber + ", " + zip + " " + city;
        }

        /**
         * Resets the field of the node to null
         */
        public void reset() {
            this.streetName = null;
            this.streetNumber = null;
            this.zip = null;
            this.city = null;
            this.node = null;
        }
    }

}
