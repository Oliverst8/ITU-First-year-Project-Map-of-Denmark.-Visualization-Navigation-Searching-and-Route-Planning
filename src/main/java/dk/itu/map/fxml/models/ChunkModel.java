package dk.itu.map.fxml.models;

public class ChunkModel {
    public String OSMFile;
    public int fileLength;
    public volatile int progress;

    /**
     * Creates a new ChunkModel
     * @param OSMFile The path to the OSM file
     */
    public ChunkModel(String OSMFile) {
        this.OSMFile = OSMFile;
        this.fileLength = 0;
        this.progress = 0;
    }
}
