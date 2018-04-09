import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] items;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    } // construct an empty randomized queue

    public boolean isEmpty() {
        return N == 0;
    } // is the randomized queue empty?

    public int size() {
        return N;
    } // return the number of items on the randomized queue

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (N == items.length) resize(2 * items.length);
        items[N++] = item;
    } // add the item

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (N <= items.length / 4) resize(items.length / 4);
        swap(StdRandom.uniform(N), N - 1);
        Item item = items[--N];
        items[N] = null;
        return item;
    } // remove and return a random item

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(N)];
    } // return a random item (but do not remove it)

    private void swap(int i, int j) {
        Item tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    private class RandomItemIterator implements Iterator<Item> {
        private Item[] itemsCopy;
        private int length;

        RandomItemIterator(Item[] items, int length) {
            this.length = length;
            itemsCopy = (Item[]) new Object[length];
            System.arraycopy(items, 0, itemsCopy, 0, length);
            StdRandom.shuffle(itemsCopy);
        }

        @Override
        public boolean hasNext() {
            return length > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = itemsCopy[--length];
            itemsCopy[length] = null;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new RandomItemIterator(items, N);
    } // return an independent iterator over items in random order

    private void resize(int capacity) {
        Item[] oldItems = items;
        items = (Item[]) new Object[capacity];
        System.arraycopy(oldItems, 0, items, 0, N);
    }

    public static void main(String[] args) {
    } // unit testing (optional)
}