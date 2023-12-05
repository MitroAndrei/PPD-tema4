package main;

import threads.*;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int totalThreads = 16;
        int readThreads = 2;
        FileNameGenerator fileNameGenerator = new FileNameGenerator(readThreads);
        MessageQueue messageQueue = new MessageQueue(readThreads);
        ParticipantsList2 participantsList = new ParticipantsList2();
        Instant start = Instant.now();
        Thread[] threads = new Thread[totalThreads];
        for(int i = 0;i<readThreads;i++){
            threads[i] = new ReaderThread(i,messageQueue, fileNameGenerator.getFilesForThread(i));
            threads[i].start();
        }
        for(int i = 0;i<totalThreads-readThreads;i++){
            threads[i+readThreads] = new WorkerThread(i+readThreads,messageQueue, participantsList);;
            threads[i+readThreads].start();
        }
        for (int i = 0; i < totalThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String filename = "Parallel.txt";
        participantsList.printToFile(filename);
        Instant end = Instant.now();
        Test test = new Test();
        test.readSequential("Sequential.txt");
        test.readParallel("Parallel.txt");
        System.out.println((double) Duration.between(start,end).getNano()/1000000);
    }
}
