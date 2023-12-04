package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Test {
    private Map<Integer,Integer> sequentialResults= new HashMap<>();

    public void readSequential(String filename){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            int id = Integer.parseInt(tokens[0]);
            int score = Integer.parseInt(tokens[1]);
            sequentialResults.put(id,score);
        }
    }

    public void readParallel(String filename){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = scanner.nextLine();
        String[] tokens = line.split(" ");
        int previousId = Integer.parseInt(tokens[0]);
        int previousScore = Integer.parseInt(tokens[1]);
        if(sequentialResults.get(previousId) != previousScore){
            throw new RuntimeException("Wrong result for participant " + previousId);
        }
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            tokens = line.split(" ");
            int id = Integer.parseInt(tokens[0]);
            int score = Integer.parseInt(tokens[1]);
            if(sequentialResults.get(id) != score){
                throw new RuntimeException("Wrong result for participant " + id);
            }
            if(score > previousScore){
                throw new RuntimeException("Wrong order for participant " + id);
            }
        }
    }
}
