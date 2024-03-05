package itu.map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler {

    private final File file;

    //Temp variable to save loaded ways
    public ArrayList<Way> ways;

    /**
     * Initialises the filehandler
     * @param file
     */
    public FileHandler(File file){
        this.file = file;
    }

    /**
     * Decompresses the file attribute
     */
    private void decompress(){

    }

    public void load() throws IOException, XMLStreamException {
        InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(file));
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        Map<Long, float[]> nodes = new HashMap<>();

        ArrayList<Way> ways = new ArrayList<>();

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
        this.ways = ways;
    }



}
