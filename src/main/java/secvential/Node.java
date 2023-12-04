package secvential;


public class Node {
    private  Entry entry;
    private Node next;
    private Node previous;

    public Node( Entry entry) {
        this.entry = entry;
        this.next = null;
        this.previous = null;
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

    public  Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }
}
