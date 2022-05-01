/* *****************************************************************************
 *  Name:              Eric Lin
 *  Last modified:     January 21, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] threshold;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("out of range");
        }

        threshold = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!percolation.isOpen(row, col))
                    percolation.open(row, col);
            }
            threshold[i] = (double) (percolation.numberOfOpenSites()) / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(threshold.length));

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(threshold.length));
    }

    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]),
                                                                 Integer.parseInt(args[1]));
        System.out.print("mean                    = ");
        System.out.println(percolationStats.mean());
        System.out.print("stddev                  = ");
        System.out.println(percolationStats.stddev());
        System.out.print("95% confidence interval = ");
        System.out.printf("[%s, %s]\n", percolationStats.confidenceLo(),
                          percolationStats.confidenceHi());
    }
}
