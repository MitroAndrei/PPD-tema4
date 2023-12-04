package secvential;

import threads.FileNameGenerator;

public class Main {
    public static void main(String[] args) {
        int nrThreads = 1;
        FileNameGenerator fileNameGenerator = new FileNameGenerator(nrThreads);
        ParticipantsList2 participantsList = new ParticipantsList2();
        Sequential sequential = new Sequential(fileNameGenerator.getFilesForThread(0), participantsList);
        sequential.run();
        participantsList.printToFile("Sequential.txt");
    }
}
