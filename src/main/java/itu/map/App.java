package itu.map;

import java.io.File;

public class App {

    public static void main(String[] args) {
        try{
            FileHandler fileHandler = new FileHandler(new File("/home/ostarup/Downloads/isle-of-man-latest.osm.bz2"));
            fileHandler.load();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
