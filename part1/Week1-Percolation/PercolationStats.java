import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int trials;
    private double[] x;
    private double mean;
    private double stddev;
    private final double CC = 1.96;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.trials = trials;
        x = new double[trials];
        for (int t = 0; t < trials; t++) {
            int row;
            int col;
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                do {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                } while (percolation.isOpen(row, col));
                percolation.open(row, col);
            }
            x[t] = percolation.numberOfOpenSites() / (double) (n * n);
        }
        mean = StdStats.mean(x);
        stddev = StdStats.stddev(x);
    }  // perform trials independent experiments on an n-by-n grid

    public double mean() {
        return mean;
    }  // sample mean of percolation threshold

    public double stddev() {
        return stddev;
    }  // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return mean - CC * stddev / Math.sqrt(trials);
    }  // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return mean + CC * stddev / Math.sqrt(trials);
    }  // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean\t= " + ps.mean());
        StdOut.println("stddev\t= " + ps.stddev());
        StdOut.println("95% confidence interval\t= [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}