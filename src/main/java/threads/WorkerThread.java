package threads;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WorkerThread extends Thread{
    private final int id;
    private final MessageQueue queue;
    private final ParticipantsList2 participantsList;
    private int sc=0;

    public WorkerThread(int id, MessageQueue queue, ParticipantsList2 participantsList) {
        this.id = id;
        this.queue = queue;
        this.participantsList = participantsList;
    }

    @Override
    public void run() {
        while(true){
            try {
                Entry entry = queue.take(this.id);
                if(entry == null){
                    System.out.println("Worker " + id + " score " + sc);
                    break;
                }
                sc++;
//                System.out.println("entry " + id + ":" + entry);
                participantsList.insert(entry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
