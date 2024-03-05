package itu.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Way {

    private final float[] cords;
    private final String[] tags;

    public Way(List<Float> nodes, List<String> tags){
        this.cords = new float[nodes.size()];
        this.tags = new String[tags.size()];

        for(int i = 0; i < this.cords.length; i++){
            this.cords[i] = nodes.get(i);
        }
        for(int i = 0; i < this.tags.length; i++){
            this.tags[i] = tags.get(i);
        }

    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Nodes:\n");
        for(int i = 0; i < cords.length; i += 2){
            builder.append("Node: ");
            builder.append(cords[i]);
            builder.append(" ");
            builder.append(cords[i+1]);
            builder.append("\n");
        }

        builder.append("Tags:\n");
        for(int i = 0; i < tags.length; i += 2){
            builder.append("Tag: k=");
            builder.append(tags[i]);
            builder.append(" v=");
            builder.append(tags[i+1]);
            builder.append("\n");
        }

        return builder.toString();
    }

}
