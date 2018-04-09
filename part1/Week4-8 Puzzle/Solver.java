import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private boolean solvable;
    private SearchNode goalNode;

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int moves;
        int manhattanPriority;
        SearchNode predecessor;

        public SearchNode(Board board, int moves, SearchNode predecessor) {
            this.board = board;
            this.moves = moves;
            this.manhattanPriority = moves + board.manhattan();
            this.predecessor = predecessor;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.manhattanPriority < that.manhattanPriority) return -1;
            else if (this.manhattanPriority > that.manhattanPriority) return 1;
            else return 0;
        }
    }

    private boolean finished(MinPQ<SearchNode> pq) {
        SearchNode current = pq.delMin();
        if (current.board.isGoal()) {
            goalNode = current;
            return true;
        } else {
            for (Board neighbor : current.board.neighbors()) {
                if (current.predecessor == null || !neighbor.equals(current.predecessor.board))
                    pq.insert(new SearchNode(neighbor, current.moves + 1, current));
            }
            return false;
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        MinPQ<SearchNode> boardPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        Board initialTwin = initial.twin();
        SearchNode initialNode = new SearchNode(initial, 0, null);
        SearchNode initialNodeTwin = new SearchNode(initialTwin, 0, null);
        boardPQ.insert(initialNode);
        twinPQ.insert(initialNodeTwin);
        while (true) {
            if (finished(boardPQ)) {
                solvable = true;
                break;
            } else if (finished(twinPQ)) {
                solvable = false;
                break;
            }
        }

    } // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return solvable;
    } // is the initial board solvable?

    public int moves() {
        if (solvable) {
            return goalNode.moves;
        } else {
            return -1;
        }
    } // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (solvable) {
            List<Board> solutionBoards = new ArrayList<>();
            SearchNode current = goalNode;
            while (current != null) {
                solutionBoards.add(current.board);
                current = current.predecessor;
            }
            Collections.reverse(solutionBoards);
            return solutionBoards;
        } else
            return null;
    } // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    } // solve a slider puzzle (given below)
}