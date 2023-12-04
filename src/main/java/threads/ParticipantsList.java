package threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ParticipantsList {
    private Node head = null;
    private final Object lock = new Object();
    private final List<Integer> blackList = new LinkedList<>();


    public void insert(Entry entry){
        synchronized (lock){
            System.out.println("insert "+entry.getId());
//            Node participant = new Node(entry);
//            Node participant = checkBlackListed(entry);
            Node participant = findNode(entry.getId());
            if(participant==null){
                System.out.println("new");
                participant = new Node(entry);
                addNode(participant);
            }else {
                System.out.println("old");
                participant.getEntry().addScore(entry.getScore());
                if(!needsReorder(participant)){
                    System.out.println("not reordering");
                    return;
                }
                System.out.println("reordering");
//                remove(participant);
//                addNode(participant);
            }
        }
    }

    private void addNode(Node participant) {
        synchronized (lock) {
            if (head == null) {
                head = participant;
            } else {
                Node node = head;
                while (node.getNext() != null && node.getNext().getEntry().getScore() > participant.getEntry().getScore()) {
//                while (node.getNext() != null) {
                    node = node.getNext();
                }
                if (node == head) {
                    head.setNext(participant);
                    participant.setPrevious(head);
                } else {
                    node.setNext(participant);
                    participant.setPrevious(node);
                }
            }
        }
    }

    public  void add(Entry entry) {
        synchronized (lock) {
            System.out.println("adadsadasdad "+entry);
            Node participant = checkBlackListed(entry);
            if (participant == null) {
                System.out.println("Participant " + entry.getId() + " blacklisted");
                return;
            }
            if (head == null) {
                head = participant;
                System.out.println("Participant " + entry.getId() + " added to head");
            } else {
                participant.getEntry().addScore(entry.getScore());
                Node node = head;
                if (!needsReorder(participant)) {
                    System.out.println("Participant " + entry.getId() + " not reordered");
                    return;
                }
                while (node.getNext() != null && node.getNext().getEntry().getScore() > participant.getEntry().getScore()) {
                    node = node.getNext();
                }
                if(node.getEntry().getId()==participant.getEntry().getId()){
                    System.out.println("Participant " + entry.getId() + " already in list");
                    return;
                }
                if(node==head){
                    if(head.getEntry().getScore() < participant.getEntry().getScore()){
                        System.out.println("Participant " + entry.getId() + " added before head");
                        participant.setNext(head);
                        head.setPrevious(participant);
                        head = participant;
                    }
                    else {
                        System.out.println("Participant " + entry.getId() + " added after head");
                        head.setNext(participant);
                        participant.setPrevious(head);
                    }
                }
                else{
                    System.out.println("Participant " + entry + " added after " + node);
                    node.setNext(participant);
                    participant.setNext(node.getNext());
                    participant.setPrevious(node);
                }
            }
        }
    }

    public  Entry remove(Node node) {
        synchronized (lock) {
            if (node.getPrevious() != null) {
                System.out.println("left");
                node.getPrevious().setNext(node.getNext());
            }
            if (node.getNext() != null) {
                System.out.println("right");
                node.getNext().setPrevious(node.getPrevious());
            }
            return node.getEntry();
        }
    }

    private  Node findNode(int id) {
        synchronized (lock) {
            Node toReturn = null;
            Node node = head;
            while (node!=null){
                if(id==node.getEntry().getId()){
                    System.out.println("Da");
                    toReturn = node;
                    break;
                }
                node = node.getNext();
            }
            return toReturn;
        }
    }

    private  Node checkBlackListed(Entry entry) {
        synchronized (lock) {
            if (blackList.contains(entry.getId())) {
                return null;
            }
            Node participant = findNode(entry.getId());
//            Node participant = new Node(entry);
            System.out.println("Participant " + entry.getId() + " found: " + participant);
            if (entry.getScore() < 0) {
                blackList.add(entry.getId());
                if (participant != null) {
                    remove(participant);
                }
                return null;
            }
            if (participant == null) {
                System.out.println("neeeeeeeeeeeeeeeeeeeeeeeeew");
                return new Node(entry);
            }
            System.out.println("oooooooooold");
            return participant;
        }
    }

    private  boolean needsReorder(Node participant) {
        synchronized (lock) {
            if (participant.hasNeighbours()) {
                System.out.println("Participant " + participant.getEntry().getId() + " has neighbours");
                if (participant.hasLeft()) {
                    if (participant.getEntry().getScore() < participant.getPrevious().getEntry().getScore()) {
                        return false;
                    }
                    participant.getPrevious().setNext(participant.getNext());
                }
                if (participant.hasRight()) {
                    participant.getNext().setPrevious(participant.getPrevious());
                }
            }
            return true;
        }
    }

    public void printToFile(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            Node node = head;
            while (node != null) {
                System.out.println(node.getEntry().getId() + " " + node.getEntry().getScore());
                fileWriter.write(node.getEntry().getId() + " " + node.getEntry().getScore() + "\n");
                node = node.getNext();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
