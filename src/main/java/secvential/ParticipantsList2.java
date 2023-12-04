package secvential;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ParticipantsList2 {

    private Node head = null;
    private final List<Integer> blackList = new LinkedList<>();

    public void insert(Entry entry) {
        System.out.println("Inserting " + entry.getId() + " " + entry.getScore());
        printList();
        Node participant = findNode(entry.getId());
        System.out.println("Found " + participant);
        if (cheater(participant, entry)) {
            return;
        }
        if (head == null) {
            head = new Node(entry);
            return;
        }
        if (participant == null) {
            participant = new Node(entry);
            Node place = findPlace(entry);
            insertAfter(place, participant);
        } else {
            participant.getEntry().addScore(entry.getScore());
            if (!needsReorder(participant)) {
                return;
            }
            System.out.println("Reordering" + participant.getEntry().getScore() + " " + participant.getPrevious().getEntry().getScore() + " " + participant.getEntry().getId() + " " + participant.getPrevious().getEntry().getId());
            remove(participant);
            Node place = findPlace(participant.getEntry());
            insertAfter(place, participant);
        }
    }

    private boolean cheater(Node participant, Entry entry) {
        if (blackList.contains(entry.getId())) {
            return true;
        }
        if (entry.getScore() < 0) {
            blackList.add(entry.getId());
            if (participant != null) {
                remove(participant);
            }
            return true;
        }
        return false;
    }

    private void remove(Node participant) {
        System.out.println("Removing " + participant.getEntry().getId());
        if (participant.getPrevious() == null) {
            head = participant.getNext();
            if (head != null) {
                head.setPrevious(null);
            }
        } else {
            System.out.println("Previous " + participant.getPrevious().getEntry().getId());
            participant.getPrevious().setNext(participant.getNext());
            if (participant.getNext() != null) {
                System.out.println("Next " + participant.getNext().getEntry().getId());
                participant.getNext().setPrevious(participant.getPrevious());
            }
        }
        participant.setNext(null);
        participant.setPrevious(null);
    }

    private boolean needsReorder(Node participant) {
        if (participant.getPrevious() == null) {
            return false;
        }
        if (participant.getEntry().getScore() > participant.getPrevious().getEntry().getScore()) {
            return true;
        }
        return false;
    }

    private void insertAfter(Node place, Node participant) {
        if (place == null) {
            System.out.println("Inserting at head");
            System.out.println("Head " + head.getEntry().getId());
            System.out.println("Participant " + participant.getEntry().getId());
            participant.setNext(head);
            head.setPrevious(participant);
            head = participant;
        } else {
            System.out.println("Inserting after " + place.getEntry().getId());
            System.out.println("Participant " + participant.getEntry().getId());
            if (place.getNext() != null) {
                place.getNext().setPrevious(participant);
            }
            participant.setNext(place.getNext());
            participant.setPrevious(place);
            place.setNext(participant);
        }
    }

    private Node findNode(int id) {
        Node node = head;
        while (node != null) {
            if (node.getEntry().getId() == id) {
                return node;
            }
            node = node.getNext();
        }
        return null;
    }

    private Node findPlace(Entry entry) {
        Node node = head;
        Node prev = null;
        while (node != null) {
            if (node.getEntry().getScore() < entry.getScore()) {
                return prev;
            }
            prev = node;
            node = node.getNext();
        }
        return prev;
    }

    public void printToFile(String filename) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            Node node = head;
            while (node != null) {
                myWriter.write(node.getEntry().getId() + " " + node.getEntry().getScore() + "\n");
                node = node.getNext();
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printList() {
        Node node = head;
        Node prev = null;
        while (node != null) {
            System.out.println((node.getEntry().getId() + " " + node.getEntry().getScore()));
            prev = node;
            node = node.getNext();
            if(prev==node){
                System.out.println("Ciclu");
                break;
            }
        }
    }

}
