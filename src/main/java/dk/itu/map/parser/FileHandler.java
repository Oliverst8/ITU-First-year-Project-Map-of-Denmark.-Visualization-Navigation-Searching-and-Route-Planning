package dk.itu.map.parser;

import dk.itu.map.structures.CoordArrayList;
import dk.itu.map.structures.LongFloatArrayHashMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.FactoryConfigurationError;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class FileHandler {
    private final File file;
    private final String dataPath;

    private ArrayList<MapElement> relations;
    private float minlat, maxlat, minlon, maxlon;
    private LongFloatArrayHashMap nodes;
    private Map<Long, LinkedList<Polygon>> relationMap;

    private ChunkGenerator chunkGenerator;

    /**
     * Initialises the filehandler
     *
     * @param file
     */
    public FileHandler(File file, String dataPath) {
        this.file = file;
        this.dataPath = dataPath;

        relations = new ArrayList<>();
        relationMap = new HashMap<>();
    }

    public void load() throws IOException, XMLStreamException {
        if (file.getName().contains("bz2")) {
            parse(new BZip2CompressorInputStream(new FileInputStream(file)));
        } else {
            try {
                ReversedLinesFileReader relationReader = ReversedLinesFileReader.builder().setFile(file).get();
                parseRelations(relationReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
            parse(new FileInputStream(file));
        }
    }

    private void parse(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        nodes = new LongFloatArrayHashMap();
        long startLoadTime = System.nanoTime();

        while (input.hasNext()) {
            int tagKind = input.next();

            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                String type = input.getLocalName();
                switch (type) {
                    case "bounds" -> {
                        minlat = Float.parseFloat(input.getAttributeValue(null, "minlat"));
                        maxlat = Float.parseFloat(input.getAttributeValue(null, "maxlat"));
                        minlon = Float.parseFloat(input.getAttributeValue(null, "minlon"));
                        maxlon = Float.parseFloat(input.getAttributeValue(null, "maxlon"));
                        chunkGenerator = new ChunkGenerator(dataPath, minlat, maxlat, minlon, maxlon);
                    }

                    case "node" -> {
                        float[] cords = new float[2];
                        Long id = Long.parseLong(input.getAttributeValue(null, "id"));
                        cords[0] = Float.parseFloat(input.getAttributeValue(null, "lat"));
                        cords[1] = Float.parseFloat(input.getAttributeValue(null, "lon"));
                        nodes.put(id, cords);
                    }

                    case "way" -> {
                        long id = Long.parseLong(input.getAttributeValue(null, "id"));
                        createWay(input, nodes, id);
                    }
                }
            }
        }

        long startWriteTime = System.nanoTime();
        System.out.println("Reading took: " + ((startWriteTime - startLoadTime) / 1_000_000_000.0) + "s");

        relations.forEach(relation -> {
            chunkGenerator.addWay(relation);
        });

        chunkGenerator.finishWork();
        long endWriteTime = System.nanoTime();
        System.out.println("Writing took: " + ((endWriteTime - startWriteTime) / 1_000_000_000.0) + "s");
    }

    private void parseRelations(ReversedLinesFileReader reader) throws IOException {
        nextRelation:
        for (String line = reader.readLine().trim(); !line.equals("</way>"); line = reader.readLine().trim()) {
            if (!line.equals("</relation>")) continue;

            List<String> tags = new ArrayList<>();
            List<Long> outer = new ArrayList<>();
            List<Long> inner = new ArrayList<>();
            for (line = reader.readLine().trim(); !line.startsWith("<relation"); line = reader.readLine().trim()) {
                if (line.startsWith("<tag")) {
                    String[] temp = line.split("\"");
                    tags.add(temp[1]);
                    tags.add(temp[3]);
                    if (temp[1].equals("type") && !temp[3].equals("multipolygon")) {
                        continue nextRelation;
                    }
                    continue;
                }
                if (line.startsWith("<member")) {
                    String[] temp = line.split("\"");
                    long ref = Long.parseLong(temp[3]);
                    if (temp[5].equals("outer")) {
                        outer.add(ref);
                    } else if (temp[5].equals("inner")) {
                        inner.add(ref);
                    }
                }
            }
            
            if (outer.isEmpty()) continue;

            long id = Long.parseLong(line.split("\"")[1]);

            Polygon polygon = new Polygon(id, tags, outer, inner);
            relations.add(polygon);
            outer.forEach(ref -> {
                if (relationMap.containsKey(ref)) {
                    relationMap.get(ref).add(polygon);
                } else {
                    relationMap.put(ref, new LinkedList<>());
                    relationMap.get(ref).add(polygon);
                }
            });
            inner.forEach(ref -> {
                if (relationMap.containsKey(ref)) {
                    relationMap.get(ref).add(polygon);
                } else {
                    relationMap.put(ref, new LinkedList<>());
                    relationMap.get(ref).add(polygon);
                }
            });
        }
    }

    private void createWay(XMLStreamReader input, LongFloatArrayHashMap nodes, long id) throws XMLStreamException {
        CoordArrayList coords = new CoordArrayList();
        List<String> tags = new ArrayList<>();
        long[] nodeIds = new long[]{-1,0};
        while (input.hasNext()) {
            int eventType = input.next();
            if (eventType != XMLStreamConstants.START_ELEMENT) {
                if (eventType == XMLStreamConstants.END_ELEMENT && input.getLocalName().equals("way")) break;
                continue;
            }

            String innerType = input.getLocalName();
            if (innerType.equals("nd")) {

                Long node = Long.parseLong(input.getAttributeValue(null, "ref"));

                if (nodeIds[0] == -1) {
                    nodeIds[0] = node;
                } else {
                    nodeIds[1] = node;
                }

                float[] temp = nodes.get(node);

                coords.add(temp[0]);
                coords.add(temp[1]);
            } else if (innerType.equals("tag")) {
                tags.add(input.getAttributeValue(null, "k"));
                tags.add(input.getAttributeValue(null, "v"));
            } else {
                break;
            }
        }

        Way way = new Way(id, tags, coords);

        if (relationMap.containsKey(id)) {
            relationMap.get(id).forEach(relation -> {
                relation.addWay(way);
            });
        }

        if (chunkGenerator == null) {
            System.err.println("Chunkgenerator han not been made yet");
        } else {
            chunkGenerator.addWay(way);
        }
    }
}
