import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            alphabet.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = alphabet.indexOf(c);
            alphabet.remove(index);
            alphabet.addFirst(c);
            BinaryStdOut.write(index, 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            alphabet.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char c = alphabet.remove(index);
            alphabet.addFirst(c);
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}