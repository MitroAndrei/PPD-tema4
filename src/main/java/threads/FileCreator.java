package threads;

import java.io.FileWriter;

public class FileCreator {
    public static void main(String[] args) {
        int nrParticipants = 5;
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 10; j++) {
                try {
                    FileWriter myWriter = new FileWriter("RezultateC"+i+"_P"+j+".txt");
                    for (int k = 1; k <= nrParticipants; k++) {
                        int id  = (i-1)*nrParticipants + k;
                        int sign = Math.random() < 0.1 ? -1 : 1;
                        int score = sign*id;
                        if(sign == -1){
                            myWriter.write(id+" "+score+"\n");
                        }
                        else{
//                            myWriter.write(id+" "+(int)(Math.random()*10+1)+"\n");
                            myWriter.write(id+" "+score+"\n");
                        }
                    }
                    myWriter.close();
                } catch (Exception e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
    }
}
