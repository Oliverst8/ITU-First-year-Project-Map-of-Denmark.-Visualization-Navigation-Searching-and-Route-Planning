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

    /**
     * Creates a new FileProgress
     * @param progressBar The progress bar to be used
     */
    public FileProgress(ProgressBar progressBar){
        this.progressBar = progressBar;
        finishedProgress = false;
    }

    @Override
    public void run(){

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

    /**
     * Sets the total number of lines in the current file
     */
    public void setTotalLines() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.toURI()))) {
            totalLines = (long) (reader.lines().parallel().count() * 2.86);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the progress of the file
     */
    public void updateProgress() {
        progress++;
    }

    /**
     * Sets the file to be used
     * @param file The file to be used
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Finishes the progress
     */
    public void finishProgress(){
        finishedProgress = true;
    }
}
