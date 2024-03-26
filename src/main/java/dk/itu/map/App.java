package dk.itu.map;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Controller().getView());
        
        stage.setTitle("MapIT");
        stage.setScene(scene);
        stage.show();
    }
}
