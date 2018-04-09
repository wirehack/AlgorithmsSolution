import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private String string;
    private int length;
    private Integer[] suffixes;

    private class SuffixArrayComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            int i = o1;
            int j = o2;
            while (string.charAt(i) == string.charAt(j)) {
                i++;
                j++;
                if (i == length())
                    i = 0;
                if (j == length())
                    j = 0;
                if (i == o1 && j == o2)
                    break;
            }
            if (string.charAt(i) > string.charAt(j))
                return 1;
            else if (string.charAt(i) < string.charAt(j))
                return -1;
            else
                return 0;
        }
    }

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        string = s;
        length = s.length();
        suffixes = new Integer[length];
        for (int i = 0; i < length; i++) {
            suffixes[i] = i;
        }
        Arrays.sort(suffixes, new SuffixArrayComparator());
    } // circular suffix array of s

    public int length() {
        return length;
    } // length of s

    public int index(int i) {
        if (i < 0 || i > length - 1)
            throw new IllegalArgumentException();
        return suffixes[i];
    } // returns index of ith sorted suffix

    public static void main(String[] args) {
        String testString = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(testString);
        StdOut.println(csa.length());
        for (int i = 0; i < csa.length(); i++) {
            StdOut.println(csa.index(i));
        }
    } // unit testing (required)
}