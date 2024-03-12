package dk.itu.map;

public class Controller {
    double lastX;
    double lastY;

    public Controller(Model model, View view) {
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
            }

            lastX = e.getX();
            lastY = e.getY();
        });

        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });

        view.zoomIn.setOnAction(e -> view.zoom(0, 0, 1.1));
        view.zoomOut.setOnAction(e -> view.zoom(0, 0, 1 / 1.1));
    }
}
