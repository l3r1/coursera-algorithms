/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] trialResults;
    private int trials;

    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Grid size and trials should be > 0");
        }

        this.trials = trials;
        this.trialResults = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {

                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                percolation.open(row, col);
            }

            int openSites = percolation.numberOfOpenSites();
            double result = (double) openSites / (n * n);
            this.trialResults[i] = result;
        }
    }

    public double mean() {
        return StdStats.mean(this.trialResults);
    }

    public double stddev() {
        return StdStats.stddev(this.trialResults);
    }

    public double confidenceLo() {
        return this.mean() - ((1.96 * this.stddev()) / Math.sqrt(this.trials));
    }

    public double confidenceHi() {
        return this.mean() + ((1.96 * this.stddev()) / Math.sqrt(this.trials));
    }

    public static void main(String[] args) {

        if (args.length >= 2) {
            PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]),
                                                          Integer.parseInt(args[1]));
            StdOut.println("mean = " + stats.mean());
            StdOut.println("stddev = " + stats.stddev());
            StdOut.println(
                    "95% confidence interval = [" + stats.confidenceLo() + ", " + stats
                            .confidenceHi()
                            + "]");
        }
    }
}
