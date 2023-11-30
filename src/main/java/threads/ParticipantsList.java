package threads;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ParticipantsList {
    private Node head;
    private final List<Integer> blackList = new LinkedList<>();

    public synchronized void add(Entry entry) {
        Node participant = checkBlackListed(entry);
        if (participant == null) {
            return;
        }
        participant.getEntry().addScore(entry.getScore());
        if (head == null) {
            head = participant;
        } else {
            Node node = head;
            if (!needsReorder(participant)) {
                return;
            }
            while (node.getNext() != null && node.getNext().getEntry().getScore() > participant.getEntry().getScore()) {
                node = node.getNext();
            }
            node.setNext(participant);
            participant.setNext(node.getNext());
            participant.setPrevious(node);
        }
    }

    public synchronized Entry remove(Node node) {
        if (node.getPrevious() != null) {
            node.getPrevious().setNext(node.getNext());
        }
        if (node.getNext() != null) {
            node.getNext().setPrevious(node.getPrevious());
        }
        return node.getEntry();
    }

    private synchronized Node findNode(int id) {
        Node node = head;
        while (node != null) {
            if (node.getEntry().getId() == id) {
                return node;
            }
            node = node.getNext();
        }
        return null;
    }

    private synchronized Node checkBlackListed(Entry entry) {
        if (blackList.contains(entry.getId())) {
            return null;
        }
        Node participant = findNode(entry.getId());
        if (entry.getScore() < 0) {
            blackList.add(entry.getId());
            if (participant != null) {
                remove(participant);
            }
            return null;
        }
        if (participant == null) {
            return new Node(entry);
        }
        return participant;
    }

    private synchronized boolean needsReorder(Node participant) {
        if (participant.hasNeighbours()) {
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

    public void printToFile(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            Node node = head;
            while (node != null) {
                fileWriter.write(node.getEntry().toString() + "\n");
                node = node.getNext();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
