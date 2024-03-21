package dk.itu.map;

import javafx.geometry.Point2D;

public class Controller {
    float lastX;
    float lastY;

    public Controller(Model model, View view) {
        view.canvas.setOnMousePressed(e -> {
            lastX = (float) e.getX();
            lastY = (float) e.getY();
            Point2D p = view.convertTo2DPoint(lastX, lastY);
            float X = (float) p.getX()/0.56f;
            float Y = (float) p.getY()*-1;
            System.out.println("X: " + X + " Y: " + Y);
            System.out.println("Chunk: " + model.chunkHandler.coordsToChunkIndex(Y, X));
            view.chunkToBeloaded = model.chunkHandler.coordsToChunkIndex(Y, X);
        });

        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
            }

            lastX = (float) e.getX();
            lastY = (float) e.getY();
        });

        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });
    }
}
