package main;

import threads.*;

public class Main {
    public static void main(String[] args) {
        int totalThreads = 4;
        int readThreads = 2;
        FileNameGenerator fileNameGenerator = new FileNameGenerator(readThreads);
        MessageQueue messageQueue = new MessageQueue(readThreads);
        ParticipantsList participantsList = new ParticipantsList();
        Thread[] threads = new Thread[totalThreads];
        for(int i = 0;i<readThreads;i++){
            ReaderThread readerThread = new ReaderThread(i,messageQueue, fileNameGenerator.getFilesForThread(i));
            threads[i] = readerThread;
            readerThread.start();
        }
        for(int i = 0;i<totalThreads-readThreads;i++){
            WorkerThread workerThread = new WorkerThread(i+readThreads,messageQueue, participantsList);
            threads[i+readThreads] = workerThread;
            workerThread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String filename = "src/main/resources/threads/Rezultate.txt";
        participantsList.printToFile(filename);
    }
}
