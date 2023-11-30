package threads;

import lombok.Data;

@Data
public class Node {
    private Entry entry;
    private Node next;
    private Node previous;

    public Node(Entry entry) {
        this.entry = entry;
    }

    public boolean hasLeft() {
        return previous != null;
    }
    public boolean hasRight() {
        return next != null;
    }

    public boolean hasNeighbours() {
        return hasLeft() || hasRight();
    }
}
