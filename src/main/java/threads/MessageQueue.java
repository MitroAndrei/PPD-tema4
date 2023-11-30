package threads;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final Object notEmpty = new Object();
    private final Object notFull = new Object();
    private final Queue<Entry> queue = new LinkedList<>();

    private final Boolean[] readersFinished;

    public MessageQueue(int readersCount) {
        readersFinished = new Boolean[readersCount];
        for (int i = 0; i < readersCount; i++) {
            readersFinished[i] = false;
        }
    }

    public synchronized void put(Entry entry) throws InterruptedException {
        while (queue.size() >= 100) {
            notFull.wait();
        }
        queue.add(entry);
        notEmpty.notifyAll();
    }

    public synchronized Entry take() throws InterruptedException {
        while (queue.isEmpty()) {
            notEmpty.wait();
        }
        Entry entry = queue.remove();
        notFull.notifyAll();
        return entry;
    }

    public synchronized boolean doneReading(){
        for(Boolean readerFinished: readersFinished){
            if(!readerFinished){
                return false;
            }
        }
        return true;
    }

}
