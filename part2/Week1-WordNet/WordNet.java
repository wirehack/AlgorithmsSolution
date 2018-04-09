import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private Map<Integer, String> synsetMap;
    private Map<String, Set<Integer>> word2Id;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validate(synsets);
        validate(hypernyms);
        synsetMap=new HashMap<>();
        word2Id=new HashMap<>();
        parseSynsets(synsets);
        G = new Digraph(synsetMap.size());
        parseHypernyms(hypernyms);
        validateGraph(G);
        sap = new SAP(G);
    }

    private void parseSynsets(String synsets) {
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] line = in.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String[] nouns = line[1].split(" ");
            for (String noun : nouns) {
                if (!word2Id.containsKey(noun))
                    word2Id.put(noun, new HashSet<>());
                word2Id.get(noun).add(id);
            }
            synsetMap.put(id, line[1]);
        }
    }

    private void parseHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] line = in.readLine().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                G.addEdge(v, Integer.parseInt(line[i]));
            }
        }
    }

    private void validateGraph(Digraph G) {
        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle())
            throw new IllegalArgumentException();
        int root = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0)
                root++;
        }
        if (root != 1)
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2Id.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validate(word);
        return word2Id.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        return sap.length(word2Id.get(nounA), word2Id.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        int ancestorId = sap.ancestor(word2Id.get(nounA), word2Id.get(nounB));
        return synsetMap.get(ancestorId);
    }

    private void validate(Object o) {
        if (o == null)
            throw new IllegalArgumentException();
    }

    private void validateNoun(String noun) {
        if (noun == null || !isNoun(noun))
            throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);

    }
}