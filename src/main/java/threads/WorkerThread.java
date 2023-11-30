package threads;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WorkerThread extends Thread{
    private final MessageQueue queue;
    private final ParticipantsList participantsList;

    @Override
    public void run() {
        while(!queue.doneReading()){
            try {
                Entry entry = queue.take();
                participantsList.add(entry);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
