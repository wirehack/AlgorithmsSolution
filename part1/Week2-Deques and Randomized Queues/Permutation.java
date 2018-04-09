import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int k = Integer.valueOf(args[0]);
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        Iterator<String> randomIterator = randomizedQueue.iterator();
        while (randomIterator.hasNext() && k > 0) {
            StdOut.println(randomIterator.next());
            k--;
        }
    }
}
