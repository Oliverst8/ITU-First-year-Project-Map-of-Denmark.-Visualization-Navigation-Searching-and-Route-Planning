package dk.itu.map.fxml.models;

public class HomeModel {
    
    // The path to the data folder
    private final String dataPath;
    
    public HomeModel() {
        dataPath = "maps";
    }

    public String getDataPath() {
        return dataPath;
    }
}
