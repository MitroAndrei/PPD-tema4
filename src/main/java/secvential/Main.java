package secvential;

import threads.FileNameGenerator;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int nrThreads = 1;
        FileNameGenerator fileNameGenerator = new FileNameGenerator(nrThreads);
        ParticipantsList2 participantsList = new ParticipantsList2();
        Sequential sequential = new Sequential(fileNameGenerator.getFilesForThread(0), participantsList);
        Instant start = Instant.now();
        sequential.run();
        participantsList.printToFile("Sequential.txt");
        Instant end = Instant.now();
        System.out.println((double) Duration.between(start,end).getNano() / 1000000);
    }
}
