import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int count;
    private WeightedQuickUnionUF wquf;
    private WeightedQuickUnionUF wqufForFull;
    private int n;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[n][n];
        count = 0;
        this.n = n;
        wquf = new WeightedQuickUnionUF(n * n + 2);
        wqufForFull = new WeightedQuickUnionUF(n * n + 1);
    }             // create n-by-n grid, with all sites blocked

    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            count++;
            if (row == 1) {
                wquf.union(0, col);
                wqufForFull.union(0, col);
            }
            if (row == n) {
                wquf.union(n * (n - 1) + col, n * n + 1);
            }
            if (row - 1 >= 1 && isOpen(row - 1, col)) {
                wquf.union((row - 2) * n + col, (row - 1) * n + col);
                wqufForFull.union((row - 2) * n + col, (row - 1) * n + col);
            }
            if (row + 1 <= n && isOpen(row + 1, col)) {
                wquf.union(row * n + col, (row - 1) * n + col);
                wqufForFull.union(row * n + col, (row - 1) * n + col);
            }
            if (col - 1 >= 1 && isOpen(row, col - 1)) {
                wquf.union((row - 1) * n + col - 1, (row - 1) * n + col);
                wqufForFull.union((row - 1) * n + col - 1, (row - 1) * n + col);
            }
            if (col + 1 <= n && isOpen(row, col + 1)) {
                wquf.union((row - 1) * n + col + 1, (row - 1) * n + col);
                wqufForFull.union((row - 1) * n + col + 1, (row - 1) * n + col);
            }
        }
    }  // open site (row, col) if it is not open already

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    } // is site (row, col) open?

    public boolean isFull(int row, int col) {
        validate(row, col);
        return wqufForFull.connected(0, (row - 1) * n + col);
    } // is site (row, col) full?

    public int numberOfOpenSites() {
        return count;
    } // number of open sites

    public boolean percolates() {
        return wquf.connected(0, n * n + 1);
    } // does the system percolate?

    private void validate(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n)
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        boolean x;
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
            x = percolation.isFull(p, q);
        }
        System.out.println(percolation.numberOfOpenSites());
        System.out.println(percolation.percolates());
    }
}