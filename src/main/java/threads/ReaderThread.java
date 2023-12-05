package threads;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@AllArgsConstructor
public class ReaderThread extends Thread{
    private final int id;
    private final MessageQueue queue;
    private final List<String> filesList;

    @Override
    public void run() {
        for(String file: filesList){
            readFromFile(file);
        }
        queue.setReadersFinished(id);
//        System.out.println("Reader " + id + " finished reading");
    }

    private void readFromFile(String file){
        Scanner scanner = new Scanner(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(file)));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            int id = Integer.parseInt(tokens[0]);
            int score = Integer.parseInt(tokens[1]);
            Entry entry = new Entry(id, score);
            try {
//                System.out.println("Reader " + this.id + " read " + entry + " from " + file);
                queue.put(entry);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
