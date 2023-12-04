package threads;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final Object notEmpty = new Object();
    private final Object notFull = new Object();
    private final Deque<Entry> queue = new LinkedList<>();

    private final Boolean[] readersFinished;

    private volatile boolean isWorking = true;

    public MessageQueue(int readersCount) {
        readersFinished = new Boolean[readersCount];
        for (int i = 0; i < readersCount; i++) {
            readersFinished[i] = false;
        }
    }

    public void put(Entry entry) throws InterruptedException {
        synchronized (queue){
            queue.add(entry);
//             System.out.println("Reader " + entry.getId() + " put " + entry + " in queue" + " size: " + queue.size());
            queue.notifyAll();
        }
    }

    public Entry take(int id) throws InterruptedException {
        synchronized (queue){
            while (shouldWait() ) {
                queue.wait();
            }
            if (queue.isEmpty() && !isWorking) {
                return null; // No more items to consume, exit the method
            }
            Entry entry = queue.remove();
            queue.notifyAll();
//            System.out.println("Worker "+id +" notified " + queue.size());
            return entry;
        }
    }

    public  boolean canRead(){
        synchronized (queue) {
            if (!isWorking) {
                return !queue.isEmpty();
            }
            return true;
        }
    }

    public boolean shouldWait(){
        synchronized (queue) {
            if(!isWorking){
                System.out.println(!queue.isEmpty());
                return false;
            }
            else {
                return queue.isEmpty();
            }
        }
    }



    public synchronized int size(){
        return queue.size();
    }

    public  void setReadersFinished(int id) {
        synchronized (queue) {
            readersFinished[id] = true;
            for (Boolean readerFinished : readersFinished) {
                if (!readerFinished) {
                    return;
                }
            }
            this.isWorking = false;
            queue.notifyAll();
        }
    }
}
