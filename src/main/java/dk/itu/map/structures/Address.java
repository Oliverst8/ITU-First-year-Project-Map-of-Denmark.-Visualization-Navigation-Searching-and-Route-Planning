package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.BooleanArrayList;
import dk.itu.map.structures.ArrayLists.CharArrayList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Address implements Runnable{
    private CharArrayList streetTree;
    private BooleanArrayList[] endingChar;
    private TwoDTree kdTree;
    private Queue<String> streetNames;

    public Address(){
        streetNames = new LinkedList<>();
        streetTree = new CharArrayList();
    }

    public void addStreetName(String streetName){
        streetNames.add(streetName);
    }

    public void run(){
        while(!streetNames.isEmpty()){
            String street = streetNames.remove();
            char[] streetChars = street.toCharArray();
            int[] searchResult = search(street);
            if(searchResult[0] < 0){
                searchResult[0] = -searchResult[0];
            }
            String streetToInsert = street.substring(searchResult[1]);
            insert(searchResult[0], streetToInsert);
        }
    }

    private int[] search(String goal){
        return search(0, new StringBuilder(), goal, 0);
    }
    //Ternary search
    private int[] search(int start, StringBuilder current, String goal, int pos){
        if(streetTree.get(start) == '\000') return (new int[]{-start, pos});

        if((!current.toString().isEmpty()) && current.toString().charAt(pos) == goal.charAt(pos)) {
            if(pos == goal.length()-1) return new int[]{start, pos};
            return search(getMiddleChild(start), current.append(streetTree.get(start)), goal, pos+1);
        }

        if(streetTree.get(start) < goal.charAt(pos)) return search(getRightChild(start), current.append(streetTree.get(start)), goal, pos);

        return search(getLeftChild(start), current.append(streetTree.get(start)), goal, pos);
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

    private void insert(int index, String value){
        char[] charArray = value.toCharArray();
        for(char c : charArray){
            streetTree.set(index,c);
            index = getMiddleChild(index);
        }
    }

    public char[] getStreetTree(){
        return streetTree.toArray();
    }

}
