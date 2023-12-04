package secvential;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Sequential {
    private final List<String> filesList;
    private final ParticipantsList2 participantsList;

    public Sequential(List<String> filesList, ParticipantsList2 participantsList) {
        this.filesList = filesList;
        this.participantsList = participantsList;
    }

    public void run() {
        for(String file: filesList){
            readFromFile(file);
        }
    }

    private void readFromFile(String file){
        Scanner scanner = new Scanner(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(file)));
        while(scanner.hasNextLine()){
            System.out.println("Reading from " + file);
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            int id = Integer.parseInt(tokens[0]);
            int score = Integer.parseInt(tokens[1]);
            Entry entry = new Entry(id, score);
            participantsList.insert(entry);
        }
    }
}
