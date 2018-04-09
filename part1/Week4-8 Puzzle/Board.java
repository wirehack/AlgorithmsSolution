import java.util.ArrayList;

public class Board {
    private int hamming;
    private int manhattan;
    private int n;
    private char[][] blocks;
    private int blankRow;
    private int blankCol;

    public Board(int[][] blocks) {
        if (blocks == null)
            throw new IllegalArgumentException();
        n = blocks.length;
        this.blocks = new char[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = (char) blocks[i][j];
            }
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                } else if (blocks[i][j] != getIndex(i, j)) {
                    hamming++;
                    manhattan += Math.abs((blocks[i][j] - 1) / n - i) + Math.abs((blocks[i][j] - 1) % n - j);
                }
            }
    }     // construct a board from an n-by-n array of blocks

    private Board(char[][] blocks){
        if (blocks == null)
            throw new IllegalArgumentException();
        n = blocks.length;
        this.blocks = new char[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                } else if (blocks[i][j] != getIndex(i, j)) {
                    hamming++;
                    manhattan += Math.abs((blocks[i][j] - 1) / n - i) + Math.abs((blocks[i][j] - 1) % n - j);
                }
            }
    }

    private int getIndex(int row, int col) {
        return row * n + col + 1;
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return n;
    }// board dimension n

    public int hamming() {
        return hamming;
    }// number of blocks out of place

    public int manhattan() {
        return manhattan;
    }// sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        return manhattan == 0;
    }// is this board the goal board?

    private void exch(int i1, int j1, int i2, int j2) {
        char t = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = t;
    }

    public Board twin() {
        int i1 = 0, j1 = 0, i2 = 1, j2 = 1;
        if (blocks[i1][j1] == 0) {
            i1++;
        }
        if (blocks[i2][j2] == 0) {
            i2--;
        }
        exch(i1, j1, i2, j2);
        Board twinBoard = new Board(blocks);
        exch(i1, j1, i2, j2);
        return twinBoard;
    }// a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
            }
        return true;
    }// does this board equal y?

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        if (blankRow > 0) {
            exch(blankRow - 1, blankCol, blankRow, blankCol);
            neighbors.add(new Board(blocks));
            exch(blankRow - 1, blankCol, blankRow, blankCol);
        }
        if (blankRow < n - 1) {
            exch(blankRow + 1, blankCol, blankRow, blankCol);
            neighbors.add(new Board(blocks));
            exch(blankRow + 1, blankCol, blankRow, blankCol);
        }
        if (blankCol > 0) {
            exch(blankRow, blankCol - 1, blankRow, blankCol);
            neighbors.add(new Board(blocks));
            exch(blankRow, blankCol - 1, blankRow, blankCol);
        }
        if (blankCol < n - 1) {
            exch(blankRow, blankCol + 1, blankRow, blankCol);
            neighbors.add(new Board(blocks));
            exch(blankRow, blankCol + 1, blankRow, blankCol);
        }
        return neighbors;
    }// all neighboring boards

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int)blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }// string representation of this board (in the output format specified below)

    public static void main(String[] args) {
    }// unit tests (not graded)
}