import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    } // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int[][] distance = new int[nouns.length][nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                distance[i][j] = distance[j][i] = wordNet.distance(nouns[i], nouns[j]);
            }
        }
        int maxDistance = -1;
        int out = -1;
        for (int i = 0; i < nouns.length; i++) {
            int sum = 0;
            for (int j = 0; j < nouns.length; j++) {
                sum += distance[i][j];
            }
            if (sum > maxDistance) {
                maxDistance = sum;
                out = i;
            }
        }
        return nouns[out];
    } // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    } // see test client below
}