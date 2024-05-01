package dk.itu.map.parser;


import javafx.scene.control.ProgressBar;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

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

        setTotalLines();
        System.out.println("Total lines: " + totalLines);

        while(!finishedProgress){
            //progressBar.setProgress(Math.min(((double)progress / totalLines), 0.98));
            progressBar.setProgress((double)progress / totalLines);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Finished progress bar!");
        System.out.println("Real number: " + progress + " total lines: " + totalLines);
    }

    public void setTotalLines() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.toURI()))) {
            totalLines = (long) (reader.lines().parallel().count() * 2.86);
            /*totalLines = 0;
            reader.lines().parallel().forEach((line) -> {
                for(char c : line.toCharArray()){
                    if(c == '<' || c == '>'){
                        totalLines++;
                    }
                }
            });*/
            System.out.println("Amount of lines: " + totalLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*XMLInputFactory factory = XMLInputFactory.newInstance();
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            while (true) {
                reader.next();
                totalLines++;
            }
        } catch (NoSuchElementException e){
            //Do Nothing
    }   catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    public void updateProgress() {
        progress++;
        //System.out.println("Progress: " + progress + " / " + totalLines + " = " + progressBar.getProgress());
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void finishProgress(){
        finishedProgress = true;
    }
}
