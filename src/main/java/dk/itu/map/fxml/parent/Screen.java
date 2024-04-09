package dk.itu.map.fxml.parent;

public abstract class Screen<M> {
    public String fxml;
    public M model; // state
    public Object controller; // functions
    public /*View */ /*Screen*/ Object view; // drawing

}
