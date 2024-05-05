package dk.itu.map.parser;


import javafx.scene.control.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileProgress extends Thread{
    private ProgressBar progressBar;
    private long totalLines;
    private long progress;
    private File file;
    private boolean finishedProgress;

    public FileProgress(ProgressBar progressBar){
        this.progressBar = progressBar;
        finishedProgress = false;
    }

    @Override
    public void run(){

        if(progressBar == null) return;

        setTotalLines();

        while(!finishedProgress){
            progressBar.setProgress((double)progress / totalLines);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTotalLines() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.toURI()))) {
            totalLines = (long) (reader.lines().parallel().count() * 2.86);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateProgress() {
        progress++;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void finishProgress(){
        finishedProgress = true;
    }
}
