package main;

import threads.*;

public class Main {
    public static void main(String[] args) {
        while (true) {
            int totalThreads = 4;
            int readThreads = 2;
            FileNameGenerator fileNameGenerator = new FileNameGenerator(readThreads);
            MessageQueue messageQueue = new MessageQueue(readThreads);
            ParticipantsList2 participantsList = new ParticipantsList2();
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
            String filename = "Rezultate.txt";
            participantsList.printToFile(filename);
            break;
        }


    }
}
