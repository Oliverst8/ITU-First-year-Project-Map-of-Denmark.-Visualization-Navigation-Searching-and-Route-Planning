package dk.itu.map.fxml.models;

public class ChunkModel {
    
    public String OSMFile;
    public String mapName;
    public int fileLength;
    public volatile int progress;

    public ChunkModel(String OSMFile, String mapName) {
        this.OSMFile = OSMFile;
        this.mapName = mapName;
        this.fileLength = 0;
        this.progress = 0;
    }
}
