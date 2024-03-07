package dk.itu.map;

import java.io.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import javax.xml.stream.*;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

public class FileHandler {

    private final File file;

    //Temp variable to save loaded ways
    public ArrayList<Way> ways;
    public float minlat, maxlat, minlon, maxlon;
    
    private ChunkGenerator chunkGenerator;

    /**
     * Initialises the filehandler
     * @param file
     */
    public FileHandler(File file) {
        this.file = file;

        ways = new ArrayList<>();
    }

    public void load() throws IOException, XMLStreamException {
        if (file.getName().contains("bz2")) {
            parse(new BZip2CompressorInputStream(new FileInputStream(file)));
        } else {
            parse(new FileInputStream(file));
        }        
    }

    private void parse(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        Map<Long, float[]> nodes = new HashMap<>();

        while(input.hasNext()){
            int tagKind = input.next();

            if(tagKind == XMLStreamConstants.START_ELEMENT){
                String type = input.getLocalName();
                switch (type) {
                    case "node" -> {
                        float[] cords = new float[2];
                        Long id = Long.parseLong(input.getAttributeValue(null, "id"));
                        cords[0] = Float.parseFloat(input.getAttributeValue(null, "lat"));
                        cords[1] = Float.parseFloat(input.getAttributeValue(null, "lon"));
                        nodes.put(id, cords);
                    }

                    case "bounds" -> {
                        minlat = Float.parseFloat(input.getAttributeValue(null, "minlat"));
                        maxlat = Float.parseFloat(input.getAttributeValue(null, "maxlat"));
                        minlon = Float.parseFloat(input.getAttributeValue(null, "minlon"));
                        maxlon = Float.parseFloat(input.getAttributeValue(null, "maxlon"));
                        System.out.println("Bounds found");
                        System.out.println(minlat + " " + maxlat + " "+ minlon +" " + " " + maxlon);
                        chunkGenerator = new ChunkGenerator(minlat, maxlat, minlon, maxlon);
                    }

                    case "way" -> {
                        var coords = new ArrayList<Node>();
                        var tags = new ArrayList<String>();

                        while(input.hasNext()){
                            if(input.next() != XMLStreamConstants.START_ELEMENT) continue;
                            String innerType = input.getLocalName();
                            if(innerType.equals("nd")){
                                float[] temp = nodes.get(Long.parseLong(input.getAttributeValue(null, "ref")));
                                coords.add(new Node(temp[0], temp[1]));
                                //cords.add(temp[0]);
                                //cords.add(temp[1]);
                            } else if (innerType.equals("tag")) {
                                tags.add(input.getAttributeValue(null, "k"));
                                tags.add(input.getAttributeValue(null, "v"));
                            } else{
                                break;
                            }
                        }
                        
                        if (chunkGenerator == null) {
                            System.out.println("Chunkgenerator han not been made yet");
                        } else {
                            chunkGenerator.addWay(new Way(coords, tags));
                        }
                        ways.add(new Way(coords, tags));
                    }
                }
            }
        }
        chunkGenerator.writeFiles();
        chunkGenerator.printAll();
    }

    /*private void parse1(InputStream inputStream) throws IOException, XMLStreamException {
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        Map<Long, float[]> nodes = new HashMap<>();

        while(input.hasNext()){
            int tagKind = input.next();

            if(tagKind == XMLStreamConstants.START_ELEMENT){
                String type = input.getLocalName();
                switch (type) {
                    case "node" -> {
                        float[] cords = new float[2];
                        Long id = Long.parseLong(input.getAttributeValue(null, "id"));
                        cords[0] = Float.parseFloat(input.getAttributeValue(null, "lat"));
                        cords[1] = Float.parseFloat(input.getAttributeValue(null, "lon"));
                        nodes.put(id, cords);
                    }

                    case "bounds" -> {
                        minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
                        maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
                        minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
                        maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
                        System.out.println("Bounds found");
                        System.out.println(minlat + " " + maxlat + " "+ minlon +" " + " " + maxlon);
                    }

                    case "way" -> {
                        List<Float> cords = new ArrayList<>();
                        List<String> tags = new ArrayList<>();

                        while(input.hasNext()){
                            if(input.next() != XMLStreamConstants.START_ELEMENT) continue;
                            String innerType = input.getLocalName();
                            if(innerType.equals("nd")){
                                float[] temp = nodes.get(Long.parseLong(input.getAttributeValue(null, "ref")));
                                cords.add(temp[0]);
                                cords.add(temp[1]);
                            } else if (innerType.equals("tag")) {
                                tags.add(input.getAttributeValue(null, "k"));
                                tags.add(input.getAttributeValue(null, "v"));
                            } else{
                                break;
                            }
                        }

                        ways.add(new Way(cords, tags));
                    }
                }
            }
        }
    }*/
}
