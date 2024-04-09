package dk.itu.map.fxml.controllers;

import dk.itu.map.fxml.models.ChunkModel;

public class ChunkController {
    
    ChunkModel model;

    public ChunkController(ChunkModel model) {
        this.model = model;

        importMap(model.OSMFile, model.mapName);
    }

    
    /**
     * Imports a map from a file
     * @param filePath The path to the file
     * @param name The name of the map to be saved to
     */
    public void importMap(String filePath, String name) {
        // try {            
        //     if (!new File(dataPath + "/" + name + "/config").exists()) {
        //         OSMParser OSMParser = new OSMParser(new File(filePath), dataPath + "/" + name);
        //         OSMParser.load();
        //         System.out.println("Finished importing map!");
        //     };
        //     chunkLoader = new ChunkLoader(dataPath + "/" + name);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } catch (XMLStreamException e) {
        //     e.printStackTrace();
        // }
    }
}
