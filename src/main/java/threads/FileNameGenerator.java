package threads;

import java.util.ArrayList;
import java.util.List;

public class FileNameGenerator {
    private final int nrThreads;
    private final List<String> filesList;

    public FileNameGenerator(int nrThreads) {
        this.nrThreads = nrThreads;
        filesList = new ArrayList<>();
        generateFiles();
    }

    public void generateFiles(){
        for(int i = 1;i<=5;i++){
            for(int j = 1;j<=10;j++){
                filesList.add("src/main/resources/threads/RezultateC"+i+"_P"+j+".txt");
            }
        }
    }

    public List<String> getFilesForThread(int id){
        return filesList.subList(id*filesList.size()/nrThreads, (id+1)*filesList.size()/nrThreads);
    }
}
