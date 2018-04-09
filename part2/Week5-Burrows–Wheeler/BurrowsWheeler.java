import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        char[] t = new char[csa.length()];
        int first = -1;
        for (int i = 0; i < csa.length(); i++) {
            int order = csa.index(i);
            if (order == 0) {
                first = i;
                t[i] = s.charAt(s.length() - 1);
            } else {
                t[i] = s.charAt(order - 1);
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(t[i]);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int N = s.length();
        int[] next = new int[N];
        int[] count = new int[R + 1];
        char[] aux = new char[N];
        for (int i = 0; i < N; i++)
            count[s.charAt(i) + 1]++;
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < N; i++) {
            aux[count[s.charAt(i)]] = s.charAt(i);
            next[count[s.charAt(i)]++] = i;
        }
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(aux[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }
}