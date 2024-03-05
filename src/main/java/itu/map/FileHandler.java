package itu.map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    private final File file;

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
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(reader);
        Map<Long, float[]> nodes = new HashMap<>();
        while(input.hasNext()){
            int tagKind = input.next();

            if(tagKind == XMLStreamConstants.START_ELEMENT){
                String name = input.getLocalName();
                if(name.equals("node")){
                    float[] cords = new float[2];
                    Long id = Long.parseLong(input.getAttributeValue(null, "id"));
                    cords[0] = Float.parseFloat(input.getAttributeValue(null, "lat"));
                    cords[1] = Float.parseFloat(input.getAttributeValue(null, "lon"));
                    nodes.put(id,cords);
                }
            }
        }
        System.out.println(nodes.size());
    }



}
