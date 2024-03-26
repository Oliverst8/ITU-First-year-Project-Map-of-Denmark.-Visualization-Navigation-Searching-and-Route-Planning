package dk.itu.map.fxml;

import javafx.fxml.FXML;

import javafx.scene.input.MouseEvent;

public class MapController {
    double lastX;
    double lastY;

    // @FXML
    // void setOnMousePressed(MouseEvent e) {
    //     lastX = e.getX();
    //     lastY = e.getY();
    // };

    // @FXML
    // void setOnMouseDragged(MouseEvent e) {
    //     if (e.isPrimaryButtonDown()) {

    //         double dx = e.getX() - lastX;
    //         double dy = e.getY() - lastY;
    //         view.pan(dx, dy);
    //     }

    //     lastX = e.getX();
    //     lastY = e.getY();
    // };

    // @FXML
    // void setOnScroll(MouseEvent e) {
    //     double factor = e.getDeltaY();
    //     view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
    // };

    // @FXML
    // void setOnActionZoomIn(MouseEvent e) {
    //     view.zoom(0, 0, 1.1);
    // };

    // @FXML
    // void setOnActionZoomOut(MouseEvent e) {
    //     view.zoom(0, 0, 1 / 1.1);
    // };
}
