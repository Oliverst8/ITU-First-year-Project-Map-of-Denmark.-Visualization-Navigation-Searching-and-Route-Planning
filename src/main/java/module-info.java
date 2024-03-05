module dk.itu.map {
    requires java.xml;

    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires org.apache.commons.compress;

    opens dk.itu.map to javafx.fxml;
    exports dk.itu.map;
}