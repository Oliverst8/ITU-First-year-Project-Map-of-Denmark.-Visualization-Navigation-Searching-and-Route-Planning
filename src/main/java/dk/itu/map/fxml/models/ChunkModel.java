package dk.itu.map.fxml.models;

public class ChunkModel {
    
    public String OSMFile;
    public int fileLength;
    public volatile int progress;

    public ChunkModel(String OSMFile) {
        this.OSMFile = OSMFile;
        this.fileLength = 0;
        this.progress = 0;
    }
}
