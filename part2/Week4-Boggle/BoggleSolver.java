import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;


public class BoggleSolver {
    private Trie dictionaryTrie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictionaryTrie = new Trie();
        for (String word : dictionary) {
            dictionaryTrie.put(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new HashSet<>();
        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++) {
                StringBuilder prefix = new StringBuilder();
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                dfs(board, prefix, marked, i, j, validWords);
            }
        return validWords;
    }

    private void dfs(BoggleBoard board, StringBuilder prefix, boolean[][] marked, int i, int j, Set<String> validWords) {
        if (i < 0 || i > board.rows() - 1 || j < 0 || j > board.cols() - 1 || marked[i][j]) return;
        char c = board.getLetter(i, j);
        if (c == 'Q') {
            prefix.append("QU");
        } else {
            prefix.append(c);
        }
        marked[i][j] = true;
        String word = prefix.toString();
        if (dictionaryTrie.containsPrefix(word)) {
            if (word.length() >= 3 && dictionaryTrie.contains(word)) {
                validWords.add(word);
            }
            dfs(board, prefix, marked, i - 1, j, validWords);
            dfs(board, prefix, marked, i - 1, j - 1, validWords);
            dfs(board, prefix, marked, i - 1, j + 1, validWords);
            dfs(board, prefix, marked, i, j - 1, validWords);
            dfs(board, prefix, marked, i, j + 1, validWords);
            dfs(board, prefix, marked, i + 1, j, validWords);
            dfs(board, prefix, marked, i + 1, j - 1, validWords);
            dfs(board, prefix, marked, i + 1, j + 1, validWords);
        }
        if (c == 'Q') {
            prefix.delete(prefix.length() - 2, prefix.length());
        } else {
            prefix.deleteCharAt(prefix.length() - 1);
        }
        marked[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionaryTrie.contains(word))
            return 0;
        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}