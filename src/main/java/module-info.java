module dk.itu.map {
    requires java.xml;
    
    requires javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    
    requires org.apache.commons.io;
    requires org.apache.commons.compress;
    
    opens dk.itu.map to javafx.fxml;
    opens dk.itu.map.fxml to javafx.fxml;

    exports dk.itu.map;
    exports dk.itu.map.fxml;
    exports dk.itu.map.parser;
    exports dk.itu.map.structures;
    opens dk.itu.map.structures to javafx.fxml;
    exports dk.itu.map.structures.ArrayLists;
    opens dk.itu.map.structures.ArrayLists to javafx.fxml;
    exports dk.itu.map.structures.HashMaps;
    opens dk.itu.map.structures.HashMaps to javafx.fxml;
}