import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int count;

    private class Node {
        Node next;
        Node previous;
        Item item;

        Node(Item item) {
            if (item == null)
                throw new IllegalArgumentException();
            this.item = item;
            next = null;
            previous = null;
        }
    }

    public Deque() {
        first = null;
        last = null;
        count = 0;
    } // construct an empty deque

    public boolean isEmpty() {
        return count == 0;
    } // is the deque empty?

    public int size() {
        return count;
    } // return the number of items on the deque

    public void addFirst(Item item) {
        if (isEmpty()) {
            first = last = new Node(item);
        } else {
            Node oldFirst = first;
            first = new Node(item);
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        count++;
    } // add the item to the front

    public void addLast(Item item) {
        if (isEmpty()) {
            first = last = new Node(item);
        } else {
            Node oldLast = last;
            last = new Node(item);
            oldLast.next = last;
            last.previous = oldLast;
        }
        count++;
    } // add the item to the end

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        count--;
        Item item = first.item;
        first = first.next;
        if (isEmpty()) {
            last = null;
        } else {
            first.previous = null;
        }

        return item;
    } // remove and return the item from the front

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        count--;
        Item item = last.item;
        last = last.previous;
        if (isEmpty()) {
            first = null;
        } else {
            last.next = null;
        }
        return item;
    } // remove and return the item from the end

    private class ItemIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new ItemIterator();
    } // return an iterator over items in order from front to end

    public static void main(String[] args) {
    } // unit testing (optional)
}