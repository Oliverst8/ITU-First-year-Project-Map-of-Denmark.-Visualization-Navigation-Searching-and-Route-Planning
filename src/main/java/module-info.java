module dk.itu.map {
    requires java.xml;
    
    requires javafx.base;
    requires javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    
    requires org.apache.commons.io;
    requires org.apache.commons.compress;


    opens dk.itu.map to javafx.fxml;
    opens dk.itu.map.fxml to javafx.fxml;
    opens dk.itu.map.parser to javafx.fxml;
    opens dk.itu.map.structures to javafx.fxml;
    opens dk.itu.map.fxml.views to javafx.fxml;

    exports dk.itu.map;
    exports dk.itu.map.fxml;
    exports dk.itu.map.fxml.models;
    exports dk.itu.map.parser;
    exports dk.itu.map.structures;
    exports dk.itu.map.structures.ArrayLists;
}
