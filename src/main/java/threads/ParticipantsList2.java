package threads;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class ParticipantsList2 {

    private final Object lock = new Object();
    private Node head = null;

    private final List<Integer> blackList = new LinkedList<>();

    public void insert(Entry entry){
        synchronized (lock){
            if (head == null) {
                head = new Node(entry);
                return;
            }
            Node participant = findNode(entry.getId());
            if(cheater(participant,entry)){
                return;
            }
            if(participant==null) {
                participant = new Node(entry);
                Node place = findPlace(entry);
                insertAfter(place, participant);
            }else{
                participant.getEntry().addScore(entry.getScore());
                if(!needsReorder(participant)){
                    return;
                }
                remove(participant);
                Node place = findPlace(participant.getEntry());
                insertAfter(place, participant);
            }
        }
    }

    private boolean cheater(Node participant,Entry entry) {
        synchronized (lock){
            if(blackList.contains(entry.getId())){
                return true;
            }
            if(entry.getScore() < 0){
                blackList.add(entry.getId());
                if(participant != null){
                    remove(participant);
                }
                return true;
            }
            return false;
        }
    }

    private void remove(Node participant) {
        synchronized (lock) {
            if (participant.getPrevious() == null) {
                head = participant.getNext();
                if (head != null) {
                    head.setPrevious(null);
                }
            } else {
                participant.getPrevious().setNext(participant.getNext());
                if (participant.getNext() != null) {
                    participant.getNext().setPrevious(participant.getPrevious());
                }
            }
            participant.setNext(null);
            participant.setPrevious(null);
        }
    }

    private boolean needsReorder(Node participant) {
        if(participant.getPrevious() == null){
            return false;
        }
        if(participant.getEntry().getScore() > participant.getPrevious().getEntry().getScore()){
            return true;
        }
        return false;
    }

    private void insertAfter(Node place, Node participant) {
        synchronized (lock){
            if(place == null){
                participant.setNext(head);
                head.setPrevious(participant);
                head = participant;
            }else{
                if(place.getNext() != null){
                    place.getNext().setPrevious(participant);
                }
                participant.setNext(place.getNext());
                participant.setPrevious(place);
                place.setNext(participant);
            }
        }
    }

    private Node findNode(int id) {
        synchronized (lock){
            Node node = head;
            while(node != null){
                if(node.getEntry().getId() == id){
                    return node;
                }
                node = node.getNext();
            }
            return null;
        }
    }
    private Node findPlace(Entry entry){
        synchronized (lock){
            Node node = head;
            Node prev = null;
            while(node != null){
                if(node.getEntry().getScore() < entry.getScore()){
                    return prev;
                }
                prev = node;
                node = node.getNext();
            }
            return prev;
        }
    }

    public void printToFile(String filename){
        synchronized (lock){
            try {
                FileWriter myWriter = new FileWriter(filename);
                Node node = head;
                while(node != null){
                    myWriter.write(node.getEntry().getId() + " " + node.getEntry().getScore() + "\n");
                    node = node.getNext();
                }
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
