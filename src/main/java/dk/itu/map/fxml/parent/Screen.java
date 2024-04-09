package dk.itu.map.fxml.parent;

import dk.itu.map.Model;
import dk.itu.map.fxml.Controller;

public abstract class Screen {
    public String fxml;
    public Model model; // state
    public Controller controller; // functions
    public /*View */ Screen view; // drawing

}
