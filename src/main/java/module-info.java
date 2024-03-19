module dk.itu.map {
    requires java.xml;
    
    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;
    
    requires org.apache.commons.io;
    requires org.apache.commons.compress;
    
    opens dk.itu.map to javafx.fxml;

    exports dk.itu.map;
    exports dk.itu.map.parser;
    exports dk.itu.map.structures;
}